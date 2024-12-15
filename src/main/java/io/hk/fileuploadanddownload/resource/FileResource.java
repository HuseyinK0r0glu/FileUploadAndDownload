package io.hk.fileuploadanddownload.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@RestController
@RequestMapping("/file")
public class FileResource {

    // define a location
    public static final String DIRECTORY = System.getProperty("user.home") + "Downloads/uploads";

    // define a method to upload files
    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadFile(@RequestParam("files") List<MultipartFile> multipartFiles) {
        List<String> filenames = new ArrayList<>();
        try{
            for(MultipartFile file : multipartFiles) {
                String filename = StringUtils.cleanPath(file.getOriginalFilename());
                Path fileStorage = Paths.get(DIRECTORY, filename).toAbsolutePath().normalize();
                Files.copy(file.getInputStream(),fileStorage,REPLACE_EXISTING);
                filenames.add(filename);
            }
            return ResponseEntity.ok().body(filenames);
        }catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    // define a method to download files

}
