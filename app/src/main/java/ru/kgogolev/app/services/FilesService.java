package ru.kgogolev.app.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.kgogolev.app.dao.FileDao;
import ru.kgogolev.app.dto.FileDto;
import ru.kgogolev.app.entity.FileEntity;
import ru.kgogolev.app.exceptions.CantReadFileException;
import ru.kgogolev.app.exceptions.CantSaveFileException;
import ru.kgogolev.app.exceptions.FileIsMissingException;
import ru.kgogolev.app.util.mapper.FileEntityToFileDtoMapper;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class FilesService {
    private List<FileEntity> cache;
    private final FileDao fileDao;


    public void markFileAsDeleted(String filename) {
        var file = getFileEntityByFilename(filename);
        if (file.isEmpty()) {
            log.error("Попытка удалить несцществующий файл: " + filename);
            throw new FileIsMissingException(String.format("Файл %s перемещен или удален", filename));
        }
        var fileEntity = file.get();
        fileEntity.setDeleted(true);
        log.info(String.format(" Файл %s отмечен для дальнейшего удаления", filename));
    }

    public FileDto upload(MultipartFile file) {
        var filename = new String(file.getOriginalFilename().getBytes(), StandardCharsets.UTF_8);
        try {
            var data = file.getBytes();
            var savedFile = fileDao.save(filename, data);
            if (getFileEntityByFilename(filename).isEmpty()) {
                cache.add(savedFile);
            }
            log.info("Файл успешно загружен :" + filename);
            return FileEntityToFileDtoMapper.map(savedFile);
        } catch (IOException e) {
            log.error("Невозможно сохранить файл:" + filename, e);
            throw new CantSaveFileException("Невозможно сохранить файл" + filename, e);
        }
    }

    public Resource downloadFile(String filename) {
        var mayBeFileEntity = getFileEntityByFilename(filename);
        if (mayBeFileEntity.isEmpty()) {
            log.error(String.format("Файл %s не найден", filename));
            throw new FileIsMissingException(String.format("Файл %s не найден", filename));
        }
        var fileEntity = mayBeFileEntity.get();
        if (fileEntity.isDeleted()) {
            log.error("Попытка скачать удаленный или перемещенный файл: " + fileEntity.getFullPath().getFileName());
            throw new FileIsMissingException(String.format("Файл %s перемещен или удален", filename));

        }

        Resource resource = new FileSystemResource(fileEntity.getFullPath());
        if (resource.exists() || resource.isReadable()) {
            log.info("Файл скачан " + filename);
            return resource;
        } else {
            log.error("Невозможно прочитиать файл " + filename);
            throw new CantReadFileException("Невозможно прочитиать файл " + filename);
        }


    }

    public List<FileDto> getListOfFiles() {
        return cache.stream()
                .filter(it -> !it.isDeleted())
                .map(FileEntityToFileDtoMapper::map)
                .sorted((f, s) -> f.getFilename().compareToIgnoreCase(s.getFilename())).toList();
    }

    @PostConstruct
    private void init() {
        cache = new CopyOnWriteArrayList<>(fileDao.findAll());
    }

    @Scheduled(cron = "${files.cron.delete_period}")
    private void deleteFiles() {
        for (FileEntity fileEntity : cache) {
            if (fileEntity.isDeleted()) {
                try {
                    fileDao.delete(fileEntity.getFullPath().getFileName().toString());
                    log.info(String.format("файл %s успешно удален", fileEntity.getFullPath().getFileName()));
                } catch (IOException e) {
                    log.error(String.format("файл %s не может быть удален", fileEntity.getFullPath().getFileName()), e);
                }
            }
        }
    }

    private Optional<FileEntity> getFileEntityByFilename(String filename) {
        return cache.stream()
                .filter(it -> filename.equalsIgnoreCase(it.getFullPath().getFileName().toString()))
                .findFirst();
    }
}


