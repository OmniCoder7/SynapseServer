package com.proton.demo

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ClickMartApplication

fun main(args: Array<String>) {
    runApplication<ClickMartApplication>(*args)
}

private var mongoDatabase: MongoDatabase? = null
fun mongoDatabase(): MongoDatabase {
    if (mongoDatabase == null) {
        val serverApi = ServerApi.builder()
            .version(ServerApiVersion.V1)
            .build()
        val settings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString("mongodb://localhost:27017/"))
            .serverApi(serverApi)
            .build()
        val mongoClient = MongoClient.create(settings)
        return mongoClient.getDatabase("ClickMart")
    } else return mongoDatabase as MongoDatabase
}
