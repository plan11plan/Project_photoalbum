package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.Constants;
import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.AlbumDto;
import com.squarecross.photoalbum.mapper.AlbumMapper;
import com.squarecross.photoalbum.repository.AlbumRepository;
import com.squarecross.photoalbum.repository.PhotoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
class AlbumServiceTest {
    @Autowired
    AlbumService albumService;
    @Autowired
    AlbumRepository albumRepository;
    @Autowired
    PhotoRepository photoRepository;

    @Test
    @DisplayName("앨범 조회")
    void getAlbum() {
        //given
        Album album = new Album();
        album.setAlbumName("테스트");
        //when
        Album savedAlbum = albumRepository.save(album);
        AlbumDto findAlbum = albumService.getAlbum(savedAlbum.getAlbumId());
        //then
        assertThat(findAlbum.getAlbumName()).isEqualTo("테스트");
    }

    @Test
    @DisplayName("앨범이름으로 조회")
    void getAlbumByAlbumName() {
        Album album = new Album();
        album.setAlbumName("테스트");
        albumRepository.save(album);

        AlbumDto findAlbum = albumService.findByAlbumName("테스트");
        assertThat(findAlbum.getAlbumName()).isEqualTo("테스트");
    }
    @Test
    void testPhotoCount(){

        //given
        Album album = new Album();
        album.setAlbumName("테스트");
        Album savedAlbum = albumRepository.save(album);

        Photo photo1 = new Photo();
        photo1.setFileName("사진1");
        photo1.setAlbum(savedAlbum);
        photoRepository.save(photo1);

        Photo photo2 = new Photo();
        photo2.setFileName("사진2");
        photo2.setAlbum(savedAlbum);
        photoRepository.save(photo2);

        Photo photo3 = new Photo();
        photo3.setFileName("사진3");
        photo3.setAlbum(savedAlbum);
        photoRepository.save(photo3);
        //when
        AlbumDto findAlbum = albumService.getAlbum(savedAlbum.getAlbumId());
        //then
        System.out.println("카운트 = "+ findAlbum.getCount());
    }
    @Test
    void test1() throws IOException {
        AlbumDto albumDto = new AlbumDto();
        albumDto.setAlbumId(1L);
        albumDto.setAlbumName("앨범1");
        Album album = AlbumMapper.convertToModel(albumDto);
        albumRepository.save(album);
        System.out.println("albumId ="+album.getAlbumId());

        Path directories = Files.createDirectories(Paths.get(
                Constants.PATH_PREFIX + "/photos/original/" + album.getAlbumId()));
        Path directories1 = Files.createDirectories(Paths.get(
                Constants.PATH_PREFIX + "/photos/thumb/" + album.getAlbumId()));
        System.out.println("directories"+directories );
        System.out.println("directories1"+directories1 );
        System.out.println("앨범이름 = "+album.getAlbumName());

        AlbumDto albumDto11 = AlbumMapper.convertToDto(album);
        System.out.println(albumDto11.getAlbumName());
        albumRepository.delete(album);
    }
    @Test
    @DisplayName("앨범명 수정")
    void changeAlbumName() throws IOException {
        // 앨범 생성
        AlbumDto albumDto = new AlbumDto();
        albumDto.setAlbumName("변경전");
        AlbumDto album = albumService.createAlbum(albumDto);

        Long albumId = album.getAlbumId(); // 생성된 앨범 아이디 추출
        AlbumDto updateDto =new AlbumDto();
        updateDto.setAlbumName("변경후"); // 업데이트용 Dto 생성
        albumService.changeName(albumId, updateDto);

        AlbumDto updatedDto = albumService.getAlbum(albumId);

        //앨범명 변경되었는지 확인
        assertThat("변경후").isEqualTo(updatedDto.getAlbumName());
    }

}