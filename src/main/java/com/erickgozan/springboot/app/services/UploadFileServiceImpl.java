package com.erickgozan.springboot.app.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.erickgozan.springboot.app.controllers.ClienteController;

@Service
public class UploadFileServiceImpl implements IUploadFileService {

	private final static String FILE_REPOSITORY = "uploads";
	private final Logger LOG = LoggerFactory.getLogger(ClienteController.class);

	// Guarda el archivo en el servidor
	@Override
	public String copyFile(MultipartFile file) throws IOException {

		// Genera un nombre id aleatoreo de la imagen para que no se sobrescriban	
		String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

		Path absolutePath = getPath(uniqueFileName);

		LOG.info("rootPath: " + absolutePath);

		 Files.copy(file.getInputStream(), absolutePath);
		
		return uniqueFileName;
		
		
	}

	// Muestra el archivo(recurso) que se encuentra guardado en el servidor
	@Override
	public Resource loadFile(String filename) throws MalformedURLException {

		//Ruta absoluta de la foto.
		Path absolutePath = getPath(filename);
		//Recurso
		Resource recurso = null;
		//Establece la uri del archivo File://c://...
		recurso = new UrlResource(absolutePath.toUri());

		if (!recurso.exists() || !recurso.isReadable()) {
			throw new RuntimeException("Error: no se puede cargar la imagen: " + absolutePath.toString());
		}

		return recurso;
	}

	// Elimina el archivo del servidor
	@Override
	public boolean deleteFile(String filename) {

		Path absolutePath = getPath(filename);
		File archivo = absolutePath.toFile();

		if (archivo.exists() && archivo.canRead()) {
			if (archivo.delete()) {
				return true;
			}
		}
		return false;
	}

	// Obtener la ruta absoluta del archivo
	private Path getPath(String filename) {
		return Paths.get(FILE_REPOSITORY).resolve(filename).toAbsolutePath();
	}
	
	

	@Override
	public void deleteAllFiles() {
		
		FileSystemUtils.deleteRecursively(Paths.get(FILE_REPOSITORY).toFile());
		
	}

	@Override
	public void initFile() throws IOException {
		Files.createDirectory(Paths.get(FILE_REPOSITORY));		
	}

}
