package ru.kgogolev.app.entity;


import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;

@RequiredArgsConstructor
@Data
public class FileEntity {
    private Path fullPath;
    private MediaTypes mediaType;
    private boolean isDeleted;
}
