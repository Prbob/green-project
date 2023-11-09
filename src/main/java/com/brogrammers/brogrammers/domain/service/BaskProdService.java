package com.brogrammers.brogrammers.domain.service;

import com.brogrammers.brogrammers.domain.order.Basket;
import com.brogrammers.brogrammers.domain.product.BasketProducts;
import com.brogrammers.brogrammers.domain.product.Products;
import com.brogrammers.brogrammers.domain.repository.BaskProdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BaskProdService {

    private final BaskProdRepository baskProdRepository;

    public void save(BasketProducts basketProducts){
        baskProdRepository.save(basketProducts);
    }
    public Optional<BasketProducts> findById(Long id){return baskProdRepository.findById(id);}
    public List<Products> findProductsByBasketId(Basket basket){return baskProdRepository.findProductsByBasket(basket);}

    public Page<Products> findProductsByBasket(Basket basket, Pageable pageable){return baskProdRepository.findProductsByBasket(basket, pageable);}

    // 이건 안쓸듯
    public Optional<BasketProducts> findBasProByBasAndPro(Basket basket, Products products){return baskProdRepository.findOneByBaskeyAndProducts(basket,products);}

    public void baskProdUpdate(BasketProducts basketProducts){  // 장바구니 수량 업데이트
        BasketProducts origin = findById(basketProducts.getId()).get();
        origin = basketProducts;
    }
    public void delete(Long id){
        baskProdRepository.deleteBasketProductsById(id);
    }
    public void delete(Basket basket, Products products){
        baskProdRepository.deleteBaskProdByBasketAndProducts(basket,products);
    }
}
