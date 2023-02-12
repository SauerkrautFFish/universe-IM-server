package edu.yjzxc.universeimserver.controller;

import edu.yjzxc.universeimserver.utils.MinioUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/files")
@CrossOrigin("*")
@Slf4j
public class FilesController {

    @Resource
    private MinioUtil minioUtil;

    @Value("${minio.endpoint}")
    private String address;

    @Value("${minio.bucketName}")
    private String bucketName;

    @RequestMapping(method = RequestMethod.POST, value = "/upload")
    public Object upload(MultipartFile file) {
        List<String> uploadFiles = minioUtil.upload(new MultipartFile[]{file});

        return address + "/" + bucketName + "/" + uploadFiles.get(0);
    }
}
