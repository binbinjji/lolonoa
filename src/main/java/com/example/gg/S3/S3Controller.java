package com.example.gg.S3;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/s3")
@AllArgsConstructor
public class S3Controller {
    private S3UploadService s3UploadService;

    /*파일 업로드 (1)개*/
    @Operation(summary = "이미지 업로드", description = "s3에 이미지 업로드")
    @PostMapping("/upload")
    public String upload(@RequestParam("images") MultipartFile multipartFile) throws IOException {
        return s3UploadService.saveFile(multipartFile);
    }

   /*파일 삭제 (1)개*/
   @Operation(summary = "이미지 삭제", description = "https://~형식 필요")
    @DeleteMapping("/delete")
    public void delete(@RequestParam("fileName") String fileName){
        s3UploadService.deleteImage(fileName);
    }

}
