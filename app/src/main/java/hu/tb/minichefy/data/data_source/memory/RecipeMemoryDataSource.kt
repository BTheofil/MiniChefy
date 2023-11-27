package hu.tb.minichefy.data.data_source.memory

import hu.tb.minichefy.domain.model.Recipe
import hu.tb.minichefy.domain.model.RecipeStep

class RecipeMemoryDataSource : RecipeDataSource {

    private val mockDataSouce = listOf(
        Recipe(0, "alma", 2, listOf(RecipeStep(0, "one"), RecipeStep(1, "two"))),
        Recipe(1, "banan", 5, listOf(RecipeStep(0, "one"))),
        Recipe(2, "citrom", 3, listOf(RecipeStep(0, "one"))),
        Recipe(3, "dio", 1, listOf(RecipeStep(0, "one")))
    )

    override fun getAllRecipe(): List<Recipe> =
        mockDataSouce


    override fun getRecipeById(id: Int): Recipe =
        mockDataSouce.find { recipe -> recipe.id == id }!!

}