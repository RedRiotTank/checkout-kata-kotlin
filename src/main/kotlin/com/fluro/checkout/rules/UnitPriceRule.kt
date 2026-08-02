package com.fluro.checkout.rules

import com.fluro.checkout.domain.Money
import com.fluro.checkout.domain.Sku

class UnitPriceRule(private val prices: Map<Sku, Money>) : PricingRule {
    override fun apply(items: ItemTally): RuleResult {
        var total = Money.ZERO
        val remainingItems = items.toMutableMap()

        val iterator = remainingItems.iterator()
        while (iterator.hasNext()) {
            val (sku, count) = iterator.next()
            prices[sku]?.let { price ->
                total += price * count
                iterator.remove()
            }
        }

        return RuleResult(total, remainingItems)
    }
}
