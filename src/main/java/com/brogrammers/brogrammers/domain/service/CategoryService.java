package com.brogrammers.brogrammers.domain.service;

import com.brogrammers.brogrammers.domain.product.Category;
import com.brogrammers.brogrammers.domain.repository.CategoryRepository;
import com.brogrammers.brogrammers.web.products.CategoryForm;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public void save(Category category){
        categoryRepository.save(category);
    }
    public Optional<Category> findOneByName(String name){ // 상품 등록할 때 사용하는 메서드,
        return categoryRepository.findByName(name);
    }
    public List<Category> findAll(){
        return categoryRepository.findAll();
    }
    public Page<Category> findAll(Pageable pageable){return categoryRepository.findAll(pageable);}
    public Long duplChk(String email){ // 카테고리가 이미 존재하는지 중복하는 메서드, 이름으로 확인
        Long id = null;
        if(categoryRepository.findByName(email).isPresent()){
            id = categoryRepository.findByName(email).get().getId();
        };
        return id;
    }
    public CategoryForm getCategoryFormById(Long id){
        Category category = categoryRepository.findById(id).get();
        return CategoryForm.builder().name(category.getName()).id(category.getId()).build();
    }
    public void updatCatoegoryByForm(CategoryForm form){
        Category category = categoryRepository.findById(form.getId()).get();
        category.setName(form.getName());
    }

    public Optional<Category> findById(Long id){
        return categoryRepository.findById(id);
    }

    public Page<Category> findCategoryByName(String keyName, Pageable pageable){
        return categoryRepository.findCategoryByName(keyName,pageable);
    }
}
