package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.PhotoDto;
import com.squarecross.photoalbum.repository.PhotoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class PhotoServiceTest {

    @Autowired
    PhotoService photoService;

    @Test
    @DisplayName("사진 상세정보 가져오기")
    void getPhoto(){
        Long albumId = 1L;
        Long photoId = 1L;

        PhotoDto photo = photoService.getPhoto(albumId, photoId);
        System.out.println(photo);
    }
}
