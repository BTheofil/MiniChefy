package hu.tb.minichefy.presentation.screens.manager.icons

import hu.tb.minichefy.R

enum class MealIcon(override val resource: Int): IconResource {
    FRIED_EGG(R.drawable.fried_egg_icon),
    HOT_SOUP(R.drawable.hotsoup_icon),
    PIZZA(R.drawable.pizza_icon),
    STEAK(R.drawable.steak_icon),
    SWEETS(R.drawable.sweets_icon),
    JUNK_FOOD(R.drawable.junk_food_icon),
}

enum class ProductIcon(override val resource: Int): IconResource {
    APPLE(R.drawable.apple_icon),
    AVOCADO(R.drawable.avocado_icon),
    BREAD(R.drawable.bread_icon),
    CHEESE(R.drawable.cheese_icon)
}

enum class AppWideIcon(override val resource: Int): IconResource {
    DEFAULT_EMPTY_ICON(R.drawable.default_empty_icon),
}