package com.proton.demo.routes

import com.proton.demo.model.product.ProductPreview
import com.proton.demo.security.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/product")
class ProductController(
    private val productService: ProductService
) {

    @GetMapping("/productPreviews")
    fun getProductPreview(@RequestParam pageNo: Int,
                          @RequestParam pageSize: Int): ResponseEntity<List<ProductPreview>> {
        return ResponseEntity.ok(productService.getPreviewProducts(pageSize, pageNo))
    }
}