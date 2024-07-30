package com.proton.demo.model.category

sealed interface Category {
    data class Fashion(val fashion: FashionCategory): Category
    data class Electronics(val electronicsCategory: ElectronicsCategory): Category
    data class HomeAndGarden(val homeAndGardenCategory: HomeAndGardenCategory): Category
    data class Beauty(val beautyCategory: BeautyAndPersonalCareCategory): Category
    data class Games(val gamesAndToysCategory: GamesAndToysCategory): Category
}
