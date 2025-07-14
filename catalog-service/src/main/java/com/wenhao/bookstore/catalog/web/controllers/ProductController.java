package com.wenhao.bookstore.catalog.web.controllers;

import com.wenhao.bookstore.catalog.domain.PagedResult;
import com.wenhao.bookstore.catalog.domain.Product;
import com.wenhao.bookstore.catalog.domain.ProductNotFoundException;
import com.wenhao.bookstore.catalog.domain.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController // 接口方法返回对象, 转换成JSON文本
@RequestMapping("/api/products")
class ProductController {
    private final ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }
    // @PostMapping 增加
    // @PutMapping 修改
    // @DeleteMapping 删除
    // 注意: 不存在@Get的写法, 不过可以写 @RequestMapping(method = RequestMethod.GET)
    @GetMapping // 表示查询
    PagedResult<Product> getProducts(@RequestParam(name = "page", defaultValue = "1") int pageNo) {
        return productService.getProducts(pageNo);
    }

    @GetMapping("/{code}")
    ResponseEntity<Product> getProductByCode(@PathVariable String code) {
        return productService
                .getProductByCode(code)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> ProductNotFoundException.forCode(code));
    }
}
