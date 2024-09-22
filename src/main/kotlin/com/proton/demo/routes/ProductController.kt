package com.proton.demo.routes

import com.proton.demo.exception.UserNotFoundException
import com.proton.demo.model.product.Order
import com.proton.demo.model.product.Product
import com.proton.demo.model.product.ProductPreview
import com.proton.demo.model.user.User
import com.proton.demo.security.ProductService
import com.proton.demo.service.UserAuthService
import com.proton.demo.service.UserProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/product")
class ProductController(
    private val productService: ProductService,
    private val userProductService: UserProductService,
    private val userService: UserAuthService
) {
    @GetMapping("/productPreviews")
    fun getProductPreview(
        @RequestParam pageNo: Int,
        @RequestParam pageSize: Int,
        @RequestParam query: String
    ): ResponseEntity<List<ProductPreview>> {
        return ResponseEntity.ok(productService.getPreviewProducts(pageSize, pageNo))
    }

    @PostMapping("/{userId}/cart")
    fun addToCart(@PathVariable userId: Long, @RequestParam productId: Long): ResponseEntity<String> {
        val user = getUserOrThrow(userId)
        userProductService.addToCart(productId, user)
        return ResponseEntity.ok("Product added to cart successfully")
    }

    @DeleteMapping("/{userId}/cart")
    fun removeFromCart(@PathVariable userId: Long, @RequestParam productId: Long): ResponseEntity<String> {
        val user = getUserOrThrow(userId)
        return if (userProductService.removeFromCart(productId, user)) {
            ResponseEntity.ok("Product removed from cart successfully")
        } else {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to remove product from cart")
        }
    }

    @PostMapping("/{userId}/wishlist")
    fun addToWishList(@PathVariable userId: Long, @RequestParam productId: Long): ResponseEntity<String> {
        val user = getUserOrThrow(userId)
        return if (userProductService.addToWishList(productId, user)) {
            ResponseEntity.ok("Product added to wishlist successfully")
        } else {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add product to wishlist")
        }
    }

    @DeleteMapping("/{userId}/wishlist")
    fun removeFromWishList(@PathVariable userId: Long, @RequestParam productId: Long): ResponseEntity<String> {
        val user = getUserOrThrow(userId)
        return if (userProductService.removeFromWishList(productId, user)) {
            ResponseEntity.ok("Product removed from wishlist successfully")
        } else {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to remove product from wishlist")
        }
    }

    @GetMapping("/{userId}/cart")
    fun getCartProducts(@PathVariable userId: Long): ResponseEntity<List<Product>> {
        return ResponseEntity.ok(userProductService.getCartProducts(userId))
    }

    @GetMapping("/{userId}/wishlist")
    fun getWishlist(@PathVariable userId: Long): ResponseEntity<List<Product>> {
        return ResponseEntity.ok(userProductService.getWishlist(userId))
    }

    @GetMapping("/{userId}/orders/{orderId}")
    fun getOrderHistory(@PathVariable userId: Long, @PathVariable orderId: Long): ResponseEntity<List<Order?>?> {
        return ResponseEntity.ok(userProductService.getOrderHistory(orderId))
    }

    @PostMapping("/{userId}/orders")
    fun addOrderHistory(@PathVariable userId: Long, @RequestBody order: Order): ResponseEntity<Order> {
        return ResponseEntity.ok(userProductService.addOrderHistory(order))
    }

    private fun getUserOrThrow(userId: Long): User {
        return userService.getUser(userId) ?: throw UserNotFoundException(userId)
    }
}

/*
* access token - ya29.a0AcM612ylCWuEVT6Lo7wauEOkowg1Ks-fsu7D7Fx9-ZRvhBQX52c9JcsfB-_LuINHTeNIJ_bEV3-lrkXwBdNgRnOLfu52Xf-JXnRywNu7Om2NNKzJPaNxJiibYNVTHtDxVxb2QiwHxFrgzFEsspc4Wc4jB6cN7gNYsD5DboQQaCgYKAQ4SARMSFQHGX2MiaV3fIeAWPaXvCaloOClQAg0175
* id_token - eyJhbGciOiJSUzI1NiIsImtpZCI6ImIyNjIwZDVlN2YxMzJiNTJhZmU4ODc1Y2RmMzc3NmMwNjQyNDlkMDQiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI4MzAxNzMyNDc4ODEtYWJzMTFncG5jY2c4NmV1YWFhdGxlMXBuZGdwZ3Noc2UuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI4MzAxNzMyNDc4ODEtYWJzMTFncG5jY2c4NmV1YWFhdGxlMXBuZGdwZ3Noc2UuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMTgzMTQzNTE3MDA2ODcyNjk4MzIiLCJlbWFpbCI6InNhcmFzd2F0cjMzN0BnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiYXRfaGFzaCI6IjlCcWNJc0FrSlZIdG1peFBxOEVoeXciLCJuYW1lIjoiUmlzaGFiaCBTYXJhc3dhdCIsInBpY3R1cmUiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS9BQ2c4b2NMQVl3UkJ4MWd6eGRNbklFZy1YT0d0T1FoNkk0OVVBTXVYREl6UU1ObnpERnZEMnNCYj1zOTYtYyIsImdpdmVuX25hbWUiOiJSaXNoYWJoIiwiZmFtaWx5X25hbWUiOiJTYXJhc3dhdCIsImlhdCI6MTcyNjc3MzA2MiwiZXhwIjoxNzI2Nzc2NjYyfQ.bBbs57w_bvHx-U7HV2_DRq8hwtaIQlof2K_pCrlL61fOYXW4F5IZ_sHCWeEzcwL1fl84OoijaFsRihw-ElLL7FwdwMePyM5Aj4znabV17r0unjcVFPj30oT5iH0syQIlkQXDvOoURuIn_S_lPg1NDBzuG-aKsk90Xd8TBgzCohd0v9CVdLmqLgIUOfUX5dn0B8JheQAMSPuXZBsylFUBTlbVK18_H-geZKHSren5ioD8KSQnrmni7GMZod1fqFud3mfJNWjj8vXE5uJPTrnAaulmMl77KjOE6qrlGAzuhxJepAy818pUQPSYojNlbx-rgz2oaAsj-Np8Ewv7koG0kg
* */