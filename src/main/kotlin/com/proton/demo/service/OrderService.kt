package com.proton.demo.service

import com.proton.demo.model.product.Order
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val mongoTemplate: MongoTemplate
) {
    fun saveOrder(order: Order) = mongoTemplate.save(order)
    fun getOrderById(id: Long) = mongoTemplate.find(Query(Criteria.where(Order::orderId.name).`is`(id)), Order::class.java)
}