package br.com.welissontiago.service;

import br.com.welissontiago.config.FileStorageConfig;
import br.com.welissontiago.exceptions.FileNotFoundException;
import br.com.welissontiago.exceptions.FileStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    private final static Logger log = LoggerFactory.getLogger(FileStorageService.class);

    @Autowired
    public FileStorageService(FileStorageConfig config) {
        Path path = Paths.get(config.getUploadDir()).toAbsolutePath().normalize();
        this.fileStorageLocation = path;
        try{
            log.info("Creating directory");
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception e) {
            log.error("Could not create the directory where");
            throw new FileStorageException("Could not create the directory where",e);
        }
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try{
            if(fileName.contains("..")){
                log.error("Sorry! Filename contains invalid path sequence");
                throw new FileStorageException("Sorry! Filename contains invalid path sequence" + fileName);
            }
            log.info("Saving file to disk");
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (Exception e) {
            log.error("Could not store file", e);
            throw new FileStorageException("Could not store file",e);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try{
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()){
                return resource;
            }else {
                throw new FileNotFoundException("File not found: " + fileName);
            }
        } catch (Exception e) {
            log.error("File not found:", e);
            throw new FileNotFoundException("File not found: " + fileName, e);
        }
    }


}
