package com.squarecross.photoalbum.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class AlbumDto {

    Long albumId;
    @NotEmpty
    String albumName;

    int count;
    Date createdAt;
    private List<String> thumbUrls;


}
