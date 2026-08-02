package com.fluro.checkout

import com.fluro.checkout.domain.Money
import com.fluro.checkout.domain.Sku
import com.fluro.checkout.rules.BuyNGetMFreeRule
import com.fluro.checkout.rules.MealDealRule
import com.fluro.checkout.rules.MultipricedRule
import com.fluro.checkout.rules.PricingRule
import com.fluro.checkout.rules.UnitPriceRule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CheckoutIntegrationTest {
    private lateinit var checkout: Checkout

    @BeforeEach
    fun setUp() {
        // B: 2 for £1.25 (125p)
        val multipricedBRule = MultipricedRule(Sku("B"), quantityForOffer = 2, offerPrice = Money(125))

        // C: Buy 3, get one free
        val buy3Get1FreeCRule =
            BuyNGetMFreeRule(
                sku = Sku("C"),
                buyQuantity = 3,
                freeQuantity = 1,
                unitPrice = Money(25),
            )

        // D and E: Buy D and E for £3 (300p)
        val mealDealDERule =
            MealDealRule(
                itemsToCombine = setOf(Sku("D"), Sku("E")),
                comboPrice = Money(300),
            )

        // Base prices for all items
        val basePricesRule =
            UnitPriceRule(
                mapOf(
                    Sku("A") to Money(50),
                    Sku("B") to Money(75),
                    Sku("C") to Money(25),
                    Sku("D") to Money(150),
                    Sku("E") to Money(200),
                ),
            )

        val fullRuleset: List<PricingRule> =
            listOf(
                multipricedBRule,
                buy3Get1FreeCRule,
                mealDealDERule,
                basePricesRule,
            )

        checkout = Checkout(fullRuleset)
    }

    @Test
    fun `calculates empty basket correctly`() {
        assertEquals(Money(0), checkout.total())
    }

    @Test
    fun `calculates basket with no promotions applied`() {
        // 1 A (50), 1 B (75), 2 C (50) -> Total: 175p
        listOf("A", "B", "C", "C").forEach { checkout.scan(Sku(it)) }

        assertEquals(Money(175), checkout.total())
    }

    @Test
    fun `calculates basket applying multipriced B promotion`() {
        // 2 B (125), 1 A (50) -> Total: 175p
        listOf("B", "B", "A").forEach { checkout.scan(Sku(it)) }

        assertEquals(Money(175), checkout.total())
    }

    @Test
    fun `calculates basket applying buy 3 get 1 free C promotion`() {
        // 4 C -> Pay 3 (75p)
        listOf("C", "C", "C", "C").forEach { checkout.scan(Sku(it)) }

        assertEquals(Money(75), checkout.total())
    }

    @Test
    fun `calculates basket applying meal deal D and E promotion`() {
        // 1 D + 1 E -> Combo (300p)
        listOf("D", "E").forEach { checkout.scan(Sku(it)) }

        assertEquals(Money(300), checkout.total())
    }

    @Test
    fun `calculates complex basket with all rules triggering and leaving leftovers`() {
        /*
         * - 1 A = 50p (Base price)
         * - 3 B = 125p (Prom 2xB) + 75p (1xB) = 200p
         * - 5 C = 75p (Prom 4xC) + 25p (1xC) = 100p
         * - 2 D, 1 E = 300p (Comb D+E) + 150p (1xD) = 450p
         *
         * Expected total: 50 + 200 + 100 + 450 = 800p (£8.00)
         */

        val items =
            listOf(
                "A",
                "B",
                "B",
                "B",
                "C",
                "C",
                "C",
                "C",
                "C",
                "D",
                "D",
                "E",
            )

        items.shuffled().forEach { checkout.scan(Sku(it)) }

        assertEquals(Money(800), checkout.total())
    }
}
