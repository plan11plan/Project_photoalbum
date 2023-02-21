package com.squarecross.photoalbum.domain;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

@Data
public class Item {
    @Id @GeneratedValue
    private Long id;
    private String itemName;
    private UploadFile attachFile;
    private List<UploadFile> imageFiles;//이미지는 여러개 업로드 가능

}
