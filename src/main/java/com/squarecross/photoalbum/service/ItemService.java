package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.domain.Item;
import com.squarecross.photoalbum.domain.UploadFile;
import com.squarecross.photoalbum.dto.ItemForm;
import com.squarecross.photoalbum.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    public Long putImagesInDB(ItemForm form, UploadFile attachFile, List<UploadFile> storeImageFiles){
        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setAttachFile(attachFile);
        item.setImageFiles(storeImageFiles);
        itemRepository.save(item);
        return item.getId();
    }
}
