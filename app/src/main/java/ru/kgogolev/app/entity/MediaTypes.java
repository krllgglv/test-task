package ru.kgogolev.app.entity;

public enum MediaTypes {
    PICTURE("/images/picture.png","jpg", "bmp","png"),
    VIDEO("/images/videos.png","avi", "mpeg", "mp4"),
    PDF("/images/pdf.png","pdf"),
    TEXT("/images/text.png","txt","rtf","docx"),
    FILE("/images/file.png");

     String imageUri;
     String [] extensions;

     MediaTypes(String imageURI, String... extensions){
         this.imageUri = imageURI;
        this.extensions = extensions;
    }

    public String[] getExtensions() {
        return extensions;
    }

    public String getImageUri() {
        return imageUri;
    }
}
