package com.squarecross.photoalbum.file;

import com.squarecross.photoalbum.domain.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {
    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    //여러장 업로드
    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if(!multipartFile.isEmpty()){
                UploadFile uploadFile = storeFile(multipartFile);
                storeFileResult.add(uploadFile);
            }
        }
        return storeFileResult;
    }



    //파일 저장을 해볼게요.
    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        // multipartFile을 통해 나한테 진짜 파일을 저장한 다음에 업로드 파일로 바꿔줌.
        if (multipartFile.isEmpty()) {
            return null;
        }
        //image.png
        String originalFilename = multipartFile.getOriginalFilename();
        //서버에 저장하는 파일명
        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));
        return new UploadFile(originalFilename,storeFileName);
    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);        //확장자 .png 추출
        String uuid = UUID.randomUUID().toString();   // "qwe-qwe-123-qwe-qw"
        return  uuid + "." + ext;
    }


    //.png 확장자만 추출하기
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf("."); // .의 인덱스
        String ext = originalFilename.substring(pos + 1);
        return ext;
    }

}
