# Supermarket Checkout Kata

A fully tested Kotlin implementation of the Checkout Kata, built with a focus on code quality, immutability, and SOLID principles.

## 📋 Prerequisites

* **Java JDK 17 or higher** (tested with Java 21). 
* Make sure your `JAVA_HOME` environment variable is properly configured.

## 🚀 How to Run

The project uses the Gradle wrapper, so you don't need to install anything globally. You can run the tests directly from your terminal:

```bash
# Run the complete test suite (Unit & Integration tests)
./gradlew test
```

## 🏗️ Design Highlights

Although this is a small kata, I treated the code quality as I would for production software:

- **Extra Points (Dynamic Pricing):** Fulfilled the requirement to pass pricing rules dynamically by injecting a List<PricingRule> into the Checkout constructor.

- **Architecture (Consumer Pipeline):** Instead of calculating a total and subtracting discounts (which can cause rule collisions), rules act as a pipeline. Each rule "consumes" the items it prices and passes the remainder to the next rule.

- **Domain Types:** Used Value Classes (like Sku) and Domain Objects (like Money in pence) to avoid primitive obsession and ensure type safety.

- **Extensibility:** The Checkout class is completely closed to modification (Open/Closed Principle). New promotions can be added simply by creating a new PricingRule.

## 🤖 AI Usage Note

As encouraged in the assignment, I used AI tooling (LLMs) during the development. I used it primarily as a "sounding board" to iterate on my initial architecture, moving from a basic discount-based model to the more robust Consumer Pipeline pattern, and to refine the idiomatic Kotlin syntax (e.g., using fold and groupingBy). I'll be happy to walk through this iteration process in our interview!
