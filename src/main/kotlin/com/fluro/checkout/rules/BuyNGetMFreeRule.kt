package com.fluro.checkout.rules

import com.fluro.checkout.domain.Money
import com.fluro.checkout.domain.Sku

class BuyNGetMFreeRule(
    private val sku: Sku,
    private val buyQuantity: Int,
    private val freeQuantity: Int,
    private val unitPrice: Money,
) : PricingRule {
    override fun evaluate(items: List<Sku>): PriceEffect {
        val matchingItems = items.count { it == sku }
        val bundleSize = buyQuantity + freeQuantity
        val totalBundles = matchingItems / bundleSize

        if (totalBundles == 0) return Discount(Money.ZERO)

        val discountAmount = unitPrice * (totalBundles * freeQuantity)
        return Discount(discountAmount)
    }
}
