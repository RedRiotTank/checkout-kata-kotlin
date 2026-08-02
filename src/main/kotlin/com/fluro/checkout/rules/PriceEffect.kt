package com.fluro.checkout.rules

import com.fluro.checkout.domain.Money

sealed interface PriceEffect {
    fun applyTo(total: Money): Money
}

data class Charge(val amount: Money) : PriceEffect {
    override fun applyTo(total: Money): Money = total + amount
}

data class Discount(val amount: Money) : PriceEffect {
    override fun applyTo(total: Money): Money = total - amount
}
