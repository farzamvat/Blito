package com.blito.services;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.blito.mappers.GenericMapper;
import com.blito.rest.viewmodels.exception.ExceptionViewModel;
import com.blito.rest.viewmodels.image.ImageBase64ViewModel;
import io.vavr.Function2;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.blito.configs.Constants;
import com.blito.enums.ImageType;
import com.blito.enums.Response;
import com.blito.exceptions.InternalServerException;
import com.blito.exceptions.NotFoundException;
import com.blito.mappers.ImageMapper;
import com.blito.models.Image;
import com.blito.repositories.ImageRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.image.ImageViewModel;

import javax.swing.text.html.ImageView;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private ImageMapper imageMapper;

    public CompletableFuture<String> save(String encodedBase64) {
        return CompletableFuture.supplyAsync(() -> {
            String uuid = UUID.randomUUID().toString();
            return saveImage(uuid, encodedBase64);
        });
    }

    public String saveSync(String encodedBase64) {
        String uuid = UUID.randomUUID().toString();
        return saveImage(uuid, encodedBase64);
    }

    public String saveImage(String uuid, String encodedBase64) {
        try {
            File file = new File("images/");
            if (!file.exists()) {
                file.mkdir();
            }
            PrintWriter writer = new PrintWriter("images/" + uuid + ".txt", "UTF-8");
            writer.write(encodedBase64);
            writer.close();
            return uuid;
        } catch (Exception e) {
            throw new NotFoundException(ResourceUtil.getMessage(Response.IMAGE_NOT_FOUND));
        }
    }

    public CompletableFuture<String> save(String encodedBase64, String defaultId) {
        return CompletableFuture.supplyAsync(() -> saveImage(defaultId, encodedBase64));
    }

    public Try<String> getFileEncodedBase64(String id) {
        return Try.ofSupplier(() -> {
            try {
                return new String(Files.readAllBytes(Paths.get("images/" + id + ".txt")));
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        });
    }

    public CompletableFuture<String> getFileEncodedBase64Async(String id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return new String(Files.readAllBytes(Paths.get("images/" + id + ".txt")));
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        });
    }

    public Either<ExceptionViewModel, ImageViewModel> saveMultipartFile(MultipartFile file) {
        return Try.ofSupplier(() -> {
            try {
                return new String(file.getBytes());
            } catch (IOException e) {
                throw new InternalServerException(ResourceUtil.getMessage(Response.INTERNAL_SERVER_ERROR));
            }
        }).map(this::saveSync)
                .map(uid -> imageRepository.save(new Image(uid)).getImageUUID())
                .map(ImageViewModel::new).toRight(new ExceptionViewModel(ResourceUtil.getMessage(Response.INTERNAL_SERVER_ERROR), 500));
    }

    public CompletableFuture<Void> deleteAsync(String uuid) {
        return CompletableFuture.runAsync(() -> {
            Image image = imageRepository.findByImageUUID(uuid)
                    .orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.IMAGE_NOT_FOUND)));
            deleteImageFromFileSystemAndDatabase(image);
        });
    }

    public void deleteImageFromFileSystemAndDatabase(Image image) {
        try {
            if (Files.deleteIfExists(Paths.get("images/" + image.getImageUUID() + ".txt")))
                imageRepository.delete(image);
            else
                throw new NotFoundException(ResourceUtil.getMessage(Response.IMAGE_NOT_FOUND));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Transactional
    public void delete(String uid) {
        List<String> defaults = Arrays.asList(Constants.DEFAULT_EVENT_PHOTO, Constants.DEFAULT_EXCHANGEBLIT_PHOTO,
                Constants.DEFAULT_HOST_COVER_PHOTO_1, Constants.DEFAULT_HOST_COVER_PHOTO_2, Constants.DEFAULT_HOST_COVER_PHOTO_3,
                Constants.DEFAULT_HOST_COVER_PHOTO_4, Constants.DEFAULT_HOST_PHOTO);
        if (defaults.contains(uid))
            return;
        Image image = imageRepository.findByImageUUID(uid)
                .orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.IMAGE_NOT_FOUND)));
        deleteImageFromFileSystemAndDatabase(image);
    }


    public Either<ExceptionViewModel,?> createOrUpdateDefaultImage(MultipartFile file, String defaultImageId) {
        Function<Either<ExceptionViewModel,Image>,Either<ExceptionViewModel,ImageViewModel>> imageEitherToImageViewModelEither =
                (either) -> {
                    if(either.isLeft())
                        return Either.left(either.getLeft());
                    else
                        return Either.right(imageMapper.createFromEntity(either.get()));
                };
        if (defaultImageId.equals(Constants.DEFAULT_HOST_COVER_PHOTO_1)) {
            return imageEitherToImageViewModelEither.apply(persistDefaultImage(file, defaultImageId, ImageType.HOST_COVER_PHOTO));
        } else if (defaultImageId.equals(Constants.DEFAULT_HOST_COVER_PHOTO_2)) {
            return imageEitherToImageViewModelEither.apply(persistDefaultImage(file, defaultImageId, ImageType.HOST_COVER_PHOTO));
        } else if (defaultImageId.equals(Constants.DEFAULT_HOST_COVER_PHOTO_3)) {
            return imageEitherToImageViewModelEither.apply(persistDefaultImage(file, defaultImageId, ImageType.HOST_COVER_PHOTO));
        } else if (defaultImageId.equals(Constants.DEFAULT_HOST_COVER_PHOTO_4)) {
            return imageEitherToImageViewModelEither.apply(persistDefaultImage(file, defaultImageId, ImageType.HOST_COVER_PHOTO));
        } else if (defaultImageId.equals(Constants.DEFAULT_EVENT_PHOTO)) {
            return imageEitherToImageViewModelEither.apply(persistDefaultImage(file, defaultImageId, ImageType.EVENT_PHOTO));
        } else if (defaultImageId.equals(Constants.DEFAULT_EXCHANGEBLIT_PHOTO)) {
            return imageEitherToImageViewModelEither.apply(persistDefaultImage(file, defaultImageId, ImageType.EXCHANGEBLIT_PHOTO));
        } else if (defaultImageId.equals(Constants.DEFAULT_HOST_PHOTO)) {
            return imageEitherToImageViewModelEither.apply(persistDefaultImage(file, defaultImageId, ImageType.HOST_PHOTO));
        } else {
            throw new RuntimeException("default id is not valid");
        }

    }

    @Transactional
    public Either<ExceptionViewModel, Image> persistDefaultImage(MultipartFile file, String defaultId, ImageType imageType) {
        BiFunction<String, Option<Image>, Image> saveImageLambda = (id, optionalImage) ->
                optionalImage.map(image -> {
                    image.setImageType(imageType.name());
                    image.setImageUUID(id);
                    return imageRepository.save(image);
                }).orElse(() -> {
                    Image newImage = new Image();
                    newImage.setImageUUID(id);
                    newImage.setImageType(imageType.name());
                    return Option.of(imageRepository.save(newImage));
                }).get();
        return Option.ofOptional(imageRepository.findByImageUUID(defaultId))
                .map(image ->
                        Try.ofSupplier(() -> trySaveImage(defaultId, file))
                                .map(uid -> saveImageLambda.apply(uid, Option.of(image)))
                )
                .orElse(() ->
                        Option.of(Try.ofSupplier(() -> trySaveImage(defaultId, file))
                                .map(uid -> saveImageLambda.apply(uid, Option.none())))
                )
                .filter(Try::isSuccess)
                .map(Try::get)
                .toRight(new ExceptionViewModel(ResourceUtil.getMessage(Response.INTERNAL_SERVER_ERROR), 500));

    }

    private String trySaveImage(String defaultId, MultipartFile file) {
        try {
            return saveImage(defaultId, new String(file.getBytes()));
        } catch (IOException e) {
            throw new InternalServerException(ResourceUtil.getMessage(Response.INTERNAL_SERVER_ERROR));
        }
    }

    public Either<ExceptionViewModel, ImageBase64ViewModel> downloadImage(String id) {
        return Option.ofOptional(imageRepository.findByImageUUID(id))
                .map(image -> getFileEncodedBase64(image.getImageUUID()))
                .filter(Try::isSuccess)
                .map(Try::get)
                .map(ImageBase64ViewModel::new)
                .toRight(() -> new ExceptionViewModel(ResourceUtil.getMessage(Response.IMAGE_NOT_FOUND), 400));
    }

}