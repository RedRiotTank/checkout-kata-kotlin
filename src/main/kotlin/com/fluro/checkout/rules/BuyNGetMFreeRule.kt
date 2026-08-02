package com.fluro.checkout.rules

import com.fluro.checkout.domain.Money
import com.fluro.checkout.domain.Sku

class BuyNGetMFreeRule(
    private val sku: Sku,
    private val buyQuantity: Int,
    private val freeQuantity: Int,
    private val unitPrice: Money,
) : PricingRule {
    override fun apply(items: ItemTally): RuleResult {
        val count = items[sku] ?: 0
        val bundleSize = buyQuantity + freeQuantity
        val bundles = count / bundleSize

        if (bundles == 0) return RuleResult(Money.ZERO, items)

        val price = unitPrice * (bundles * buyQuantity)
        val remainingCount = count % bundleSize

        val remainingItems = items.toMutableMap()
        if (remainingCount == 0) {
            remainingItems.remove(sku)
        } else {
            remainingItems[sku] = remainingCount
        }

        return RuleResult(price, remainingItems)
    }
}
