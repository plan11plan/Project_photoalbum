package com.squarecross.photoalbum.domain;

import lombok.Getter;
import lombok.Setter;
import net.bytebuddy.asm.Advice;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Album {

    @Id
    @GeneratedValue
    @Column(name="album_id")
    private long id;

    private String AlbumName;

    private LocalDateTime created_at;




}
