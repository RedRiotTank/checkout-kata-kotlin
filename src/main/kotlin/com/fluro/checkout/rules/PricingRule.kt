package com.fluro.checkout.rules

import com.fluro.checkout.domain.Money
import com.fluro.checkout.domain.Sku

typealias ItemTally = Map<Sku, Int>

data class RuleResult(
    val price: Money,
    val remainingItems: ItemTally,
)

fun interface PricingRule {
    fun apply(items: ItemTally): RuleResult
}
