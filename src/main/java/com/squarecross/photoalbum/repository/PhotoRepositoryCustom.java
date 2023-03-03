package com.squarecross.photoalbum.repository;

import com.squarecross.photoalbum.domain.Photo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PhotoRepositoryCustom {
    List<Photo>fdesc(Long albumId, String fileName);
    List<Photo> fasc(Long albumId, String fileName);
    List<Photo> tdesc(Long albumId, String fileName);
    List<Photo> tasc(Long albumId, String fileName);
    List<Photo> findPhotosByPhotoIdIn(List<Long> photoId);


}
