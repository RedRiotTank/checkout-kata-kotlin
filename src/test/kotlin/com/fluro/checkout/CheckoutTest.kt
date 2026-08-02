package com.fluro.checkout

import com.fluro.checkout.domain.Money
import com.fluro.checkout.domain.Sku
import com.fluro.checkout.rules.BuyNGetMFreeRule
import com.fluro.checkout.rules.MealDealRule
import com.fluro.checkout.rules.MultipricedRule
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

    @Test
    fun `scanning three units of Sku A applies multipriced promotion`() {
        val multipricedRule = MultipricedRule(Sku("A"), 3, Money(130))
        val unitRule = UnitPriceRule(mapOf(Sku("A") to Money(50)))

        checkout = Checkout(pricingRules = listOf(multipricedRule, unitRule))

        checkout.scan(Sku("A"))
        checkout.scan(Sku("A"))
        checkout.scan(Sku("A"))

        assertEquals(Money(130), checkout.total())
    }

    @Test
    fun `scanning mix of items with multipriced promotion and standard items`() {
        val multipricedRule = MultipricedRule(Sku("A"), 3, Money(130))
        val unitRule =
            UnitPriceRule(
                mapOf(
                    Sku("A") to Money(50),
                    Sku("B") to Money(75),
                ),
            )

        checkout = Checkout(pricingRules = listOf(multipricedRule, unitRule))

        checkout.scan(Sku("A"))
        checkout.scan(Sku("A"))
        checkout.scan(Sku("A"))
        checkout.scan(Sku("B"))

        assertEquals(Money(205), checkout.total())
    }

    @Test
    fun `scanning items applies buy N get M free promotion`() {
        val bngmRule = BuyNGetMFreeRule(Sku("C"), buyQuantity = 3, freeQuantity = 1, unitPrice = Money(25))
        val unitRule = UnitPriceRule(mapOf(Sku("C") to Money(25)))

        checkout = Checkout(pricingRules = listOf(bngmRule, unitRule))

        checkout.scan(Sku("C"))
        checkout.scan(Sku("C"))
        checkout.scan(Sku("C"))
        checkout.scan(Sku("C"))

        assertEquals(Money(75), checkout.total())
    }

    @Test
    fun `scanning items included in a meal deal applies special combination price`() {
        val mealDealRule =
            MealDealRule(
                itemsToCombine = setOf(Sku("D"), Sku("E")),
                comboPrice = Money(300),
            )
        val unitRule =
            UnitPriceRule(
                mapOf(
                    Sku("D") to Money(150),
                    Sku("E") to Money(200),
                ),
            )

        checkout = Checkout(pricingRules = listOf(mealDealRule, unitRule))

        checkout.scan(Sku("D"))
        checkout.scan(Sku("E"))

        assertEquals(Money(300), checkout.total())
    }

    @Test
    fun `meal deal rule leaves uncombined items for unit price rule`() {
        val mealDealRule =
            MealDealRule(
                itemsToCombine = setOf(Sku("D"), Sku("E")),
                comboPrice = Money(300),
            )
        val unitRule =
            UnitPriceRule(
                mapOf(
                    Sku("D") to Money(150),
                    Sku("E") to Money(200),
                ),
            )

        checkout = Checkout(pricingRules = listOf(mealDealRule, unitRule))

        checkout.scan(Sku("D"))
        checkout.scan(Sku("D"))
        checkout.scan(Sku("E"))

        assertEquals(Money(450), checkout.total())
    }
}
