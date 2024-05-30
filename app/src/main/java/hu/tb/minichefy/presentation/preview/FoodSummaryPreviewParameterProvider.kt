package hu.tb.minichefy.presentation.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import hu.tb.minichefy.domain.model.storage.FoodSummary
import hu.tb.minichefy.domain.model.storage.UnitOfMeasurement

class FoodSummaryPreviewParameterProvider : PreviewParameterProvider<List<FoodSummary>> {

    override val values: Sequence<List<FoodSummary>> = sequenceOf(
        MockFoodSummaryDomain.mockFoodSummaryList
    )
}

object MockFoodSummaryDomain {

    val mockFoodSummaryList = listOf(
        FoodSummary(
            id = 1,
            title = "Apple",
            unitOfMeasurement = UnitOfMeasurement.PIECE
        ),
        FoodSummary(
            id = 2,
            title = "Banana",
            unitOfMeasurement = UnitOfMeasurement.PIECE
        )
    )
}