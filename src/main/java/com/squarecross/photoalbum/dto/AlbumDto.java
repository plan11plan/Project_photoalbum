package com.squarecross.photoalbum.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class AlbumDto {

    Long albumId;
    String albumName;
    int count;
    LocalDateTime createdAt;


    public AlbumDto(Long albumId, String albumName,LocalDateTime createdAt ) {
        this.albumId = albumId;
        this.albumName = albumName;
        this.createdAt = createdAt;

    }
}
