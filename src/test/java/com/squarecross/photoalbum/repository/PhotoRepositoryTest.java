package com.squarecross.photoalbum.repository;

import com.squarecross.photoalbum.domain.Photo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PhotoRepositoryTest {
    @Autowired
    PhotoRepository photoRepository;

    @Test
    public void getImageFiles() {


        Long[] photoIds = {80L,81L};
        List<Photo> result = photoRepository.findByPhotoIdIn(photoIds);
        // DB에 저장되어 있는 파일 목록을 읽어온다.
        for (Photo photo : result) {
            if (photo == null) {
                throw new EntityNotFoundException(String.format("사진을 ID %d를 찾을 수 없습니다."));
            }
            System.out.println(photo);
        }
    }
}