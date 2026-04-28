package com.maxkach.elasticlist.ui.screens.coffee

import org.jetbrains.compose.resources.DrawableResource
import `scroll effect lazy`.sample.generated.resources.Res
import `scroll effect lazy`.sample.generated.resources.drink_01

data class Drink(
    val id: Int,
    val name: String,
    val res: DrawableResource,
    val price: String,
)

val drinks = listOf(
    Drink(
        id = 1,
        name = "Latte",
        res = Res.drawable.drink_01,
        price = "$4.50",
    ),
    Drink(
        id = 2,
        name = "Cappuccino",
        res = R.drawable.drink_02,
        price = "$4.00",
    ),
    Drink(
        id = 3,
        name = "Matcha Latte",
        res = R.drawable.drink_03,
        price = "$5.25",
    ),
    Drink(
        id = 4,
        name = "Cold Brew",
        res = R.drawable.drink_04,
        price = "$4.75",
    ),
    Drink(
        id = 5,
        name = "Mocha",
        res = R.drawable.drink_05,
        price = "$5.00",
    ),
    Drink(
        id = 6,
        name = "Chai Latte",
        res = R.drawable.drink_06,
        price = "$4.50",
    ),
    Drink(
        id = 7,
        name = "Espresso",
        res = R.drawable.drink_07,
        price = "$3.25",
    ),
    Drink(
        id = 8,
        name = "Flat White",
        res = R.drawable.drink_08,
        price = "$4.25",
    ),
    Drink(
        id = 9,
        name = "Hot Chocolate",
        res = R.drawable.drink_09,
        price = "$4.00",
    ),
    Drink(
        id = 10,
        name = "Vanilla Frappe",
        res = R.drawable.drink_10,
        price = "$5.50",
    ),
)
