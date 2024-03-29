package com.squarecross.photoalbum.controller;


import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.MovePhotoDto;
import com.squarecross.photoalbum.dto.PhotoDto;
import com.squarecross.photoalbum.service.AlbumService;
import com.squarecross.photoalbum.service.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.hibernate.engine.jdbc.StreamUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/albums/{albumId}/photos")
@RequiredArgsConstructor
@Slf4j
public class PhotoController {

    private final PhotoService photoService;

    /**
     * 사진 상세정보 API
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{photoId}")
    public PhotoDto getPhotoInfo(@PathVariable final long albumId,
                                 @PathVariable final long photoId) {
        PhotoDto photoDto = photoService.getPhoto(albumId, photoId);
        return photoDto;
    }

    /**
     * 사진 업로드 API . 바이너리 코드 필터링까지 필요
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("")
    public List<PhotoDto> uploadPhotos(
            @PathVariable final long albumId,
            @RequestParam("photos") MultipartFile[] files, HttpServletRequest request) throws IOException {
        //HttpServletRequest- > MultipartHttpServletRequest
        //그런데 이후에
        log.info("request={}", request);
        log.info("albumId 출력={}", albumId);
        log.info("multipartFile[] 리스트 길이={}",files.length);

        List<PhotoDto> photos = new ArrayList<>();

        if(files.length!=0){
            for (MultipartFile file : files) {

                PhotoDto photoDto = photoService.savePhoto(file, albumId);
                photos.add(photoDto);
                log.info("컨트롤) 저장한 Album id ={}",photoDto.getAlbumId());
                log.info("컨트롤)저장한 photo id ={}",photoDto.getPhotoId());
                log.info("컨트롤)저장한 photo 이름 ={}",photoDto.getFileName());
            }
        }
        log.info("사진 리턴합니다.");
        return photos;
    }

    /**
     * 사진 다운로드
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/download")
    public void downloadPhotos( //출력은 없습니다 void
                                @RequestParam Long[] photoIds, //쿼리 파라미터로 다운받을 사진 Id를 받습니다.
                                HttpServletResponse response
    ) { //HttpServletResponse는 Client에서 API호출 시 생성되는 Response Servlet 이다.
        //평상시에는 ResponseEntity를 사용하지만 파일을 전달하는 통로를 열려면 ResponseServlet을 직접 사용하여 파일을 전달하는 것이 용이
        try {
            if (photoIds.length == 1) {
                File file = photoService.getImageFile(photoIds[0]);
                OutputStream outputStream = response.getOutputStream();
                IOUtils.copy(new FileInputStream(file), outputStream);
                outputStream.close();

            } else {
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/zip");
                response.addHeader("Content-Disposition", "attachment; filename=\"allToOne.zip\"");

                FileOutputStream fos = null;
                ZipOutputStream zipOut = null;
                FileInputStream fis = null;
                try {
                    zipOut = new ZipOutputStream(response.getOutputStream());
                    // File 객체를 생성하여 List에 담는다.
                    List<File> fileList = photoService.getImageFiles(photoIds);

                    // 루프를 돌며 ZipOutputStream에 파일들을 계속 주입해준다.
                    for (File file : fileList) {
                        zipOut.putNextEntry(new ZipEntry(file.getName()));
                        fis = new FileInputStream(file);

                        StreamUtils.copy(fis, zipOut);

                        fis.close();
                        zipOut.closeEntry();
                    }
                    //
                    zipOut.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    try {
                        if (fis != null) fis.close();
                    } catch (IOException e1) {
                        System.out.println(e1.getMessage());/*ignore*/
                    }
                    try {
                        if (zipOut != null) zipOut.closeEntry();
                    } catch (IOException e2) {
                        System.out.println(e2.getMessage());/*ignore*/
                    }
                    try {
                        if (zipOut != null) zipOut.close();
                    } catch (IOException e3) {
                        System.out.println(e3.getMessage());/*ignore*/
                    }
                    try {
                        if (fos != null) fos.close();
                    } catch (IOException e4) {
                        System.out.println(e4.getMessage());/*ignore*/
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Error");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 사진 목록 불러오기
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("")
    public List<PhotoDto> getList(
            @PathVariable final long albumId,
            @RequestParam(required = false, defaultValue = "") final String keyword, //사진에 들어가는 글자
            @RequestParam(required = false, defaultValue = "byDate") final String sort,
            @RequestParam(required = false, defaultValue = "") final String orderBy) {
        List<PhotoDto> photoDtos = photoService.getPhotoList(albumId, keyword, sort, orderBy);
        return photoDtos;
    }

    /**
     * 사진 삭제하기  ..@RequestBody ? -> Body, @RequestParam? -> Param . 포스트맨 참고
     * //@RequestBody는 생략 불가. 생략해버리면 ModelAttribute로 여김.
     * <p>
     * 스프링은 @ModelAttribute,@RequestParam 해당 생략시 다음과 같은 규칙을 적용한다.
     * String, int ,Integer 같은 단순 타입 = @Requestparam
     * 나머지 = @ModelAttribute
     */
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("")
    public List<PhotoDto> deleteAlbum(
            @PathVariable("albumId") final Long albumId,
            @RequestParam final List<Long> photoIds) {
        List<PhotoDto> photoDtos = photoService.deleteAndGetPhotoList(photoIds, albumId);
        return photoDtos;
    }

    /**
     * 사진 옮기기
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/move")
    public List<PhotoDto> movePhotos(
            @PathVariable("albumId") final Long albumId,
            @RequestBody MovePhotoDto movePhotoDto)
//            @RequestParam("fromAlbumId") final Long fromAlbumId,
//            @RequestParam("toAlbumId") final Long toAlbumId,
//            @RequestParam("photoIds") final List<Long> photoIds)
    {

        List<PhotoDto> photoDtos = photoService.movePhoto(
                movePhotoDto.getFromAlbumId(), movePhotoDto.getToAlbumId(),movePhotoDto.getPhotoIds());
        return photoDtos;

    }

}
