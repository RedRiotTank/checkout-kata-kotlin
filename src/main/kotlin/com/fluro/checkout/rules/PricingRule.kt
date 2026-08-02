package com.fluro.checkout.rules

import com.fluro.checkout.domain.Sku

fun interface PricingRule {
    fun evaluate(items: List<Sku>): PriceEffect
}
