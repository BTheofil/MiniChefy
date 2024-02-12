package hu.tb.minichefy.presentation.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import hu.tb.minichefy.domain.model.recipe.Recipe
import hu.tb.minichefy.domain.model.recipe.RecipeStep

class RecipePreviewParameterProvider : PreviewParameterProvider<Recipe> {
    override val values: Sequence<Recipe> = sequenceOf(
        MockRecipeDomain.mockRecipe
    )
}

private object MockRecipeDomain {

    val mockRecipe = Recipe(
        0,
        icon = 0,
        "Apple pie",
        4,
        listOf(
            RecipeStep(0, "make dao"),
            RecipeStep(1, "put the oven"),
            RecipeStep(2, "enjoy")
        )
    )
}