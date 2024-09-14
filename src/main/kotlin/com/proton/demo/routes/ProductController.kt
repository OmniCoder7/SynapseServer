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
        @RequestParam pageSize: Int
    ): ResponseEntity<List<ProductPreview>> {
        return ResponseEntity.ok(productService.getPreviewProducts(pageSize, pageNo))
    }

    @PostMapping("/{userId}/cart")
    fun addToCart(@PathVariable userId: Long, @RequestBody product: Product): ResponseEntity<String> {
        val user = getUserOrThrow(userId)
        userProductService.addToCart(product, user)
        return ResponseEntity.ok("Product added to cart successfully")
    }

    @DeleteMapping("/{userId}/cart")
    fun removeFromCart(@PathVariable userId: Long, @RequestBody product: Product): ResponseEntity<String> {
        val user = getUserOrThrow(userId)
        return if (userProductService.removeFromCart(product, user)) {
            ResponseEntity.ok("Product removed from cart successfully")
        } else {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to remove product from cart")
        }
    }

    @PostMapping("/{userId}/wishlist")
    fun addToWishList(@PathVariable userId: Long, @RequestBody product: Product): ResponseEntity<String> {
        val user = getUserOrThrow(userId)
        return if (userProductService.addToWishList(product, user)) {
            ResponseEntity.ok("Product added to wishlist successfully")
        } else {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add product to wishlist")
        }
    }

    @DeleteMapping("/{userId}/wishlist")
    fun removeFromWishList(@PathVariable userId: Long, @RequestBody product: Product): ResponseEntity<String> {
        val user = getUserOrThrow(userId)
        return if (userProductService.removeFromWishList(product, user)) {
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