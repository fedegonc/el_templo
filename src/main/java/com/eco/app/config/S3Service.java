package com.eco.app.config;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class S3Service {

    @Value("${aws.s3.bucket}")
    private String bucketName;
    
    @Value("${aws.s3.url}")
    private String s3Url;
    
    @Value("${aws.simulation:true}")
    private boolean simulation;
    
    private final AmazonS3 amazonS3;
    
    @Autowired
    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }
    
    /**
     * Sube un archivo a S3 y devuelve la URL pública
     */
    public String uploadFile(MultipartFile file) throws IOException {
        // Si estamos en modo simulación, devolvemos una URL ficticia
        if (simulation) {
            String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
            log.info("Modo simulación: Archivo {} simulado como subido a S3", fileName);
            return "https://simulacion-s3.com/" + fileName;
        }
        
        // Generar un nombre único para el archivo
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        
        // Configurar los metadatos del objeto
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        
        // Crear la solicitud de subida sin configurar ACLs
        PutObjectRequest request = new PutObjectRequest(
                bucketName, 
                fileName, 
                file.getInputStream(), 
                metadata
        );
        
        // Nota: Ya no usamos .withCannedAcl(CannedAccessControlList.PublicRead) porque el bucket no permite ACLs
        
        // Subir el archivo a S3
        amazonS3.putObject(request);
        
        // Devolver la URL pública del archivo
        return s3Url + "/" + fileName;
    }
    
    /**
     * Elimina un archivo de S3
     */
    public void deleteFile(String fileUrl) {
        if (simulation) {
            log.info("Modo simulación: Archivo {} simulado como eliminado de S3", fileUrl);
            return;
        }
        
        // Extraer el nombre del archivo de la URL
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        
        // Eliminar el archivo de S3
        amazonS3.deleteObject(bucketName, fileName);
    }
}
