package com.g7.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.g7.model.FileKey;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

@Service
public class S3Service {

    private final AmazonS3 s3client;

    private final String bucketName= "g7-s3";

    public S3Service() {
        String accessKey = "";
        String secretKey = "";
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        String region = "eu-west-1";
        s3client = AmazonS3Client.builder()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
    }

    public String uploadFile(MultipartFile multipartFile) {
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            uploadFileTos3bucket(fileName, file);
            Files.delete(file.toPath());
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public String getFileAsBase64(FileKey fileKey) {
        try {
            S3ObjectInputStream object = getObject(fileKey.getPath());
            byte[] fileContent = IOUtils.toByteArray(object);
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (Exception e) {
            return null;
        }
    }

    public S3ObjectInputStream getObject(String key) {

        S3Object object = s3client.getObject(bucketName, key);
        return object.getObjectContent();
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {

        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }

    private String generateFileName(MultipartFile multiPart) {

        String date = new Date().getTime() + "-" + multiPart.getOriginalFilename();
        date = date.replace(" ", "_");

        return date;
    }

    public void uploadFileTos3bucket(String fileName, File file) {

        s3client.putObject(
                new PutObjectRequest(bucketName, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
    }

    public String deleteFileFromS3Bucket(String fileUrl) {

        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        s3client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
        return "Successfully deleted";
    }

    private String getRandomString() {

        String ab = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();

        StringBuilder sb = new StringBuilder(7);
        for (int i = 0; i < 7; i++) {
            sb.append(ab.charAt(rnd.nextInt(ab.length())));
        }
        String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        return date + "-" + sb;

    }

}
