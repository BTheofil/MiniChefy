package hu.tb.minichefy.presentation.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import hu.tb.minichefy.domain.model.recipe.Recipe
import hu.tb.minichefy.domain.model.recipe.RecipeIngredient
import hu.tb.minichefy.domain.model.recipe.RecipeStep
import hu.tb.minichefy.domain.model.recipe.TimeUnit
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement
import hu.tb.minichefy.presentation.screens.manager.icons.IconManager

class RecipePreviewParameterProvider : PreviewParameterProvider<Recipe> {
    override val values: Sequence<Recipe> = sequenceOf(
        MockRecipeDomain.mockRecipe
    )
}

private object MockRecipeDomain {

    val mockRecipe = Recipe(
        id = 0,
        icon = IconManager().getRandomFood().resource,
        title = "Apple pie",
        quantity = 4,
        howToSteps = listOf(
            RecipeStep(0, "make dao"),
            RecipeStep(1, "put the oven"),
            RecipeStep(2, "enjoy")
        ),
        timeUnit = TimeUnit.MINUTES,
        timeToCreate = 30,
        ingredientList = listOf(
            RecipeIngredient(
                0,
                "pasta",
                1f,
                UnitOfMeasurement.KG
            ),
            RecipeIngredient(
                0,
                "apple",
                2f,
                UnitOfMeasurement.DKG
            ),
        )
    )
}