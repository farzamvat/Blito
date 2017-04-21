package com.blito.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class ImageService {

	public String save(String encodedBase64) {
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
	}
	
	
}
