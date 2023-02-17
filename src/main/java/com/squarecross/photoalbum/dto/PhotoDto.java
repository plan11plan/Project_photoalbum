package com.squarecross.photoalbum.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@ToString
public class PhotoDto {
    private Long photoId;
    private Long albumId;
    @NotEmpty
    private String fileName;
    private String originalUrl;
    private Date uploadedAt;
    private String thumbUrl;
    private Long fileSize;

}
