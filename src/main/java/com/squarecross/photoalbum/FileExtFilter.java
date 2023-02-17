package com.squarecross.photoalbum;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class FileExtFilter {
    /**
     * 파일의 확장자를 체크하여 필터링된 확장자를 포함한 파일인 경우에 예외를 발생한다.
     *
     * @param file
     */

    public static void badFileExtIsReturnException(MultipartFile file) {

        String fileName = file.getName();

        String ext = fileName.substring(fileName.lastIndexOf(".") + 1,

                fileName.length());

        final String[] BAD_EXTENSION = {"jsp", "php", "asp", "html", "perl"};


        try {

            int len = BAD_EXTENSION.length;

            for (int i = 0; i < len; i++) {

                if (ext.equalsIgnoreCase(BAD_EXTENSION[i])) {

                    // 불량 확장자가 존재할떄 IOExepction 발생

                    throw new IOException("BAD EXTENSION FILE UPLOAD");

                }

            }

        } catch (IOException e) {

            e.printStackTrace();

        }

    }


    /**
     * 파일의 확장자를 체크하여 필터링된 확장자를 포함한 파일인 경우에 true를 리턴한다.
     *
     * @param file
     */

    public static boolean badFileExtIsReturnBoolean(MultipartFile file) {

        String fileName = file.getName();

        String ext = fileName.substring(fileName.lastIndexOf(".") + 1,

                fileName.length());

        final String[] BAD_EXTENSION = {"jsp", "php", "asp", "html", "perl"};


        int len = BAD_EXTENSION.length;

        for (int i = 0; i < len; i++) {

            if (ext.equalsIgnoreCase(BAD_EXTENSION[i])) {

                return true; // 불량 확장자가 존재할때..

            }

        }

        return false;

    }
}
