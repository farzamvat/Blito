package com.blito.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

@Service
public class ImageService {

	public CompletableFuture<String> save(String encodedBase64) {
		return CompletableFuture.supplyAsync(() -> {
			String uuid = UUID.randomUUID().toString();
			try {
				PrintWriter writer = new PrintWriter("images/"+uuid+".txt", "UTF-8");
				writer.write(encodedBase64);
				writer.close();
				return uuid;
				
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e.getMessage());
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage());
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
	
	
}