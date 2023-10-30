package com.brogrammers.brogrammers.web.products;

import com.brogrammers.brogrammers.domain.product.Category;
import com.brogrammers.brogrammers.domain.product.Products;
import com.brogrammers.brogrammers.domain.service.CategoryService;
import com.brogrammers.brogrammers.domain.service.ProductCategoryService;
import com.brogrammers.brogrammers.domain.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PartProductController {

    @Autowired ProductService productService;
    @Autowired ProductCategoryService productCategoryService;
    @Autowired CategoryService categoryService;
    @GetMapping("products/soldOut")
    public String almostSoldOutPage(Model model,  // 재고 임박 컨트롤러
                                    @PageableDefault(page=0,size=2,sort="stockQuantity",direction = Sort.Direction.ASC) Pageable pageable, String searchKeyword){ // 솔드아웃 컨트롤러
        Page<Products> products = null;
        int num = 10; // 재고 수량
        if(searchKeyword==null){
            products = productService.soldOutList(num,pageable); // 검색 키워드가 없을 때
        } else{
            products = productService.soldOutList(num,searchKeyword, pageable); // 검색 키워드가 있을  때

        }
        String pname = "soldOut";
        String bodytitle = "매진임박";
        int nowPage = products.getPageable().getPageNumber() + 1; // 5
        int startPage = Math.max(1,nowPage%3==0?nowPage/3*3-2:nowPage/3*3+1);
        int endPage = Math.min(products.getTotalPages(),startPage+2);
        model.addAttribute("bodytitle",bodytitle);
        model.addAttribute("pname",pname);
        model.addAttribute("products",products);
        model.addAttribute("nowPage",nowPage);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);

        return "products/list";
    }

    @GetMapping("products/{gender}")
    public String genderList(Model model,@PageableDefault(page=0,size=2,sort="name",direction = Sort.Direction.ASC) Pageable pageable,
                             @PathVariable("gender") String gender,String searchKeyword){ // 성별 관련
        Page<Products> products = null;
        int num = 10; // 재고 수량
        if(searchKeyword==null){
            products = productService.findProductsByGender(gender,pageable); // 검색 키워드가 없을 때
        } else{
            products = productService.findProductsByGenderAndKeyword(gender,searchKeyword, pageable); // 검색 키워드가 있을  때

        }
        String pname = gender;
        String bodytitle = gender.equals("male")?"남성":"여성";
        int nowPage = products.getPageable().getPageNumber() + 1; // 5
        int startPage = Math.max(1,nowPage%3==0?nowPage/3*3-2:nowPage/3*3+1);
        int endPage = Math.min(products.getTotalPages(),startPage+2);
        model.addAttribute("bodytitle",bodytitle);
        model.addAttribute("pname",pname);
        model.addAttribute("products",products);
        model.addAttribute("nowPage",nowPage);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);

        return "products/list";
    }

    @GetMapping("products/category/{categoryName}")
    public String getProductsByCategory(@PathVariable("categoryName")String categoryName, Model model,
                                        @PageableDefault(page=0,size=2,sort="id",direction = Sort.Direction.DESC) Pageable pageable){
        Category category = categoryService.findOneByName(categoryName).get();
        Page<Products> products = productCategoryService.findProductsByCategory1(category, pageable);
        String pname = "category/"+categoryName;
        String bodytitle = categoryName;
        int nowPage = products.getPageable().getPageNumber() + 1; // 5
        int startPage = Math.max(1,nowPage%3==0?nowPage/3*3-2:nowPage/3*3+1);
        int endPage = Math.min(products.getTotalPages(),startPage+2);
        model.addAttribute("bodytitle",bodytitle);
        model.addAttribute("pname",pname);
        model.addAttribute("products",products);
        model.addAttribute("nowPage",nowPage);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
        return "products/list";
    }
}
