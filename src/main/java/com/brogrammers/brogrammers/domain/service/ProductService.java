package com.brogrammers.brogrammers.domain.service;

import com.brogrammers.brogrammers.domain.member.Member;
import com.brogrammers.brogrammers.domain.order.Orders;
import com.brogrammers.brogrammers.domain.product.Brand;
import com.brogrammers.brogrammers.domain.product.Products;
import com.brogrammers.brogrammers.domain.repository.OrdersRepository;
import com.brogrammers.brogrammers.domain.repository.ProductsRepository;
import com.brogrammers.brogrammers.form.DuplicatedProduct;
import com.brogrammers.brogrammers.web.products.ProductForm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {
    private final ProductsRepository productsRepository;
    @Autowired BrandService brandService;
    @Transactional
    public Long save(Products products) throws Exception{
        productsRepository.save(products);
        return products.getId();
    }
    public Page<Products> findProductsByNameMember(String nameSearch, Member member,Pageable pageable){
        return productsRepository.findProductsByNameAndMember(nameSearch,member,pageable);
    }
    public Page<Products> findProductsByMember(Member member,Pageable pageable){
        return productsRepository.findProductsByMember(member,pageable);
    }
    public Page<Products> findAll(Pageable pageable){
        return productsRepository.findAll(pageable);
    }

    public Products getByid(Long id){return  productsRepository.findProductById(id);}

    public ProductForm getProductFormByid(Long id){ // productform 맵핑
        // 상품수정할 때 사용할 메서드
        Products products = productsRepository.findProductById(id);
        ProductForm productForm = ProductForm.builder()
                .productBrand(products.getBrand().getName())
                .productName(products.getName())
                .productColor(products.getColor())
                .stockQuantity(products.getStockQuantity())
                .size(products.getSize())
                .price(products.getSize())
                .build();
        return  productForm;
    }
    @Transactional
    public Products updateProduct(ProductForm form,Long id){
        Products products = productsRepository.findById(id).get();
        Brand brand = brandService.findOneByName(form.getProductBrand()).get();
        products.saveProduct(form.getProductName(), brand, form.getProductColor(),form.getPrice(),form.getSize(),
                form.getStockQuantity(),form.getMember(),form.getGender());
        return products;
    }
    @Transactional
    public void updateProduct(Products products){
        Products product = productsRepository.findById(products.getId()).get();
        product = products;
    }
    public Optional<Long> duplChk(DuplicatedProduct duplicatedProduct){
        Optional<Long>id = Optional.empty();
        Optional<Products> product = productsRepository.duplicationChk(
                duplicatedProduct.getProductName(),
                duplicatedProduct.getBrand(),
                duplicatedProduct.getProductColor(),
                duplicatedProduct.getSize());
        if(product.isPresent()){
            id = Optional.of(product.get().getId());
        }
        return id;
    }

    public Page<Products> productSearchList(String searchKeyword,Pageable pageable){
        return productsRepository.findByNameContaining(searchKeyword, pageable);
    } // 키워드O

    public Page<Products> productSearchListGender(String searchKeyword,String gender,Pageable pageable){
        return productsRepository.findByNameContainingAndGender(searchKeyword,gender,"every",pageable);
    } // 키워드O 성별O

    public Page<Products> soldOutList(int num,Pageable pageable){ // 매진 임박, no keyword
        return productsRepository.findByStockQuantityLessThan(num,pageable);
    }
    public Page<Products> soldOutList(int num, String searchKeyword,Pageable pageable){
        // 매진 임박,  keyword ok
        return productsRepository.findByStockQuantityAndName(num,searchKeyword,pageable);
    }

    public Page<Products> findProductsByGender(String gender, Pageable pageable){
        return productsRepository.findProductsByMemberAndGender(gender,"every" ,pageable); // 성별 입력하면 그 성별에 맞는 모든 제품들 출력
    }
    public Page<Products> findProductsByGenderAndKeyword(String gender,String keyword,Pageable pageable){
        return productsRepository.findProductsByGender(gender,keyword, pageable); // 성별 입력하면 그 성별에 맞는 모든 제품들 출력
    }

    @Transactional
    public void deleteProduct(Long id){
        productsRepository.deleteById(id);
    }

    public List<Products> findProductsToSize(String gender,String name,Brand brand, String color ){return productsRepository.findProductsToSize(gender,name,brand,color);}

    public Optional<Products> findProductByNameColorSizeBrand(String name, String color, int size,Brand brand){
        return productsRepository.findProductsByNameAndColorAndSizeAndBrand(name,color,size,brand);
    }


    // 브랜드 O 검색 키워드 O
    public Page<Products> findProductsByBrandAndKeyword(String keyword, Brand brand,Pageable pageable){
        return productsRepository.findProductsByNameAndBrand(keyword,brand,pageable);
    }

    // 브랜드 O 검색 키워드 O 성별 O
    public Page<Products> findProductsByBrandAndKeywordAndGender(String keyword, Brand brand,String gender,Pageable pageable){
        return productsRepository.findProductsByNameAndBrandAndGender(keyword,brand,gender,"every",pageable);
    }

    // 브랜드 O 검색 키워드X
    public Page<Products> findProductsByBrand(Brand brand,Pageable pageable){
        return productsRepository.findProductsByBrand(brand,pageable);
    }
    // 브랜드 O 검색 키워드X
    public Page<Products> findProductsByBrandGender(Brand brand,String gender,Pageable pageable){
        return productsRepository.findProductsByBrandAndGender(brand,gender,"every",pageable);
    }

}
