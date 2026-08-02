package com.fluro.checkout.rules

import com.fluro.checkout.domain.Money
import com.fluro.checkout.domain.Sku

class MultipricedRule(
    private val sku: Sku,
    private val quantityForOffer: Int,
    private val offerPrice: Money,
) : PricingRule {
    override fun apply(items: ItemTally): RuleResult {
        val count = items[sku] ?: 0
        val bundles = count / quantityForOffer

        if (bundles == 0) return RuleResult(Money.ZERO, items)

        val price = offerPrice * bundles
        val remainingCount = count % quantityForOffer

        val remainingItems = items.toMutableMap()
        if (remainingCount == 0) {
            remainingItems.remove(sku)
        } else {
            remainingItems[sku] = remainingCount
        }

        return RuleResult(price, remainingItems)
    }
}
