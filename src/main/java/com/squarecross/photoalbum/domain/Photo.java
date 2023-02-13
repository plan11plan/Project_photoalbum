package com.squarecross.photoalbum.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Photo {
    @Id
    @GeneratedValue
    @Column(name="photo_id")
    private Long id;

    private String file_name;
    private String thumb_url;
    private String original_url;
    private long file_size;
    private LocalDateTime uploaded_at;

    private Long album_id;
}
