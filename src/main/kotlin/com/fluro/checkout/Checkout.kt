package com.fluro.checkout

import com.fluro.checkout.domain.Money
import com.fluro.checkout.domain.Sku
import com.fluro.checkout.rules.PricingRule
import com.fluro.checkout.rules.RuleResult

class Checkout(private val pricingRules: List<PricingRule>) {
    private val scannedItems = mutableListOf<Sku>()

    fun scan(item: Sku) {
        scannedItems.add(item)
    }

    fun total(): Money {
        val initialTally = scannedItems.groupingBy { it }.eachCount()

        val finalResult =
            pricingRules.fold(RuleResult(Money.ZERO, initialTally)) { accumulated, rule ->
                if (accumulated.remainingItems.isEmpty()) return@fold accumulated

                val stepResult = rule.apply(accumulated.remainingItems)
                RuleResult(
                    price = accumulated.price + stepResult.price,
                    remainingItems = stepResult.remainingItems,
                )
            }

        return finalResult.price
    }
}
