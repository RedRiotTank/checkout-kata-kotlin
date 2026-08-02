package com.fluro.checkout.rules

import com.fluro.checkout.domain.Money
import com.fluro.checkout.domain.Sku

class MealDealRule(
    private val itemsToCombine: Set<Sku>,
    private val comboPrice: Money,
    private val regularPrices: Map<Sku, Money>,
) : PricingRule {
    override fun evaluate(items: List<Sku>): PriceEffect {
        if (itemsToCombine.isEmpty()) return Discount(Money.ZERO)

        val numberOfCombos = itemsToCombine.minOf { sku -> items.count { it == sku } }

        if (numberOfCombos == 0) return Discount(Money.ZERO)

        val regularComboTotal =
            itemsToCombine.fold(Money.ZERO) { total, sku ->
                total + (regularPrices[sku] ?: Money.ZERO)
            }

        val discountPerCombo = regularComboTotal.amountInPence - comboPrice.amountInPence

        return Discount(Money(numberOfCombos * discountPerCombo))
    }
}
