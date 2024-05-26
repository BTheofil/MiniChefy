package hu.tb.minichefy.presentation.util.icons

import hu.tb.minichefy.R

enum class MealIcon(override val resource: Int): IconResource {
    FRIED_EGG(R.drawable.fried_egg_icon),
    HOT_DOG(R.drawable.hot_dog_icon),
    HOT_SOUP(R.drawable.hotsoup_icon),
    PIZZA(R.drawable.pizza_icon),
    STEAK(R.drawable.steak_icon),
    JUNK_FOOD(R.drawable.junk_food_icon),
    NOODLES(R.drawable.cup_noodles_icon),
    SWEETS(R.drawable.sweets_icon),
}

enum class FoodIcon(override val resource: Int): IconResource {
    APPLE(R.drawable.apple_icon),
    AVOCADO(R.drawable.avocado_icon),
    BREAD(R.drawable.bread_icon),
    CHEESE(R.drawable.cheese_icon),
    CHICKEN_MEAT(R.drawable.chicken_meat_icon),
    CORIANDER(R.drawable.coriander_icon),
    CROISSANT(R.drawable.croissant_icon),
    FISH(R.drawable.fish_icon),
    ICE_CREAM(R.drawable.ice_cream_icon),
    JAPAN_RICE(R.drawable.japan_rice_icon),
    SALAD(R.drawable.leaves_salad_icon),
    LOBSTER(R.drawable.lobster_sea_food_icon),
    MUSHROOM(R.drawable.mushroom_icon),
    ONION(R.drawable.onion_icon),
    PASTA(R.drawable.pasta_icon),
    SUSHI(R.drawable.rice_sushi_icon),
    SALT(R.drawable.salt_icon),
    SAUSAGE(R.drawable.sausage_icon),
    SHRIMP(R.drawable.shrimp_seafood_food_icon)
}

enum class AppWideIcon(override val resource: Int): IconResource {
    DEFAULT_EMPTY_ICON(R.drawable.default_empty_icon),
    GALLERY(R.drawable.gallery_icon)
}