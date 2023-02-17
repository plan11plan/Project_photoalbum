package com.squarecross.photoalbum.controller;

import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.dto.AlbumDto;
import com.squarecross.photoalbum.service.AlbumService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/albums")
@RequiredArgsConstructor
@Slf4j
public class AlbumController {
    private final AlbumService albumService;

    @GetMapping("/{albumId}")
    public ResponseEntity<AlbumDto> getAlbum(@PathVariable("albumId") final long albumId) {
        AlbumDto album = albumService.getAlbum(albumId);
        return new ResponseEntity<>(album, HttpStatus.OK);
    }


    /**
     * albumName 단 하나이지만, AlbumDto에 정의되어있는, 이미 앨범에 속해있는 필드입니다.
     * 따로 albumName만 받는 클래스를 별도로 정의할 수 있지만 AlbumDto를 클래스를 재사용해도 되서,
     * 클래스 하나를 더 정의하지 않고, 코드를 늘리지 않기로 선택 했습니다.
     * 이부분은 개개인마다 다르게 할 수 있지만 중요한건 고민을 충분히 하고 스스로 판단하기에 최선의 선택을 하는거입니다.
     */

    /**
     * 앨범생성
     */
    @PostMapping
    public ResponseEntity<?> createAlbum(@Valid @RequestBody final AlbumDto albumDto, BindingResult bindingResult)
            throws IOException {
        if(bindingResult.hasErrors()){
            log.info("errors={}", bindingResult);
            return new ResponseEntity<>((Map<BindingResult, String>) bindingResult,HttpStatus.BAD_REQUEST);

        }
        AlbumDto savedAlbumDto = albumService.createAlbum(albumDto);
        return new ResponseEntity<>(savedAlbumDto, HttpStatus.OK);
    }

    /**
     * 앨범 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<AlbumDto>> getAlbumList(
            @RequestParam(required = false, defaultValue = "") final String keyword, //앨범에 들어가는 글자
            @RequestParam(required = false, defaultValue = "byDate") final String sort,
            @RequestParam(required = false, defaultValue = "") final String orderBy) {
        List<AlbumDto> albumDtos = albumService.getAlbumList(keyword, sort, orderBy);
        return new ResponseEntity<>(albumDtos, HttpStatus.OK);
    }

    /**
     * 앨범명 수정
     */
    @PutMapping("/{albumId}")
    public ResponseEntity<AlbumDto> updateAlbum(@PathVariable("albumId") final long albumId,
                                                @Valid @RequestBody final AlbumDto albumDto){
        AlbumDto result = albumService.changeName(albumId, albumDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    /**
     * 앨범 삭제
     */
    @DeleteMapping("/{albumId}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable("albumId") final long albumId){
        albumService.deleteAlbum(albumId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}