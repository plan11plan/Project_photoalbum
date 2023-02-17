package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.PhotoDto;
import com.squarecross.photoalbum.repository.PhotoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

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
    @Test
    @DisplayName("여러 아이디로 사진들 한번에 조회")
    void getImageFiles() {
        long[] photoIds = {1, 2, 3};
        Long[] photoIdsLong = new Long[photoIds.length];
        for (int i = 0; i < photoIds.length; i++) {
            photoIdsLong[i] = Long.valueOf(photoIds[i]);
        }
        List<File> imageFiles = photoService.getImageFiles(photoIdsLong);
        System.out.println(imageFiles);
    }
}
