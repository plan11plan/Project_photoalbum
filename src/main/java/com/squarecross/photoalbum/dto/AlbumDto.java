package com.squarecross.photoalbum.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class AlbumDto {

    Long albumId;
    String albumName;
    int count;
    Date createdAt;
    private List<String> thumbUrls;




}
