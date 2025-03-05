package io.hk.fileuploadanddownload.resource;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@RestController
@RequestMapping("/file")
public class FileResource {

    // define a location
    public static final String DIRECTORY = System.getProperty("user.home") + "/Downloads/uploads";

    // define a method to upload files
    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadFile(@RequestParam("files") List<MultipartFile> multipartFiles) {

        File uploadDir = new File(DIRECTORY);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

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
    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadFiles(@PathVariable("filename") String filename) throws IOException {
        Path filePath = Paths.get(DIRECTORY).toAbsolutePath().normalize().resolve(filename);
        if(!Files.exists(filePath)){
            throw new FileNotFoundException(filename + " was not found on the server");
        }

        Resource resource = new UrlResource(filePath.toUri());

        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("File-Name",filename);
        headers.add(CONTENT_DISPOSITION,"attachment;File-Name=" + resource.getFilename());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .headers(headers)
                .body(resource);
    }

    @GetMapping("/files")
    public ResponseEntity<List<String>> listFiles() {
        File uploadDir = new File(DIRECTORY);
        if (!uploadDir.exists() || !uploadDir.isDirectory()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        // Get the list of files in the directory
        String[] files = uploadDir.list();
        List<String> filenames = new ArrayList<>();
        if (files != null) {
            filenames.addAll(Arrays.asList(files));
        }

        return ResponseEntity.ok().body(filenames);
    }
}
