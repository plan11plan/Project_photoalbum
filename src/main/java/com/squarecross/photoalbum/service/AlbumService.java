package com.squarecross.photoalbum.service;


import com.squarecross.photoalbum.Constants;
import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.AlbumDto;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
    public List<AlbumDto> getAlbumList(String keyword, String sort) {
        List<Album> albums;
        if (Objects.equals(sort, "byName")){
            albums = albumRepository.findByAlbumNameContainingOrderByAlbumNameAsc(keyword);
        } else if (Objects.equals(sort, "byDate")) {
            albums = albumRepository.findByAlbumNameContainingOrderByCreatedAtDesc(keyword);
        } else {
            throw new IllegalArgumentException("알 수 없는 정렬 기준입니다");
        }

        List<AlbumDto> albumDtos = AlbumMapper.convertToDtoList(albums);
        for(AlbumDto albumDto : albumDtos){
            List<Photo> top4 = photoRepository.findTop4ByAlbum_AlbumIdOrderByUploadedAtDesc(albumDto.getAlbumId());
            albumDto.setThumbUrls(top4.stream().map(Photo::getThumbUrl).map(c -> Constants.PATH_PREFIX + c).collect(Collectors.toList()));
        }
        return albumDtos;
    }


}
