package com.squarecross.photoalbum.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class PhotoDto {
    private Long photoId;
    private Long albumId;
    private String fileName;
    private String originalUrl;
    private Date uploadedAt;
    private String thumbUrl;
    private Long fileSize;

}
