package com.squarecross.photoalbum.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id", unique = true, nullable = false)
    private Long photoId;
    @Column(name = "file_name", unique = false, nullable = true)
    private String file_name;

    @Column(name = "thumb_url", unique = false, nullable = true)
    private String thumb_url;
    @Column(name = "original_url", unique = false, nullable = true)
    private String original_url;
    @Column(name = "file_size", unique = false, nullable = true)
    private long file_size;
    @Column(name="uploaded_at", unique = false, nullable = true)
    @CreationTimestamp
    private LocalDateTime uploaded_at;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="album_id")
    private Album album;
}
