package com.fluro.checkout

import com.fluro.checkout.domain.Money
import com.fluro.checkout.domain.Sku
import com.fluro.checkout.rules.UnitPriceRule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CheckoutTest {
    private lateinit var checkout: Checkout

    @BeforeEach
    fun setUp() {
        val unitPricingRule =
            UnitPriceRule(
                mapOf(
                    Sku("A") to Money(50),
                    Sku("B") to Money(75),
                ),
            )
        checkout = Checkout(pricingRules = listOf(unitPricingRule))
    }

    @Test
    fun `scanning a single product returns its base price`() {
        checkout.scan(Sku("A"))
        assertEquals(Money(50), checkout.total())
    }

    @Test
    fun `scanning multiple products accumulates total base price regardless of order`() {
        checkout.scan(Sku("B"))
        checkout.scan(Sku("A"))
        assertEquals(Money(125), checkout.total())
    }

    @Test
    fun `scanning nothing returns 0p`() {
        assertEquals(Money(0), checkout.total())
    }
}
