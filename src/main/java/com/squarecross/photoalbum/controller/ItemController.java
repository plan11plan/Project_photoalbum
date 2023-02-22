package com.squarecross.photoalbum.controller;


import com.squarecross.photoalbum.domain.Item;
import com.squarecross.photoalbum.domain.UploadFile;
import com.squarecross.photoalbum.dto.ItemForm;
import com.squarecross.photoalbum.file.FileStore;
import com.squarecross.photoalbum.repository.ItemRepository;
import com.squarecross.photoalbum.service.ItemService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final FileStore fileStore;

    @GetMapping("/items/new")
    public String newItem(@ModelAttribute ItemForm form){
        return "item-form";
    }
    @PostMapping("/items/new")
    public String saveItem(@ModelAttribute ItemForm form, RedirectAttributes redirectAttributes) throws IOException {
        //진짜 파일에 저장이 됩니다.
        UploadFile attachFile = fileStore.storeFile(form.getAttachFile());
        List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());

        //데이터베이스에 저장
        Long getId = itemService.putImagesInDB(form, attachFile, storeImageFiles);

        redirectAttributes.addAttribute("itemId",getId);
        return "redirect:/items/{itemId}";
    }
}
