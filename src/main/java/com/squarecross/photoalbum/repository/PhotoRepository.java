package com.squarecross.photoalbum.repository;

import com.squarecross.photoalbum.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long>,PhotoRepositoryCustom{
    /**
     * findBy 대신 countBy 를 입력하면 뒤에 붙는 조건을 기준으로 테이블에 레코드가 몇개 있는지 확인합니다.
     * countBy 뒤에 붙는 Album_AlbumId 는 참조하는 Album 엔티티의 AlbumId 값을 확인해 일치하는 Photo 레코드들을 찾습니다.
     * 즉 말로하면 특정 앨범에 속해있는 사진의 개수를 가져옵니다.
     */
    int countByAlbum_AlbumId(Long AlbumId);

    List<Photo> findTop4ByAlbum_AlbumIdOrderByUploadedAtDesc(Long AlbumId);

    Optional<Photo> findByFileNameAndAlbum_AlbumId(String photoName, Long albumId);

    List<Photo> findByPhotoId(Long[] photoId);
    List<Photo> findByPhotoIdList(List<Long> photoId);

//    //앨범에 있는 사진 목록 가져오기
//
//    List<Photo> findPhotosByAlbum_AlbumIdAndFileNameContainingOrderByUploadedAtAsc(Long albumId, String fileName);
//
//    List<Photo> findPhotosByAlbum_AlbumIdAndFileNameContainingOrderByUploadedAtDesc(Long albumId, String fileName);
//
//    List<Photo> findPhotosByAlbum_AlbumIdAndFileNameContainingOrderByFileNameDesc(Long albumId, String fileName);
//
//    List<Photo> findPhotosByAlbum_AlbumIdAndFileNameContainingOrderByFileNameAsc(Long albumId, String fileName);
    //앨범에 있는 사진 목록 가져오기

    List<Photo> findPhotosByAlbum_AlbumIdAndFileNameContainingOrderByUploadedAtAsc(Long albumId, String fileName);

    List<Photo> findPhotosByAlbum_AlbumIdAndFileNameContainingOrderByUploadedAtDesc(Long albumId, String fileName);

    List<Photo> findPhotosByAlbum_AlbumIdAndFileNameContainingOrderByFileNameDesc(Long albumId, String fileName);

    List<Photo> findPhotosByAlbum_AlbumIdAndFileNameContainingOrderByFileNameAsc(Long albumId, String fileName);

    List<Photo> findPhotosByAlbum_AlbumId(Long albumId);
}
