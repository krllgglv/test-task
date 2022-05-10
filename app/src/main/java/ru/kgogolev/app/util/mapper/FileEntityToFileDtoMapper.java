package ru.kgogolev.app.util.mapper;

import lombok.experimental.UtilityClass;
import ru.kgogolev.app.dto.FileDto;
import ru.kgogolev.app.entity.FileEntity;

@UtilityClass
public class FileEntityToFileDtoMapper {

    public static FileDto map(FileEntity object) {
        var fileDto = new FileDto();
        fileDto.setFilename(object.getFullPath().getFileName().toString());
        fileDto.setImageURI(object.getMediaType().getImageUri());
        return fileDto;
    }
}
