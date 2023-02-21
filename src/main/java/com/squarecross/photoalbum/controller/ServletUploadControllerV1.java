package com.squarecross.photoalbum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Collection;

@Slf4j
@Controller
@RequestMapping("/servlet/v1")
public class ServletUploadControllerV1 {

    @GetMapping("upload")
    public String newFile(){
        return "upload-form";
    }
    @PostMapping("/upload")
    public String saveFileV1(HttpServletRequest request) throws ServletException, IOException {
        log.info("request = {}",request);
        String itemName = request.getParameter("itemName");
        log.info("itemNAme= {} ",itemName);
        //여기서 중요합니다.
        Collection<Part> parts = request.getParts(); //part가 ---xxx 파트마다 받을 수 있음.
        log.info("parts={}",parts);
        return "upload-form";
    }
}
