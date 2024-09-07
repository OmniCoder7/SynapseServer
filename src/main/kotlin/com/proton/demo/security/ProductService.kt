package com.proton.demo.security

import com.proton.demo.model.product.Product
import com.proton.demo.model.product.ProductPreview
import com.proton.demo.model.product.toProductPreview
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val mongoTemplate: MongoTemplate
) {
    fun getPreviewProducts(pageSize: Int, pageNo: Int): List<ProductPreview> {
        val pageable = PageRequest.of(pageNo, pageSize)
        val query = Query().with(pageable)
        return PageableExecutionUtils.getPage(
            mongoTemplate.find(query, Product::class.java).map { it.toProductPreview() },
            pageable
        ) {
            mongoTemplate.count(Query.of(query).limit(pageSize).skip((pageSize * pageNo).toLong()), Product::class.java)
        }.toList()
    }

}