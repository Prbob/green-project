package com.brogrammers.brogrammers.domain.service;

import com.brogrammers.brogrammers.domain.product.Imgs;
import com.brogrammers.brogrammers.domain.product.Products;
import com.brogrammers.brogrammers.domain.repository.ImgsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImgService {
    private final ImgsRepository imgsRepository;

    @Transactional
    public void save(Imgs imgs){
        imgsRepository.save(imgs);
    }

    public List<Imgs> findImgsByProducts(Products products){ return imgsRepository.findImgsByProducts(products);}
    @Transactional
    public void deleteImgs(Long id){imgsRepository.deleteById(id);}
}
