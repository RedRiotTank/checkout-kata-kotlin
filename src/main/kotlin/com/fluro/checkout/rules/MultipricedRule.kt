package com.fluro.checkout.rules

import com.fluro.checkout.domain.Money
import com.fluro.checkout.domain.Sku

class MultipricedRule(
    private val sku: Sku,
    private val quantityForOffer: Int,
    private val offerPrice: Money,
    private val unitPrice: Money,
) : PricingRule {
    override fun evaluate(items: List<Sku>): PriceEffect {
        val matchingItems = items.count { it == sku }
        val offerBundles = matchingItems / quantityForOffer

        if (offerBundles == 0) return Discount(Money.ZERO)

        val normalPriceForBundles = unitPrice * (offerBundles * quantityForOffer)
        val specialPriceForBundles = offerPrice * offerBundles

        val discount = normalPriceForBundles.amountInPence - specialPriceForBundles.amountInPence
        return Discount(Money(discount))
    }
}
