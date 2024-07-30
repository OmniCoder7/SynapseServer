package com.proton.demo.service

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections
import com.proton.demo.model.product.Product
import com.proton.demo.model.product.ProductPreview
import com.proton.demo.mongoDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.bson.conversions.Bson

class ProductService {
    private val productCollection = mongoDatabase().getCollection<Product>("Product")

    private val productPreviewProjections =
        Projections.fields(
            Projections.include(
                ProductPreview::name.name,
                ProductPreview::price.name,
                ProductPreview::discountedPrice.name,
                ProductPreview::rating.name,
                ProductPreview::isOnSale.name,
                ProductPreview::imageUrl.name
            ),
            Projections.excludeId()
        )

    fun getProductPreview(): List<ProductPreview> {
        var productList: List<ProductPreview>
        runBlocking {
            productList = productCollection.find<ProductPreview>().projection(productPreviewProjections).toList()
        }
        return productList
    }

    fun getProductWithProperty(property: String, id: Any): List<ProductPreview> {
        var product: List<ProductPreview>
        runBlocking {
            product = productCollection.find<ProductPreview>(Filters.and(Filters.lt(property, id)))
                .projection(productPreviewProjections).toList()
        }
        return product
    }

    fun getProductWithId(id: Int): Product {
        var product: Product
        runBlocking { product = productCollection.find(Filters.and(Filters.lt(Product::id.name, id))).first() }
        return product
    }

    fun insertProduct(product: Product) = runBlocking {
        productCollection.insertOne(product)
    }

    fun deleteProductBy(property: String, value: String) = runBlocking {
        productCollection.deleteMany(Filters.eq(property, value))
    }

    fun getSortedProductWithProperty(property: String, id: Any, sortOrder: Bson): List<Product> {
        var product: List<Product>
        runBlocking { product = productCollection.find(Filters.and(Filters.lt(property, id))).sort(sortOrder).toList() }
        return product
    }
}