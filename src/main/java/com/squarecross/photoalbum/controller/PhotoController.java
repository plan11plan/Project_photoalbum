package com.squarecross.photoalbum.controller;


import com.squarecross.photoalbum.dto.AlbumDto;
import com.squarecross.photoalbum.dto.PhotoDto;
import com.squarecross.photoalbum.service.AlbumService;
import com.squarecross.photoalbum.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/albums/{albumId}/photos")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;
    private final AlbumService albumService;

    /**
     * 사진 상세정보 API
     */
    @GetMapping("/{photoId}")
    public ResponseEntity<PhotoDto> getPhotoInfo(@PathVariable("albumId") final long albumId,
                                                 @PathVariable("photoId") final long photoId) {
        PhotoDto photoDto = photoService.getPhoto(albumId,photoId);
        return new ResponseEntity<>(photoDto, HttpStatus.OK);
    }
    /**
     * 사진 업로드 API
     */
    @PostMapping("")
    public ResponseEntity<List<PhotoDto>> uploadPhotos


}
