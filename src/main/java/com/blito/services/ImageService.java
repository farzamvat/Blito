package com.blito.services;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.blito.enums.Response;
import com.blito.exceptions.InternalServerException;
import com.blito.exceptions.NotFoundException;
import com.blito.models.Image;
import com.blito.repositories.ImageRepository;
import com.blito.resourceUtil.ResourceUtil;

@Service
public class ImageService {
	
	@Autowired
	ImageRepository imageRepository;

	public CompletableFuture<String> save(String encodedBase64) {
		return CompletableFuture.supplyAsync(() -> {
			String uuid = UUID.randomUUID().toString();
			try {
				File file = new File("images/");
				if (!file.exists()) {
					file.mkdir();
				}
				PrintWriter writer = new PrintWriter("images/"+uuid+".txt", "UTF-8");
				writer.write(encodedBase64);
				writer.close();
				return uuid;
			} catch (Exception e) {
				throw new NotFoundException(ResourceUtil.getMessage(Response.IMAGE_NOT_FOUND));
			}
		});
	}
	
	public CompletableFuture<String> getFileEncodedBase64(String id)
	{	
		return CompletableFuture.supplyAsync(() -> {
			try {
				return new String(Files.readAllBytes(Paths.get("images/"+id+".txt")));
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage());
			}
		});
	}
	
	public CompletableFuture<String> saveMultipartFile(MultipartFile file)
	{
		return CompletableFuture.supplyAsync(() -> {
			try {
				return new String(file.getBytes());
			} catch (IOException e) {
				throw new InternalServerException(ResourceUtil.getMessage(Response.INTERNAL_SERVER_ERROR));
			}
		}).thenCompose(base64 -> this.save(base64));
	}
	
	public CompletableFuture<Void> delete(String uuid)
	{
		return CompletableFuture.runAsync(() -> {
			Image image = imageRepository.findByImageUUID(uuid).orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.IMAGE_NOT_FOUND)));
			try {
				
				if(Files.deleteIfExists(Paths.get("images/" + image.getImageUUID() + ".txt")))
					imageRepository.delete(image);
				else
					throw new NotFoundException(ResourceUtil.getMessage(Response.IMAGE_NOT_FOUND));
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage());
			}
		});
	}
	
}