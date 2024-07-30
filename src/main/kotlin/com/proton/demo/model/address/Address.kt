package com.proton.demo.model.address

import org.springframework.data.mongodb.core.mapping.Field

data class Address(
    @Field("street_address")
    val streetAddress: String = "",
    @Field("street_number")
    val streetNumber: Int = 0,
    val city: String = "",
    val state: String = "",
    @Field("postal_code")
    val postalCode: String = "",
    val country: String = "",
    @Field("street_suffix")
    val streetSuffix: String  = "",
    @Field("address_line_2")
    val addressLane2: String = ""
)
