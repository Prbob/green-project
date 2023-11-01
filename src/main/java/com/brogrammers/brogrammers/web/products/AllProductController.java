package com.brogrammers.brogrammers.web.products;

import com.brogrammers.brogrammers.domain.member.Member;
import com.brogrammers.brogrammers.domain.product.*;
import com.brogrammers.brogrammers.domain.service.*;
import com.brogrammers.brogrammers.form.DuplicatedProduct;
import com.brogrammers.brogrammers.web.FunctionClass;
import com.brogrammers.brogrammers.web.session.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
public class AllProductController {

    @Autowired ProductService productService;
    @Autowired ImgService imgService;
    @Autowired MemberService memberService;
    @Autowired FunctionClass fun;
    @Autowired CategoryService categoryService;
    @Autowired BrandService brandService;
    @Autowired ProductCategoryService productCategoryService;
    /////////////////////////////////////상품 등록 ///////////////////////////////////
    @GetMapping("/products/add")
    public String addForm(@ModelAttribute("form") ProductForm form ,Model model, @PathVariable(value = "productId",required = false)Long productId,
                          HttpServletRequest request){
        if(fun.getMember(request)==null || fun.getMember(request).getAccessrigths().equals("NORMAL")){return "/alert/noLogin";}
        List<Brand> brands = brandService.findAll();
        List<Category> categories = categoryService.findAll();
        model.addAttribute("add","add");
        model.addAttribute("brands",brands);
        model.addAttribute("categories",categories);
        return "products/add";
    }
    @PostMapping("/products/add")
    public String add(@Valid @ModelAttribute("form") ProductForm form ,BindingResult result ,HttpServletRequest request,
                      Model model,@PathVariable(value = "productId",required = false)Long productId) throws Exception{
        List<Brand> brands = brandService.findAll();
        List<Category> categories = categoryService.findAll();
        if(result.hasErrors()){
            model.addAttribute("categories",categories);
            model.addAttribute("brands",brands);
            return "products/add";
        }
        DuplicatedProduct dupl = fun.getDuplicatedProduct(form); // 영속성X
        Optional<Long> duplProductId = productService.duplChk(dupl);
        if(duplProductId.isPresent()){
            model.addAttribute("categories",categories);
            model.addAttribute("brands",brands);
            result.reject("addProductFail","이미 등록된 아이템 입니다. 상품번호 : " + duplProductId.get());
            return "products/add";
        }
        Member member = fun.getMember(request); // 조회된 회원 아이디, DB에서 X , 세션에서 가져옴
        Products products = new Products();
        Brand brand = brandService.findOneByName(form.getProductBrand()).get(); // 브랜드 가져오기
        form.setMember(member); // 상품 등록할 때 회원 아이디 입력하기
        products.saveProduct(form.getProductName(), brand, form.getProductColor(),form.getPrice(),form.getSize(),
                form.getStockQuantity(),form.getMember(),form.getGender());
        // products 객체 만들기
        List<Imgs> list = new ArrayList<>(); // 이미지 디테일 페이지에 보내주기 위한 리스트
        if(!form.getImg().isEmpty()){ // 썸네일이 업로드가 됐으면
            fun.saveImg(products,form.getImg()); // 썸네일 저장하는 메서드
        }
        productService.save(products); // 상품 저장
        String categoryName = form.getProductCategory(); // 카테고리 이름 가져오기
        Category category = categoryService.findOneByName(categoryName).get(); // 카테고리

        ProductsCategory productsCategory = ProductsCategory.builder().category(category).products(products).build();
        productCategoryService.save(productsCategory); // 상품과 카테고리 매칭시켜줌.
        if(!form.getImgs()[0].isEmpty()){ // 사진이 업로드가 됐으면
            list = fun.saveImgs(form.getImgs(),products,list); // 상품 사진들 업로드
        }
        if(!list.isEmpty()){model.addAttribute("imgs",list);}
        model.addAttribute("mgs","상품 등록이 완료되었습니다.");
        model.addAttribute("product",products);
        return "alert/alert"; // alert창 띄우고 리스트로
    }
    /////////////////////////////////////상품 등록 ///////////////////////////////////


//@PageableDefault(page=0,size=10,sort="id",direction = Sort.Direction.DESC)
    /////////////////////////////////////상품 리스트 ///////////////////////////////////
    @GetMapping("/products/list")  // 전체 상품 조회
    public String productList(Model model, HttpServletRequest request,
                              @PageableDefault(page=0,size=2,sort="name",direction = Sort.Direction.DESC)Pageable pageable,
                              String searchKeyword){
        Page<Products> products = null;
        if(searchKeyword!=null){
            products = productService.productSearchList(searchKeyword,pageable);
        }else{
            products = productService.findAll(pageable);
        }
        List<Category> all = categoryService.findAll();
        model.addAttribute("all",all);
        String bodytitle = "전체 상품";
        int nowPage = products.getPageable().getPageNumber() + 1; // 5
        int startPage = Math.max(1,nowPage%3==0?nowPage/3*3-2:nowPage/3*3+1);
        int endPage = Math.min(products.getTotalPages(),startPage+2);
        String pname = "list";
        model.addAttribute("bodytitle",bodytitle);
        model.addAttribute("pname",pname);
        model.addAttribute("products",products);
        model.addAttribute("nowPage",nowPage);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
        return "products/list";
    }
    @GetMapping("/products/myList")  // 로그인 정보에 맞는 상품 리스트 불러오기 ?
    public String allMyProducts(Model model,HttpServletRequest request){
        Member member = fun.getMember(request); // 세션에서 멤버정보 불러오기
        member = memberService.findById(member.getId());
//        List<Products> products =  productService.findAllByMemberId(member.getId()); // 로그인된 id 값으로 상품 리스트 불러오기

        List<Products> products = member.getProdutcs();
        model.addAttribute("products",products);
        return "products/myList";
    }
    /////////////////////////////////////상품 리스트 ///////////////////////////////////

    ///////////////////////////////////// 디테일 ///////////////////////////////////
    @GetMapping("/products/detail/{productId}") // 디테일 페이지 컨트롤러
    public String detail(@PathVariable("productId")Long productId,Model model,HttpServletRequest request){
        Products product = productService.getByid(productId);
        List<Imgs> imgs = product.getImgs();
        Member member = fun.getMember(request);
        String accessrigths = "NORMAL";
        if(member!=null){ // 로그인 하지 않았을 경우
            accessrigths = String.valueOf(member.getAccessrigths());
        }

        model.addAttribute("accessrigths",accessrigths);
        model.addAttribute("imgs",imgs);
        model.addAttribute("product", product);
        return "products/detail";
    }

    ///////////////////////////////////// 디테일 ///////////////////////////////////


    /////////////////////////////////// edit ///////////////////////////////////
    @GetMapping("/products/edit/{productId}") // 상품 수정 페이지
    public String editForm(@PathVariable("productId")Long productId,@ModelAttribute("form")ProductForm form,Model model, HttpServletRequest request){
        HttpSession session = request.getSession(false); // 세션 불러오기
        if(session==null){
            return "redirect:/";
        }
        Member member = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        if(member==null ){
            // 없는 회원이거나
            return "redirect:/";
        }
        if(member.getAccessrigths().equals("NORMAL")){
            //회원 권한이 NOMAL일 경우 index페이지로 이동
            return "redirect:/";
        }
        form = productService.getProductFormByid(productId);
        List<Brand> brands = brandService.findAll();
        List<Category> categories = categoryService.findAll();
        model.addAttribute("form",form);
        model.addAttribute("brands",brands);
        model.addAttribute("categories",categories);
        return "products/edit";
    }
    @PostMapping("/products/edit/{productId}")
    public String edit(@PathVariable("productId")Long productId,@Valid @ModelAttribute("form")ProductForm form,
                       BindingResult result,HttpServletRequest request,Model model) throws Exception {
        List<Brand> brands = brandService.findAll();
        List<Category> categories = categoryService.findAll();
        if(result.hasErrors()){
            model.addAttribute("categories",categories);
            model.addAttribute("brands",brands);
            return "products/add";
        }
        DuplicatedProduct dupl = fun.getDuplicatedProduct(form); //
        Optional<Long> duplProductId = productService.duplChk(dupl);
        if(duplProductId.isPresent() && !productId.equals(duplProductId.get())){
            model.addAttribute("categories",categories);
            model.addAttribute("brands",brands);
            result.reject("addProductFail","이미 등록된 아이템 입니다. 상품번호 : " + duplProductId.get());
            return "products/add";
        }
        ///////////////////////////////////// 오류체크 ////////////////////////////////////

        Products product = productService.updateProduct(form, productId); // 데이터 업데이트 1
        Member member = fun.getMember(request);
        if(!form.getImg().isEmpty()){
            if(product.getImgPath() != null){
                new File(product.getImgPath()).delete();
            }
            product = fun.saveImg(product, form.getImg()); // 이미지 저장된 product
        }
        List<Imgs> list = new ArrayList<>();
        if(!form.getImgs()[0].isEmpty()){
            if(product.getImgs() != null){
                product.getImgs().forEach(l -> new File(l.getPath()).delete()); // 저장돼있는 사진들 삭제
            }
            list = fun.saveImgs(form.getImgs(), product,list); // 디테일 페이지로 넘겨줄 사진들
        }
        productService.updateProduct(product);

        model.addAttribute("accessrigths",member);
        model.addAttribute("imgs",list);
        model.addAttribute("product",product);
        return "/products/detail";
    }
    ///////////////////////////////////// edit ///////////////////////////////////

    @GetMapping("/products/delete/{id}")
    public String delete(@PathVariable("id") Long id, HttpServletRequest request){
        Member member = fun.getMember(request);
        if(member == null || member.getAccessrigths().equals("NORMAL")){
            return "redirect:/";
        }
        System.out.println("상품의 아이디 : " + id);
        Products products = productService.getByid(id);
        List<Imgs> imgs = imgService.findImgsByProducts(products);
        if(products.getImgPath()!=null){
            new File(products.getImgPath()).delete();
        }
        if(!imgs.isEmpty()){
            imgs.forEach(img->new File(img.getPath()).delete());
            imgs.forEach(i->imgService.deleteImgs(i.getId()));
        }
        productCategoryService.deleteByProducts(products);
        productService.deleteProduct(id);
        return "redirect:/products/list";
    }



}
