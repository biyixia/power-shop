package com.bjpowernode.controller;

import com.bjpowernode.utils.UploadUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("/admin/file")
@RequiredArgsConstructor
public class AdminFileController {
    private final UploadUtils uploadUtils;
    @PostMapping("upload/element")
    public ResponseEntity<String> upload(MultipartFile file) {
        String fireDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = fireDir + "/" + UUID.randomUUID().toString() + suffix;
        try {
            return ResponseEntity.ok(
                    uploadUtils.upload(file.getBytes(), fileName)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
