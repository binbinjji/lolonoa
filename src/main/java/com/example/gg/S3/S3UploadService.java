package com.example.gg.S3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3UploadService {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public String saveFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, originalFilename, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, originalFilename).toString();
    }

    /*하나의 이미지 삭제*/
    @Transactional
    public void deleteImage(String originalFilename)  {
        String[] filename = originalFilename.split("/");
        amazonS3.deleteObject(bucket, filename[3]);
    }

//    /*하나의 이미지 삭제*/
//    @Transactional
//    public void deleteImage(String originalFilename)  {
//        amazonS3.deleteObject(bucket, originalFilename);
//    }


}
