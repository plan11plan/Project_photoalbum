package com.squarecross.photoalbum.controller;


import com.squarecross.photoalbum.dto.PhotoDto;
import com.squarecross.photoalbum.service.AlbumService;
import com.squarecross.photoalbum.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/albums/{albumId}/photos")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;
    private final AlbumService albumService;

    /**
     * 사진 상세정보 API
     */
    @GetMapping("/{photoId}")
    public ResponseEntity<PhotoDto> getPhotoInfo(@PathVariable("albumId") final long albumId,
                                                 @PathVariable("photoId") final long photoId) {
        PhotoDto photoDto = photoService.getPhoto(albumId, photoId);
        return new ResponseEntity<>(photoDto, HttpStatus.OK);
    }

    /**
     * 사진 업로드 API
     */
    @PostMapping("")
    public ResponseEntity<List<PhotoDto>> uploadPhotos(
            @PathVariable("albumId") final Long albumId,
            @RequestParam("photos") MultipartFile[] files) throws IOException {
        List<PhotoDto> photos = new ArrayList<>();
        for (MultipartFile file : files) {
            PhotoDto photoDto = photoService.savePhoto(file, albumId);
            photos.add(photoDto);
        }
        return new ResponseEntity<>(photos, HttpStatus.OK);
    }
    /**
     * 사진 다운로드
     */
    @GetMapping("/donwload")
    public void downloadPhotos( //출력은 없습니다 void
           @RequestParam("photoIds") Long[] photoIds, //쿼리 파라미터로 다운받을 사진 Id를 받습니다.
           HttpServletResponse response){ //HttpServletResponse는 Client에서 API호출 시 생성되는 Response Servlet 이다.
        //평상시에는 ResponseEntity를 사용하지만 파일을 전달하는 통로를 열려면 ResponseServlet을 직접 사용하여 파일을 전달하는 것이 용이
        try{
            if(photoIds.length==1){
                File file = photoService.getImageFile(photoIds[0]);
                OutputStream outputStream = response.getOutputStream();
                IOUtils.copy(new FileInputStream(file),outputStream);
                outputStream.close();

            }
        } catch (FileNotFoundException e){
            throw new RuntimeException("Error");
        }catch (IOException e){
            throw new RuntimeException(e);
        }

    }


}
