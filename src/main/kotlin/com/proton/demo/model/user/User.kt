package com.proton.demo.model.user

import com.proton.demo.model.address.Address
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Document("User")
data class User(
    @Id
    var userId: Long,
    @Field("first_name")
    val firstName: String = "",
    @Field("last_name")
    val lastName: String = "",
    val email: String = "",
    val gender: String = "",
    @Field("date_of_birth")
    val dob: String = "",
    val age: Int = 0,
    @Field("username")
    var userName: String = "",
    val number: Long = 0,
    @Field("password")
    val loginPassword: String = "", // Bcrypt Hashed
    val address: List<Address> = listOf(),
    val cardId: List<Int> = listOf(),
) : UserDetails {
    companion object {
        @Transient
        val USER_SEQUENCE: String = "user_sequence"
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableSetOf()

    override fun getPassword(): String = loginPassword

    override fun getUsername(): String = email

}

/*
* Fashion & Apparel
* Clothing,Men's,Women's,Children's,Footwear,Shoes,Boots,Sandals,Accessories,Jewelry,Watches,Bags,Sportswear,Activewear,Lingerie,Swimwear
*
* Electronics & Gadgets
* Computers, Laptops, Smartphones, Tablets, TVs, Home Entertainment, Audio Equipment, Gaming Consoles, Accessories, Appliances, Kitchenware
*
* Home & Decor
* Furniture, Bedding & Bath, Kitchenware & Appliances, Home Improvement & Tools, Lighting & Decor
*
* Food & Beverage
* Groceries, Staples, Fresh Produce, Meat, Seafood, Snacks, Beverages, Alcohol, Coffee, Tea
*
* Toys & Hobbies
* Toys, Action figures, Dolls, Games, Arts & Crafts, Sports & Outdoor, Collectibles & Memorabilia, Model Kits & Puzzles
*
* Others
* Baby & Kids, Diapers, Clothing, Toys, Pets & Pet Supplies, Office Supplies, Stationery, Travel & Luggage, Automotive, Parts, Books, Music, Digital Services, Streaming, Subscriptions
* */
