package com.example.calorietracker.values

class ServingSizes {
    companion object {
        val servingAmountSingular: List<String> = listOf(
            "serving",
            "jar",
            "piece",
            "can",
            "bottle",
            "container",
        )

        val servingAmountPlural: List<String> = listOf(
            "servings",
            "jars",
            "pieces",
            "cans",
            "bottles",
            "containers"
        )

        val servingWeightUnitsPlural: List<String> = listOf(
            "grams",
            "kilograms",
            "milligrams",
            "ounces",
            "pounds",
        )

        val servingWeightUnitsSingular: List<String> = listOf(
            "gram",
            "kilogram",
            "milligram",
            "ounce",
            "pound",
        )

        val servingVolumeUnitsPlural: List<String> = listOf(
            "milliliters",
            "liters",
            "cups",
            "pints",
            "quarts",
            "gallons",
            "tablespoons",
            "teaspoons",
        )

        val servingVolumeUnitsSingular: List<String> = listOf(
            "milliliter",
            "liter",
            "cup",
            "pint",
            "quart",
            "gallon",
            "tablespoon",
            "teaspoon",
        )
    }

}