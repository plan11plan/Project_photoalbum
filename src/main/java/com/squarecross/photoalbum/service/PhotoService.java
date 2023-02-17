package com.squarecross.photoalbum.service;


import com.squarecross.photoalbum.Constants;
import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.PhotoDto;
import com.squarecross.photoalbum.mapper.PhotoMapper;
import com.squarecross.photoalbum.repository.AlbumRepository;
import com.squarecross.photoalbum.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.persistence.EntityNotFoundException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static com.squarecross.photoalbum.FileExtFilter.badFileExtIsReturnException;

@Service
@Transactional
@RequiredArgsConstructor
public class PhotoService {
    private final AlbumRepository albumRepository;
    private final PhotoRepository photoRepository;

    private final String original_path = Constants.PATH_PREFIX + "/photos/original";
    private final String thumb_path = Constants.PATH_PREFIX + "/photos/thumb";


    /**
     * 사진 조회
     */
    public PhotoDto getPhoto(Long albumId, Long photoId) {
        Optional<Album> findAlbum = albumRepository.findById(albumId);
        System.out.println("입력받은 앨범아이디=" + albumId);
        if (!findAlbum.isPresent()) {
            throw new EntityNotFoundException(String.format("앨범 아이디 %d로 조회되지 않았습니다", albumId));
        }
        Album album = findAlbum.get();
        System.out.println("찾은 앨범:" + album.getAlbumId());
        List<Photo> photos = album.getPhotos();
        //
        //
        PhotoDto findPhotoDto = new PhotoDto();
        for (Photo photo : photos) {
            Long getPhotoId = photo.getPhotoId();
            System.out.println("현재 찾은 포토Id=" + getPhotoId);
            if (getPhotoId == photoId) {
                PhotoDto photoDto = PhotoMapper.convertToDto(photo);
                findPhotoDto = photoDto;
                break;
            }
        }

        return findPhotoDto;
    }

    /**
     * 사진 업로드
     */
    public PhotoDto savePhoto(MultipartFile file, Long albumId) throws IOException {
        /** 이미지 검증 로직 */

        badFileExtIsReturnException(file);

        /**앨범아이디 체크 및 파일 기본정보 추출*/
        Optional<Album> result = albumRepository.findById(albumId);
        if (result.isEmpty()) {
            throw new EntityNotFoundException("앨범이 존재하지 않습니다");
        }
        String fileName = file.getOriginalFilename();
        int fileSize = (int) file.getSize(); //long은 64바이트 int는 32바이트다. int로 나타낼 수 있는 최대는 대략 2GB인데
        //그렇게 커질 일 없으니 내맘대로 int로 변환
        /** 파일명 존재 유무 체크
         * 1.같은 파일명이 존재하는지 체크하는 Repository 메서드 추가
         * 2.입력된 앨범 안에 같은 파일명이 있는지 확인.
         * 3.다른 앨범에는 같은 파일명 있어도 됌.
         * */
        //DB에 입력된 앨범안에 같은 파일명이 있는지 체크.
        fileName = getNextFileName(fileName, albumId);
        //다시 메인 메서드로 돌아와서 saveFile 메서드를 추가해서 사진을 저장합니다
        saveFile(file, albumId, fileName);
        // DB에 사진 레코드 생성 & 생성된 앨범 DTO 반환
        Photo photo = new Photo();
        photo.setOriginalUrl("/photos/original/" + albumId + "/" + fileName);
        photo.setThumbUrl("/photos/thumb/" + albumId + "/" + fileName);
        photo.setFileName(fileName);
        photo.setFileSize(fileSize);
        photo.setAlbum(result.get());
        Photo createdPhoto = photoRepository.save(photo);
        return PhotoMapper.convertToDto(createdPhoto);

    }


    //DB에 입력된 앨범안에 같은 파일명이 있는지 체크.
    private String getNextFileName(String fileName, Long albumId) {
        /**
         * static String	getFilenameExtension(String path)
         * 주어진 path로 부터 파일 확장자를 추출한다.
         */
        String fileNameNoExt = StringUtils.stripFilenameExtension(fileName); // 확장자 없는 파일명
        String ext = StringUtils.getFilenameExtension(fileName); // 확장자
        Optional<Photo> result = photoRepository.findByFileNameAndAlbum_AlbumId(fileName, albumId);
        /**
         * 파일명 조회해서 체크 → 파일명 숫자 추가 및 변경 → 없을 때까지 반복
         */
        int count = 2;
        while (result.isPresent()) {
            //String 의 static 메서드인 format 메서드는 문자열의 형식을 설정하는 메서드입니다.
            //String.format 메서드를 통해서 확장자 없는 파일명,숫자,확장자를 합쳐줍니다.
            fileName = String.format("%s (%d).%s", fileNameNoExt, count, ext);

            //새로운 파일명으로 Repository에서 DB 조회해서 확인
            result = photoRepository.findByFileNameAndAlbum_AlbumId(fileName, albumId);
            // 다음 들어올경우 사용도도록 Count 숫자를 늘려줌
            count++;
        }
        //While문이 끝나면 파일명 반환
        return fileName;
    }

    //이미지 저장하는 기능도 복잡하니 메서드를 별도로 생성하겠습니다
    private void saveFile(MultipartFile file, Long AlbumId, String fileName) throws IOException {
        try {
            // 원본 이미지를 original 사진 경로에 저장합니다
            String filePath = AlbumId + "/" + fileName;
            Files.copy(file.getInputStream(), Paths.get(original_path + "/" + filePath));

            //정사각형이 아닌 경우 가장 긴 면은 300으로 줄이고 다른 면은 비례해서 Resize 됩니다(라이브러리 사용)
            BufferedImage thumbImg = Scalr.resize(
                    ImageIO.read(file.getInputStream()),
                    Constants.THUMB_SIZE, Constants.THUMB_SIZE);

            //Resize된 썸네일 이미지를 저장합니다.
            File thumbFile = new File(thumb_path + "/" + filePath);
            String ext = StringUtils.getFilenameExtension(fileName);
            if (ext == null) {
                throw new IllegalArgumentException("No Extention");
            }
            ImageIO.write(thumbImg, ext, thumbFile);
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }
    /**
     * 사진 다운로드
     * 서비스 메서드는 개별 이미지 아이디를 받아 이미지 파일을 반환합니다.
     * 서비스에서 파일을 집파일로 묶어서 Client에 전달하는건 Controller에서 진행합니다.
     */
    /**
     * 의사 코드
     * 1. 입력된 이미지 아이디로 DB를 조회합니다.
     * a. 없으면 Exception을 던집니다.
     * 2. 프로젝트 경로를 앞에 붙여서 디렉토리 내에서 사진을 불러옵니다.
     * 3. 파일을 Controller로 출력합니다.
     */
    public File getImageFile(Long photoId) {
        Optional<Photo> result = photoRepository.findById(photoId);
        if (result.isEmpty()) {
            throw new EntityNotFoundException(String.format("사진을 ID %d를 찾을 수 없습니다."));
        }
        return new File(Constants.PATH_PREFIX + result.get().getOriginalUrl());
    }

    public List<File> getImageFiles(Long[] photoIds) {
        List<Photo> result = photoRepository.findByPhotoId(photoIds);
        // DB에 저장되어 있는 파일 목록을 읽어온다.
        for (Photo photo : result) {
            if (photo == null) {
                throw new EntityNotFoundException(String.format("사진을 ID %d를 찾을 수 없습니다."));
            }
        }
        // File 객체를 생성하여 List에 담는다.
        List<File> fileList = result.stream().map(fileInfo -> {
            return new File(Constants.PATH_PREFIX + fileInfo.getOriginalUrl());
        }).collect(Collectors.toList());
        return fileList;

    }

    /**
     * 사진 목록 불러오기
     */
    public List<PhotoDto> getPhotoList(Long albumId, String keyword, String sort, String orderBy) {
        Optional<Album> album = albumRepository.findById(albumId);
        if (album.isEmpty()) {
            throw new NoSuchElementException(String.format("Album ID '%d'가 존재하지 않습니다", albumId));
        }
        List<Photo> photos ;
        if (Objects.equals(sort, "byName")) {
            if (Objects.equals(orderBy, "asc")) {
                photos = photoRepository.fasc(albumId, keyword);
            } else {
                photos = photoRepository.fdesc(albumId, keyword);
            }
        } else if (Objects.equals(sort, "byDate")) {
            if (Objects.equals(orderBy, "desc")) {
                photos = photoRepository.tdesc(albumId, keyword);
            } else  {
                photos = photoRepository.tasc(albumId, keyword);
            }
        } else {
            throw new IllegalArgumentException("알 수 없는 정렬 기준입니다");
        }
        return PhotoMapper.convertToDtoList(photos);

    }

//
}
