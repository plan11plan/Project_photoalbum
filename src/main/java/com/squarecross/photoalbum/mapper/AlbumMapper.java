package com.squarecross.photoalbum.mapper;

import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.dto.AlbumDto;

public class AlbumMapper {
    /**
     * Mapper는 온전히 값을 매핑해주는 용도이기 때문에 객체를 만들 필요가 없으니 static 메서드로 만듭니다.
     */
    public static AlbumDto convertToDto (Album album) {
        AlbumDto albumDto = new AlbumDto(album.getId(), album.getAlbumName(),album.getCreated_at());
        return albumDto;
    }
}
