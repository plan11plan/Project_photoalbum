package com.squarecross.photoalbum.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@Getter
@Setter
@ToString
public class MovePhotoDto {
    private Long fromAlbumId;
    private  Long toAlbumId;
   private  List<Long> photoIds;
}
