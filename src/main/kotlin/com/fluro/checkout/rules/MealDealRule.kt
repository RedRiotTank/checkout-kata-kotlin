package com.fluro.checkout.rules

import com.fluro.checkout.domain.Money
import com.fluro.checkout.domain.Sku

class MealDealRule(
    private val itemsToCombine: Set<Sku>,
    private val comboPrice: Money,
) : PricingRule {
    override fun apply(items: ItemTally): RuleResult {
        val combos = itemsToCombine.minOfOrNull { items[it] ?: 0 } ?: 0

        if (combos == 0) return RuleResult(Money.ZERO, items)

        val price = comboPrice * combos
        val remainingItems = items.toMutableMap()

        itemsToCombine.forEach { sku ->
            val currentCount = remainingItems[sku] ?: 0
            val newCount = currentCount - combos
            if (newCount <= 0) {
                remainingItems.remove(sku)
            } else {
                remainingItems[sku] = newCount
            }
        }

        return RuleResult(price, remainingItems)
    }
}
