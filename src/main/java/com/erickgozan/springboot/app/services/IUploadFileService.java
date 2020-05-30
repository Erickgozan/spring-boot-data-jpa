package com.erickgozan.springboot.app.services;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IUploadFileService {

	// Guarda el archivo
	public String copyFile(MultipartFile file) throws IOException;

	// Mostra el archivo
	public Resource loadFile(String filename) throws MalformedURLException;

	// Elimina el archivo
	public boolean deleteFile(String filename);
	
	//Eliminar el directorio con todos y los archivos
	public void deleteAllFiles();
	
	//Crear el directorio
	public void initFile() throws IOException;
}
