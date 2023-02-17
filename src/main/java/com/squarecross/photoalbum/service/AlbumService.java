package com.squarecross.photoalbum.service;


import com.squarecross.photoalbum.Constants;
import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.AlbumDto;
import com.squarecross.photoalbum.dto.PhotoDto;
import com.squarecross.photoalbum.mapper.AlbumMapper;
import com.squarecross.photoalbum.repository.AlbumRepository;
import com.squarecross.photoalbum.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final PhotoRepository photoRepository;

    /**
     * AlbumRepository에서 Album ID로 조회했을 때 찾지 못해서 반환이 되지 않는 경우를 대비해서 Optional<Album> 리턴값을 갖습니다.
     */
    public AlbumDto getAlbum(Long albumId) {
        Optional<Album> findAlbum = albumRepository.findById(albumId);
        if (findAlbum.isPresent()) {
            AlbumDto albumDto = AlbumMapper.convertToDto(findAlbum.get());
            albumDto.setCount(photoRepository.countByAlbum_AlbumId(albumId));
            return albumDto;
        } else {
            throw new EntityNotFoundException(String.format("앨범 아이디 %d로 조회되지 않았습니다", albumId));
        }
    }
    public Album findDomainAlbumById(Long albumId){
        Optional<Album> findAlbum = albumRepository.findById(albumId);
        if(findAlbum.isEmpty()){
            throw new EntityNotFoundException(String.format("앨범 아이디 %d로 조회되지 않았습니다", albumId));
        }
        Album album = findAlbum.get();
        return album;
    }

    public AlbumDto findByAlbumName(String albumName) {
        Optional<Album> findAlbum = albumRepository.findByAlbumName(albumName);
        if (findAlbum.isPresent()) {
            AlbumDto albumDto = AlbumMapper.convertToDto(findAlbum.get());
            return albumDto;
        } else {
            throw new EntityNotFoundException(String.format("앨범 이름 %d로 조회되지 않았습니다", albumName));
        }
    }

    public AlbumDto createAlbum(AlbumDto albumDto) throws IOException {
        Album album = AlbumMapper.convertToModel(albumDto);
        this.albumRepository.save(album);
        this.createAlbumDirectories(album);
        return AlbumMapper.convertToDto(album);

    }
    private void createAlbumDirectories(Album album) throws IOException {
        Files.createDirectories(Paths.get(Constants.PATH_PREFIX + "/photos/original/" + album.getAlbumId()));
        Files.createDirectories(Paths.get(Constants.PATH_PREFIX + "/photos/thumb/" + album.getAlbumId()));
    }

    /**
     * List의 `stream()` 메서드는 굉장히 유용하게 사용되는 메서드인데요,
     * 리스트안에 있는 것들을 스트리밍하듯이 하나씩 하나씩 흘려보내듯이 처리합니다.
     *
     * albums에 있는 각 앨범을 하나씩 하나씩 `AlbumMapper.converToDto`로 변화시킨 이후 리스트형태로 다시 모읍니다
     * `collect(Collectors.toList())`.
     */
    public List<AlbumDto> getAlbumList(String keyword, String sort, String orderBy) {
        List<Album> albums = new ArrayList<>();
        if (Objects.equals(sort, "byName")){
            if(Objects.equals(orderBy, "asc")) {
                albums = albumRepository.findByAlbumNameContainingOrderByAlbumNameAsc(keyword);
            }
            else if(Objects.equals(orderBy,"desc")) {
                albums = albumRepository.findByAlbumNameContainingOrderByAlbumNameDesc(keyword);
            }
        }
        else if (Objects.equals(sort, "byDate")) {
            if(Objects.equals(orderBy,"desc")) {
                albums = albumRepository.findByAlbumNameContainingOrderByCreatedAtDesc(keyword);
            }
            else if(Objects.equals(orderBy,"asc")){
                albums = albumRepository.findByAlbumNameContainingOrderByCreatedAtAsc(keyword);
            }
        } else {
            throw new IllegalArgumentException("알 수 없는 정렬 기준입니다");
        }
        //
        List<AlbumDto> albumDtos = AlbumMapper.convertToDtoList(albums);
        //
        /**
         * -`map(Photo::getThumbUrl)` ****썸네일 URL 추출.
         * -`map(c -> Constants.PATH_PREFIX + c)` 프로젝트 폴더 디렉토리까지 합쳐서 Full 이미지 Path 로 만들기.
         * - map안에 있는 내용은 람다 함수인데, c는 stream에서 넘어오는 각 string 이고 → 우측에 있는 구현내용은 c를 사용해서 실행할 내용입니다.
         * -`collect(Collectors.toList())` 하나씩 수정을 거쳐서 들어오는 String을 List로 모읍니다.
         */
        for(AlbumDto albumDto : albumDtos){
            List<Photo> top4 = photoRepository.findTop4ByAlbum_AlbumIdOrderByUploadedAtDesc(albumDto.getAlbumId());
            albumDto.setThumbUrls(top4.stream().map(Photo::getThumbUrl).map(c -> Constants.PATH_PREFIX + c).collect(Collectors.toList()));
        }
        return albumDtos;
    }
    /**
     * 앨범명 수정
     */
    public AlbumDto changeName(Long AlbumId,AlbumDto albumDto){
        Optional<Album> album = this.albumRepository.findById(AlbumId);
        if(album.isEmpty()){
            throw new NoSuchElementException(String.format("Album ID '%d'가 존재하지 않습니다", AlbumId));
        }
        Album updateAlbum = album.get();
        updateAlbum.setAlbumName(albumDto.getAlbumName());
        Album savedAlbum = albumRepository.save(updateAlbum);/////////
        return AlbumMapper.convertToDto(savedAlbum);
    }

    /**
     * 앨범삭제
     */
    public void deleteAlbum(Long AlbumId){
        Optional<Album> album = albumRepository.findById(AlbumId);
        if(album.isEmpty()){
            throw new NoSuchElementException(String.format("Album ID '%d'가 존재하지 않습니다.",AlbumId));
        }
        Album findAlbum = album.get();
        albumRepository.delete(findAlbum);
    }



}
