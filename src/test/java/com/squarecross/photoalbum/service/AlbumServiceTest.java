package com.squarecross.photoalbum.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.repository.AlbumRepository;

import javax.persistence.Entity;
import javax.persistence.EntityManager;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
class AlbumServiceTest {
    @Autowired AlbumService albumService;
    @Autowired
    AlbumRepository albumRepository;

    @Test
    @DisplayName("앨범 조회")
    void getAlbum() {
        Album album = new Album();
        album.setAlbumName("테스트");
        Album savedAlbum = albumRepository.save(album);
        Album findAlbum = albumService.getAlbum(savedAlbum.getId());
        assertThat(findAlbum).isEqualTo(album);
        assertThat(findAlbum.getAlbumName()).isEqualTo("테스트");
    }
    @Test
    @DisplayName("앨범이름으로 조회")
    void getAlbumByAlbumName(){
        Album album = new Album();
        album.setAlbumName("테스트");
        albumRepository.save(album);

        Album findAlbum = albumService.findByAlbumName("테스트");
        assertThat(findAlbum).isEqualTo(album);
        assertThat(findAlbum.getAlbumName()).isEqualTo("테스트");
    }

}