package com.proton.demo.service

import com.proton.demo.exception.UserNotFoundException
import com.proton.demo.model.product.Order
import com.proton.demo.model.product.Product
import com.proton.demo.model.user.User
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service

@Service
class UserProductService(
    private val mongoTemplate: MongoTemplate,
    private val orderService: OrderService,
    private val userService: UserAuthService
) {
    fun addToCart(product: Product, user: User) =
        mongoTemplate.updateFirst(
            Query(Criteria.where(User::userId.name).`is`(user.userName)),
            Update().set(User::cartProducts.name, listOf(user.cartProducts, product.productId)),
            User::class.java
        ).wasAcknowledged()

    fun removeFromCart(product: Product, user: User) =
        mongoTemplate.updateFirst(
            Query(Criteria.where(User::userId.name).`is`(user.userName)),
            Update().set(User::cartProducts.name, user.cartProducts.filter { it != product.productId }),
            User::class.java
        ).wasAcknowledged()

    fun addOrderHistory(order: Order) =
        orderService.saveOrder(order)

    fun addToWishList(product: Product, user: User) =
        mongoTemplate.updateFirst(
            Query(Criteria.where(User::userId.name).`is`(user.userName)),
            Update().set(User::wishListIds.name, listOf(user.wishListIds, product.productId)),
            User::class.java
        ).wasAcknowledged()

    fun removeFromWishList(product: Product, user: User) =
        mongoTemplate.updateFirst(
            Query(Criteria.where(User::userId.name).`is`(user.userName)),
            Update().set(User::wishListIds.name, user.wishListIds.filter { it != product.productId }),
            User::class.java
        ).wasAcknowledged()

    fun getOrderHistory(id: Long) =
        orderService.getOrderById(id)

    fun getCartProducts(id: Long): List<Product> {
        val user = userService.getUser(id) ?: throw UserNotFoundException(id)
        return user.cartProducts.map {
            mongoTemplate.findOne(
                Query(Criteria.where(Product::productId.name).`is`(it)),
                Product::class.java
            )!!
        }
    }

    fun getWishlist(id: Long): List<Product> {
        val user = userService.getUser(id) ?: throw UserNotFoundException(id)
        return user.wishListIds.map {
            mongoTemplate.findOne(
                Query(Criteria.where(Product::productId.name).`is`(it)),
                Product::class.java
            )!!
        }
    }
}