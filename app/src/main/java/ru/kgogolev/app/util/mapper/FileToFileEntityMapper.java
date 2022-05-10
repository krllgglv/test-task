package ru.kgogolev.app.util.mapper;

import lombok.experimental.UtilityClass;
import ru.kgogolev.app.entity.FileEntity;
import ru.kgogolev.app.util.FileUtils;

import java.io.File;

@UtilityClass
public class FileToFileEntityMapper {
    public static FileEntity map(File object) {
        var fileEntity = new FileEntity();
        fileEntity.setFullPath(object.toPath());
        fileEntity.setMediaType(FileUtils.getFileMediaType(object.getName()));
        fileEntity.setDeleted(false);
        return fileEntity;
    }
}
