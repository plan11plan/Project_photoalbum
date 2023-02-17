package com.squarecross.photoalbum.repository;

import com.squarecross.photoalbum.domain.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    Optional<Album> findByAlbumName(String albumName);
    //앨범명검색 + 생성날짜 최신순:
    List<Album> findByAlbumNameContainingOrderByCreatedAtDesc(String albumName);
    //앨범명검색 + 생성날짜 생성순:
    List<Album> findByAlbumNameContainingOrderByCreatedAtAsc(String albumName);

    //앨범명검색 + 앨범명 A-Z 정렬:
    List<Album> findByAlbumNameContainingOrderByAlbumNameAsc(String albumName);

    //앨범명검색 + 앨범명 Z-A 정렬:
    List<Album> findByAlbumNameContainingOrderByAlbumNameDesc(String albumName);

}
