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
import static org.springframework.test.util.AssertionErrors.assertEquals;

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

    }@Test
    void testAlbumRepository() throws InterruptedException {
        Album album1 = new Album();
        Album album2 = new Album();
        album1.setAlbumName("aaaa");
        album2.setAlbumName("aaab");

        albumRepository.save(album1);
        TimeUnit.SECONDS.sleep(1); //시간차를 벌리기위해 두번째 앨범 생성 1초 딜레이
        albumRepository.save(album2);

        //최신순 정렬, 두번째로 생성한 앨범이 먼저 나와야합니다
        List<Album> resDate = albumRepository.findByAlbumNameContainingOrderByCreatedAtDesc("aaa");
        assertThat("aaab").isEqualTo( resDate.get(0).getAlbumName()); // 0번째 Index가 두번째 앨범명 aaab 인지 체크
        assertThat("aaaa").isEqualTo( resDate.get(1).getAlbumName()); // 1번째 Index가 첫번째 앨범명 aaaa 인지 체크
        assertThat(2).isEqualTo(resDate.size()); // aaa 이름을 가진 다른 앨범이 없다는 가정하에, 검색 키워드에 해당하는 앨범 필터링 체크

        //앨범명 정렬, aaaa -> aaab 기준으로 나와야합니다
        List<Album> resName = albumRepository.findByAlbumNameContainingOrderByAlbumNameAsc("aaa");
        assertThat("aaaa").isEqualTo( resName.get(0).getAlbumName()); // 0번째 Index가 두번째 앨범명 aaaa 인지 체크
        assertThat("aaab").isEqualTo( resName.get(1).getAlbumName()); // 1번째 Index가 두번째 앨범명 aaab 인지 체크
        assertThat(2).isEqualTo(resName.size()); // aaa 이름을 가진 다른 앨범이 없다는 가정하에, 검색 키워드에 해당하는 앨범 필터링 체크
    }
}