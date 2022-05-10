package ru.kgogolev.app.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kgogolev.app.dto.FileDto;
import ru.kgogolev.app.exceptions.FileIsMissingException;
import ru.kgogolev.app.services.FilesService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FilesController {

    private final FilesService filesService;

    @GetMapping
    public List<FileDto> getFilesList() {
        return filesService.getListOfFiles();
    }

    @PostMapping
    public ResponseEntity<FileDto> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileIsMissingException("Необходимо прикрепить файл");
        }
        var uploadedFile = filesService.upload(file);
        return ResponseEntity.ok().body(uploadedFile);
    }


    @GetMapping("/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("filename") String filename) {

        var resource = filesService.downloadFile(filename);


        var contentType = "application/octet-stream";
        var headerValue = String.format("attachment; filename=%s", resource.getFilename());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);
    }

    @DeleteMapping("/{filename}")
    public ResponseEntity<Object> deleteFile(@PathVariable String filename) {
        filesService.markFileAsDeleted(filename);
        return ResponseEntity.ok("deleted");
    }
}
