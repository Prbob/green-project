package com.brogrammers.brogrammers.web.products;


import com.brogrammers.brogrammers.domain.member.Member;
import com.brogrammers.brogrammers.domain.product.Brand;
import com.brogrammers.brogrammers.domain.product.Category;
import com.brogrammers.brogrammers.domain.service.BrandService;
import com.brogrammers.brogrammers.domain.service.CategoryService;
import com.brogrammers.brogrammers.web.FunctionClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class CategoryController {

    @Autowired CategoryService categoryService;
    @Autowired BrandService brandService;
    @Autowired FunctionClass fun;
    //////////////////////////////////// 카데고리 등록폼 ///////////////////////////
    @GetMapping("/category/form")
    public String addCategoryForm(Model model,HttpServletRequest request){
        if(fun.getMember(request)==null || fun.getMember(request).getAccessrigths().equals("NORAML")){
            return "alert/noLogin";
        }
        model.addAttribute("cateAdd","cateAdd");
        model.addAttribute("msg","카테고리 등록");
        model.addAttribute("form",new CategoryForm());
        return "category/categoryForm";
    }
    @PostMapping("/category/form")
    public String addCategory(@Valid @ModelAttribute("form") CategoryForm form, BindingResult bindingResult, HttpServletRequest httpServletRequest, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("cateAdd","cateAdd");
            model.addAttribute("msg","카테고리 등록");
            return "category/categoryForm";
        }
        Long categoryId = categoryService.duplChk(form.getName());
        if(categoryId!=null){
            model.addAttribute("cateAdd","cateAdd");
            model.addAttribute("msg","카테고리 등록");
            bindingResult.rejectValue("name","error.name","이미 존재하는 카테고리 입니다.");
            return "category/categoryForm";
        }
        Category category = Category.builder().name(form.getName()).build();
        categoryService.save(category);
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList",categoryList);
        return "category/categoryList";
    }
    @GetMapping("/category/list")
    public String categoryList(Model model,HttpServletRequest request){
        if(fun.getMember(request)==null || fun.getMember(request).getAccessrigths().equals("NORAML")){
            return "alert/noLogin";
        }
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList",categoryList);
        model.addAttribute("cateUp","cateUp");
        return "category/categoryList";
    }

    @GetMapping("/category/brand")
    public String brandForm(Model model, HttpServletRequest request){
        if(fun.getMember(request)==null || fun.getMember(request).getAccessrigths().equals("NORAML")){
            return "alert/noLogin";
        }
        model.addAttribute("form",new BrandForm());
        model.addAttribute("brandAdd","brandAdd");
        model.addAttribute("msg","브랜드 등록");
        return "category/brandForm";
    }
    @PostMapping("/category/brand")
    public String addBrand(@Valid @ModelAttribute("form") BrandForm form, BindingResult bindingResult,Model model){
        if(bindingResult.hasErrors()){
            return "category/brandForm";
        }
        Long brandId = brandService.duplChk(form.getName());
        if(brandId!=null){
            bindingResult.rejectValue("name","error.name","이미 존재하는 카테고리 입니다.");
            return "category/brandForm";
        }
        brandService.save(form.getName());
        List<Brand> list = brandService.findAll();
        model.addAttribute("brandList",list);
        return "category/brandList";
    }
    @GetMapping("/category/brandList")
    public String brandList(Model model, HttpServletRequest request){
        if(fun.getMember(request)==null || fun.getMember(request).getAccessrigths().equals("NORAML")){
            return "alert/noLogin";
        }
        List<Brand> brandList = brandService.findAll();
        model.addAttribute("brandList",brandList);
        model.addAttribute("brandL","brandL");
        return "category/brandList";
    }

    @GetMapping("/category/updatBrand/{id}")
    public String updatBrandFrom(@PathVariable("id")Long id,Model model, HttpServletRequest request){
        BrandForm form = brandService.getBrandFormById(id);
        model.addAttribute("form",form);
        model.addAttribute("brandList","brandList");
        model.addAttribute("msg","브랜드 업데이트");
        return "category/brandForm";
    }
    @PostMapping("/category/updatBrand/{id}")
    public String updatBrand(@PathVariable("id")Long id,Model model,
                             @Valid @ModelAttribute("form") BrandForm form,BindingResult result ,HttpServletRequest request){
        if(fun.getMember(request)==null || fun.getMember(request).getAccessrigths().equals("NORAML")){
            return "alert/noLogin";
        }

        if(result.hasErrors()){
            model.addAttribute("brandList","brandList");
            model.addAttribute("msg","브랜드 업데이트");
            return "category/brandForm";
        }
        Optional<Brand> brand = brandService.findOneByName(form.getName());
        if(brand.isPresent() && !brand.get().getId().equals(id)){
            model.addAttribute("brandList","brandList");
            model.addAttribute("msg","브랜드 업데이트");
            result.rejectValue("name","error.name","이미 존재하는 브랜드 입니다. ");
            return "category/brandForm";

        }
        brandService.updatBrandByForm(form);
        model.addAttribute("form",form);
        return "alert/brandAlert";
    }
    @GetMapping("/category/updatCategory/{id}")
    public String updatCategoryForm(@PathVariable("id")Long id, HttpServletRequest request,
                              Model model){
        if(fun.getMember(request)==null || fun.getMember(request).getAccessrigths().equals("NORMAL")){return "/alert/noLogin";}

        CategoryForm form = categoryService.getCategoryFromById(id);
        model.addAttribute("cateUp","cateUp");
        model.addAttribute("msg","카테고리 수정");
        model.addAttribute("form",form);
        return "category/categoryForm";
    }
    @PostMapping("/category/updatCategory/{id}")
    public String updatCategory(@PathVariable("id")Long id, HttpServletRequest request,Model model,
                                @ModelAttribute("form") @Valid CategoryForm form,BindingResult result){
        if(result.hasErrors()){
            return "category/categoryForm";
        }
        Optional<Category> cateChk = categoryService.findOneByName(form.getName());
        if(cateChk.isPresent() && !id.equals(cateChk.get().getId())){
            model.addAttribute("msg","카테고리 업데이트");
            result.rejectValue("name","error.name","이미 존재하는 카테고리입니다. ");
            return "category/categoryForm";
        }

        categoryService.updatCatoegryByForm(form);
        model.addAttribute("msg","카테고리 업데이트");
        model.addAttribute("form",form);
        return "redirect:/category/list";
    }
}
