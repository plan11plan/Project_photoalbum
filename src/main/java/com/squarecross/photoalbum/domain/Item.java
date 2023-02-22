package com.squarecross.photoalbum.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Item {
    @Id @GeneratedValue
    @Column(name="item_id")
    private Long id;
    private String itemName;
    @OneToOne(fetch = FetchType.LAZY,mappedBy = "item2",cascade = CascadeType.ALL)
    private UploadFile attachFile;

    @OneToMany(mappedBy = "item",cascade = CascadeType.ALL)
    private List<UploadFile> imageFiles = new ArrayList<>();//이미지는 여러개 업로드 가능

}
