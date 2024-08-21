package com.proton.demo.model.address

import jakarta.validation.constraints.NotBlank


data class AddressDTO(
    @NotBlank(message = "Street Name is required")
    val streetName: String = "",// In database it is stored as streetAddress
    @NotBlank(message = "Street number is required")
    val streetNumber: Int = 0,
    val streetSuffix: String = "",
    @NotBlank(message = "City name is required")
    val city: String = "",
    @NotBlank(message = "State name is required")
    val state: String = "",
    @NotBlank(message = "Postal Code is required")
    val postalCode: String = "",
    val country: String = "",
    val addressLane2: String = ""
) {

}