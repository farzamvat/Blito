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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

@Service
public class ImageService {

	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private ImageMapper imageMapper;

	public CompletableFuture<String> save(String encodedBase64) {
		return CompletableFuture.supplyAsync(() -> {
			String uuid = UUID.randomUUID().toString();
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
		});
	}

	public CompletableFuture<String> save(String encodedBase64, String defaultId) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				File file = new File("images/");
				if (!file.exists())
					file.mkdir();
				PrintWriter writer = new PrintWriter("images/" + defaultId + ".txt", "UTF-8");
				writer.write(encodedBase64);
				writer.close();
				return defaultId;
			} catch (Exception e) {
				throw new NotFoundException(ResourceUtil.getMessage(Response.IMAGE_NOT_FOUND));
			}
		});
	}

	public CompletableFuture<String> getFileEncodedBase64(String id) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				return new String(Files.readAllBytes(Paths.get("images/" + id + ".txt")));
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage());
			}
		});
	}

	public CompletableFuture<String> saveMultipartFile(MultipartFile file) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				return new String(file.getBytes());
			} catch (IOException e) {
				throw new InternalServerException(ResourceUtil.getMessage(Response.INTERNAL_SERVER_ERROR));
			}
		}).thenCompose(base64 -> this.save(base64));
	}

	public CompletableFuture<Void> deleteAsync(String uuid) {
		return CompletableFuture.runAsync(() -> {
			Image image = imageRepository.findByImageUUID(uuid)
					.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.IMAGE_NOT_FOUND)));
			try {

				if (Files.deleteIfExists(Paths.get("images/" + image.getImageUUID() + ".txt")))
					imageRepository.delete(image);
				else
					throw new NotFoundException(ResourceUtil.getMessage(Response.IMAGE_NOT_FOUND));
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage());
			}
		});
	}

	public void delete(String uid) {
		Image image = imageRepository.findByImageUUID(uid)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.IMAGE_NOT_FOUND)));
		try {

			if (Files.deleteIfExists(Paths.get("images/" + image.getImageUUID() + ".txt")))
				imageRepository.delete(image);
			else
				throw new NotFoundException(ResourceUtil.getMessage(Response.IMAGE_NOT_FOUND));
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public CompletableFuture<ImageViewModel> createOrUpdateDefaultImage(MultipartFile file, String defaultImageId) {
		return CompletableFuture.supplyAsync(() -> {
			if (defaultImageId.equals(Constants.DEFAULT_HOST_COVER_PHOTO)) {
				return imageMapper
						.createFromEntity(persistDefaultImage(file, defaultImageId, ImageType.HOST_COVER_PHOTO));
			} else if (defaultImageId.equals(Constants.DEFAULT_EVENT_PHOTO)) {
				return imageMapper.createFromEntity(persistDefaultImage(file, defaultImageId, ImageType.EVENT_PHOTO));
			} else if (defaultImageId.equals(Constants.DEFAULT_EXCHANGEBLIT_PHOTO)) {
				return imageMapper
						.createFromEntity(persistDefaultImage(file, defaultImageId, ImageType.EXCHANGEBLIT_PHOTO));
			} else if (defaultImageId.equals(Constants.DEFAULT_HOST_PHOTO)) {
				return imageMapper.createFromEntity(persistDefaultImage(file, defaultImageId, ImageType.HOST_PHOTO));
			} else {
				throw new RuntimeException("default id is not valid");
			}
		});
	}

	@Transactional
	public Image persistDefaultImage(MultipartFile file,String defaultId,ImageType imageType)
	{
		return imageRepository.findByImageUUID(defaultId)
				.map(image -> {
					delete(defaultId);
					try {
						return save(new String(file.getBytes()),defaultId).thenApply(id -> {
							image.setImageUUID(id);
							image.setImageType(imageType.name());
							return imageRepository.save(image);
						}).handle((result,throwable) -> {
							if(throwable != null)
								return image;
							return result;
						}).join();
					} catch (IOException e) {
						throw new InternalServerException(ResourceUtil.getMessage(Response.INTERNAL_SERVER_ERROR));
					}
				})
				.orElseGet(() -> {
					Image image = new Image();
					try {
						image.setImageUUID(save(new String(file.getBytes()), defaultId).join());
					} catch (IOException e) {
						throw new RuntimeException(e.getMessage());
					}
					image.setImageType(imageType.name());
					return image;
				}).handle((result, throwable) -> {
					if (throwable != null)
						return image;
					return result;
				}).join();
			} catch (IOException e) {
				throw new InternalServerException(ResourceUtil.getMessage(Response.INTERNAL_SERVER_ERROR));
			}
		}).orElseGet(() -> {
			Image image = new Image();
			try {
				image.setImageUUID(save(new String(file.getBytes()), defaultId).join());
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage());
			}
			image.setImageType(imageType.name());
			return imageRepository.save(image);
		});

	}

	public Page<ImageViewModel> getDefaultImages() {
		List<ImageViewModel> images;
		ImageViewModel defaultHostCoverPhoto = imageMapper
				.createFromEntity(imageRepository.findByImageUUID(Constants.DEFAULT_HOST_COVER_PHOTO)
						.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.IMAGE_NOT_FOUND))));
		ImageViewModel defaultHostPhoto = imageMapper
				.createFromEntity(imageRepository.findByImageUUID(Constants.DEFAULT_HOST_PHOTO)
						.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.IMAGE_NOT_FOUND))));
		ImageViewModel defaultEventPhoto = imageMapper
				.createFromEntity(imageRepository.findByImageUUID(Constants.DEFAULT_EVENT_PHOTO)
						.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.IMAGE_NOT_FOUND))));
		ImageViewModel defaultExchangeBlitPhoto = imageMapper
				.createFromEntity(imageRepository.findByImageUUID(Constants.DEFAULT_EXCHANGEBLIT_PHOTO)
						.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.IMAGE_NOT_FOUND))));
		images = Arrays.asList(defaultHostCoverPhoto,defaultHostPhoto,defaultEventPhoto,defaultExchangeBlitPhoto);
		Page<ImageViewModel> defaultImages = new PageImpl<>(images);
		return defaultImages;
	}

}