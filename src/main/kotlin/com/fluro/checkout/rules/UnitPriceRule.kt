package com.fluro.checkout.rules

import com.fluro.checkout.domain.Money
import com.fluro.checkout.domain.Sku

class UnitPriceRule(private val prices: Map<Sku, Money>) : PricingRule {
    override fun evaluate(items: List<Sku>): PriceEffect {
        val total =
            items.fold(Money.ZERO) { acc, item ->
                acc + (prices[item] ?: Money.ZERO)
            }
        return Charge(total)
    }
}
