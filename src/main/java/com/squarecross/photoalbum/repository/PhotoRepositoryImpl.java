package com.squarecross.photoalbum.repository;

import com.squarecross.photoalbum.domain.Photo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import java.util.List;
@RequiredArgsConstructor
public class PhotoRepositoryImpl implements PhotoRepositoryCustom{
    private final EntityManager em;

    @Override
    public List<Photo> fdesc(Long albumId, String fileName){
        return em.createQuery("select p from Photo p where p.album.albumId= :albumId and p.fileName LIKE CONCAT('%',:fileName,'%') order by p.fileName desc")
                .setParameter("albumId",albumId)
                .setParameter("fileName",fileName)
                .getResultList();
    }
    @Override
    public List<Photo> fasc(Long albumId, String fileName){
        return em.createQuery("select p from Photo p where p.album.albumId= :albumId and p.fileName LIKE CONCAT('%',:fileName,'%') order by p.fileName asc")
                .setParameter("albumId",albumId)
                .setParameter("fileName",fileName)
                .getResultList();
    }
    @Override
    public List<Photo> tdesc(Long albumId, String fileName){
        return em.createQuery("select p from Photo p where p.album.albumId= :albumId and p.fileName LIKE CONCAT('%',:fileName,'%') order by p.uploadedAt desc")
                .setParameter("albumId",albumId)
                .setParameter("fileName",fileName)
                .getResultList();
    }
    @Override
    public List<Photo> tasc(Long albumId, String fileName){
        return em.createQuery("select p from Photo p where p.album.albumId= :albumId and p.fileName LIKE CONCAT('%',:fileName,'%') order by p.uploadedAt asc")
                .setParameter("albumId",albumId)
                .setParameter("fileName",fileName)
                .getResultList();
    }

    @Override
    public List<Photo> findPhotosByPhotoIdIn(List<Long> photoId){
        return em.createQuery("select c from Photo c where c.id in :photoId")
                .setParameter("photoId",photoId)
                .getResultList();
    }




}
