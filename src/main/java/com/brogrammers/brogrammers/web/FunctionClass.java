package com.brogrammers.brogrammers.web;

import com.brogrammers.brogrammers.domain.member.Member;
import com.brogrammers.brogrammers.domain.product.Brand;
import com.brogrammers.brogrammers.domain.product.Imgs;
import com.brogrammers.brogrammers.domain.product.Products;
import com.brogrammers.brogrammers.domain.service.BrandService;
import com.brogrammers.brogrammers.domain.service.ImgService;
import com.brogrammers.brogrammers.domain.service.MemberService;
import com.brogrammers.brogrammers.form.DuplicatedProduct;
import com.brogrammers.brogrammers.web.products.ProductForm;
import com.brogrammers.brogrammers.web.session.SessionConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.List;

@Service
public class FunctionClass {
    @Autowired ImgService imgService;
    @Autowired BrandService brandService;
    @Autowired MemberService memberService;
    public Member getMember(HttpServletRequest request) { // 세션에서 회원 아이디 조회
        HttpSession session = request.getSession();

        Member member =null;
        if(session.getAttribute(SessionConst.LOGIN_MEMBER)!=null){
            member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        }
        return member;
    }
    public Member getMemberDb(HttpServletRequest request){
        Member member = getMember(request);
        return memberService.findById(member.getId());
    }
    public DuplicatedProduct getDuplicatedProduct(ProductForm form){
        Brand brand = brandService.findOneByName(form.getProductBrand()).get();
        return DuplicatedProduct.builder()
                .brand(brand)
                .productColor(form.getProductColor())
                .productName(form.getProductName())
                .size(form.getSize())
                .build();
    }

    public List<Imgs> saveImgs(MultipartFile[] files, Products products, List<Imgs> list) throws Exception{
        Imgs imgs = null;
        for(MultipartFile file : files){
            String saveName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String path = "C:/spring/imgs/" + saveName;
            imgs = Imgs.builder()
                    .originName(file.getOriginalFilename())
                    .saveName(saveName)
                    .products(products)
                    .path(path)
                    .size(file.getSize())
                    .build();
            imgService.save(imgs);
            file.transferTo(new File(path)); // 파일 저장
            list.add(imgs);
        }
        return list;
    }
    public Products saveImg(Products products,MultipartFile file) throws Exception{
        String saveName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String path = "C:/spring/imgs/" + saveName;
        products.saveImg(path,saveName);
        file.transferTo(new File(path));
        return products;
    }



}
