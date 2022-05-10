package ru.kgogolev.app.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import ru.kgogolev.app.entity.FileEntity;
import ru.kgogolev.app.exceptions.FileSystemException;
import ru.kgogolev.app.util.mapper.FileToFileEntityMapper;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Repository
@Slf4j
public class FileDao {

    @Value("${files.root.directory}")
    private String rootDirectory;

    public List<FileEntity> findAll() {
        try {
            return Files.walk(Path.of(rootDirectory))
                    .map(Path::toFile)
                    .filter(File::isFile)
                    .map(FileToFileEntityMapper::map)
                    .collect(toList());
        } catch (IOException e) {
            log.error("Невозможно получить список файлов", e);
            throw new FileSystemException("Невозможно получить список файлов", e);
        }
    }


    public FileEntity save(String filename, byte[] data) throws IOException {
        var path = Path.of(rootDirectory, filename);
        Files.write(path, data);
        return FileToFileEntityMapper.map(path.toFile());
    }

    public void delete(String filename) throws IOException {
        Files.delete(Path.of(rootDirectory, filename));
    }

    @PostConstruct
    private void init() {
        var root = Path.of(rootDirectory).toFile();
        if (!root.exists()) {
            try {
                Files.createDirectory(root.toPath());
            } catch (IOException e) {
                log.error("Невозможно создать корневой каталог хранилища", e);
                throw new FileSystemException("Невозможно создать корневой каталог хранилища", e);
            }
        }
    }
}
