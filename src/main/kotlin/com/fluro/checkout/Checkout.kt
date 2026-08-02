package com.fluro.checkout

import com.fluro.checkout.domain.Money
import com.fluro.checkout.domain.Sku
import com.fluro.checkout.rules.PricingRule

class Checkout(private val pricingRules: List<PricingRule>) {
    private val scannedItems = mutableListOf<Sku>()

    fun scan(item: Sku) {
        scannedItems.add(item)
    }

    fun total(): Money {
        return pricingRules
            .map { rule -> rule.evaluate(scannedItems) }
            .fold(Money.ZERO) { currentTotal, effect -> effect.applyTo(currentTotal) }
    }
}
