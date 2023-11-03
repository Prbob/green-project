package com.brogrammers.brogrammers.domain.service;

import com.brogrammers.brogrammers.domain.product.Brand;
import com.brogrammers.brogrammers.domain.repository.BrandRepository;
import com.brogrammers.brogrammers.web.products.BrandForm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BrandService {
    @Autowired BrandRepository brandRepository;

    public void save(String name){
        Brand brand = Brand.builder().name(name).build();
        brandRepository.save(brand);
    }
    public Page<Brand> findAll(Pageable pageable){
        return brandRepository.findAll(pageable);
    }
    public List<Brand> findAll(){
        return brandRepository.findAll();
    }
    public Optional<Brand> findOneByName(String name){
        return brandRepository.findOneByName(name);
    }
    public void updatBrandByForm(BrandForm form){
        Brand brand = brandRepository.findOneById(form.getId());
        brand.setName(form.getName());
    }
    public BrandForm getBrandFormById(Long id){
        Brand brand = brandRepository.findOneById(id);
        return BrandForm.builder().name(brand.getName()).id(brand.getId()).build();
    }
    public Long duplChk(String name){
        Optional<Brand> brand = brandRepository.findOneByName(name);
        Long id = null;
        if(brand.isPresent()){
            id = brand.get().getId();
        }
        return id;
    }

    public Optional<Brand> findById(Long id){
        return brandRepository.findById(id);
    }

    public Page<Brand> findBrandsByName(String name, Pageable pageable){
        return brandRepository.findBrandsByName(name,pageable);
    }
}
