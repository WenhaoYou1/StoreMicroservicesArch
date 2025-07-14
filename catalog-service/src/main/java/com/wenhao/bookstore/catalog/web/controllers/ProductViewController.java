package com.wenhao.bookstore.catalog.web.controllers;
// ThymeLeaf View Controller, is based on index.html && product-detail.html
import com.wenhao.bookstore.catalog.domain.PagedResult;
import com.wenhao.bookstore.catalog.domain.Product;
import com.wenhao.bookstore.catalog.domain.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductViewController {
    private final ProductService productService;

    public ProductViewController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String home(Model model, @RequestParam(name = "page", defaultValue = "1") int pageNo) {
        PagedResult<Product> products = productService.getProducts(pageNo);
        model.addAttribute("products", products);
        model.addAttribute("currentPage", pageNo);
        return "index";
    }

    @GetMapping("/product/{code}")
    public String productDetail(@PathVariable String code, Model model) {
        productService.getProductByCode(code).ifPresent(product -> {
            model.addAttribute("product", product);
        });
        return "product-detail";
    }
}
