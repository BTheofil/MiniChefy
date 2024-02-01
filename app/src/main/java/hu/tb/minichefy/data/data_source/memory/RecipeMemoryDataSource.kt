package hu.tb.minichefy.data.data_source.memory

import hu.tb.minichefy.domain.model.Recipe
import hu.tb.minichefy.domain.model.RecipeStep
import hu.tb.minichefy.presentation.screens.recipe.components.FoodIcon

class RecipeMemoryDataSource : RecipeDataSource {

    private val mockDataSource = listOf(
        Recipe(0, FoodIcon.STEAK.resource,"alma", 2, listOf(RecipeStep(0, "one"), RecipeStep(1, "two"))),
        Recipe(1, FoodIcon.STEAK.resource,"banan", 5, listOf(RecipeStep(0, "one"))),
        Recipe(2, FoodIcon.STEAK.resource,"citrom", 3, listOf(RecipeStep(0, "one"))),
        Recipe(3, FoodIcon.STEAK.resource,"dio", 1, listOf(RecipeStep(0, "one")))
    )

    override fun getAllRecipe(): List<Recipe> =
        mockDataSource


    override fun getRecipeById(id: Long): Recipe =
        mockDataSource.find { recipe -> recipe.id == id }!!

}