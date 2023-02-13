package com.squarecross.photoalbum.service;


import com.squarecross.photoalbum.dto.AlbumDto;
import com.squarecross.photoalbum.mapper.AlbumMapper;
import com.squarecross.photoalbum.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.repository.AlbumRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final PhotoRepository photoRepository;

    /**
     AlbumRepository에서 Album ID로 조회했을 때 찾지 못해서 반환이 되지 않는 경우를 대비해서 Optional<Album> 리턴값을 갖습니다.
     */
    public AlbumDto getAlbum(Long albumId){
        Optional<Album> findAlbum = albumRepository.findById(albumId);
        if (findAlbum.isPresent()){
            AlbumDto albumDto = AlbumMapper.convertToDto(findAlbum.get());

            int count = photoRepository.countByAlbum_AlbumId(albumId);
            albumDto.setCount(count);
            return albumDto;
        } else {
            throw new EntityNotFoundException(String.format("앨범 아이디 %d로 조회되지 않았습니다", albumId));
        }
    }
//    public AlbumDto findByAlbumName(String albumName){
//        Optional<Album> findAlbum = albumRepository.findByAlbumName(albumName);
//        if (findAlbum.isPresent()){
//            AlbumDto albumDto = AlbumMapper.convertToDto(findAlbum.get());
//            return albumDto;
//        } else {
//            throw new EntityNotFoundException(String.format("앨범 이름 %d로 조회되지 않았습니다", albumName));
//        }
//    }

}
