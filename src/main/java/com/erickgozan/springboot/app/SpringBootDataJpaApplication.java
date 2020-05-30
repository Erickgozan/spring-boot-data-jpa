package com.erickgozan.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.erickgozan.springboot.app.services.IUploadFileService;

@SpringBootApplication
public class SpringBootDataJpaApplication implements CommandLineRunner{
	
	@Autowired
	IUploadFileService uploadService;


	public static void main(String[] args) {
		SpringApplication.run(SpringBootDataJpaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//Eliminar el directorio con todos y los archivos
		uploadService.deleteAllFiles();
		//Crea el directorio
		uploadService.initFile();
				
	}

}
