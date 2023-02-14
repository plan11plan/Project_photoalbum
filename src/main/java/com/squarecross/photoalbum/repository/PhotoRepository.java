package com.squarecross.photoalbum.repository;

import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    /**
     * findBy 대신 countBy 를 입력하면 뒤에 붙는 조건을 기준으로 테이블에 레코드가 몇개 있는지 확인합니다.
     * countBy 뒤에 붙는 Album_AlbumId 는 참조하는 Album 엔티티의 AlbumId 값을 확인해 일치하는 Photo 레코드들을 찾습니다.
     * 즉 말로하면 특정 앨범에 속해있는 사진의 개수를 가져옵니다.
     */
    int countByAlbum_AlbumId(Long AlbumId);
    List<Photo> findTop4ByAlbum_AlbumIdOrderByUploadedAtDesc(Long AlbumId);
}