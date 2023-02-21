package com.squarecross.photoalbum.domain;

import lombok.Data;

@Data
public class UploadFile {
    private String uploadFileName;
    private String storeFileName; //시스템에 저장한 파일 이름
    //사용자가 2명 이상 같은 파일명 업로드 했을 경우, 나의 디스크에 같은 파일명이면 덮어지겠죠
    // uuid로 안겹치게

    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
