package com.zioanacleto.speakeazy.core.data.create.datamappers

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.buffa.default
import com.zioanacleto.speakeazy.core.data.create.dto.CreateCocktailRequestDTO
import com.zioanacleto.speakeazy.core.data.create.dto.TagsRequestDTO
import com.zioanacleto.speakeazy.core.data.main.dto.MainSpeakEazyBEIngredientDTO
import com.zioanacleto.speakeazy.core.data.main.dto.MainSpeakEazyBEIngredientsListDTO
import com.zioanacleto.speakeazy.core.domain.create.model.CreateCocktailModel

class CreateCocktailDataMapper : DataMapper<CreateCocktailModel, CreateCocktailRequestDTO> {
    override fun mapInto(input: CreateCocktailModel): CreateCocktailRequestDTO {
        return CreateCocktailRequestDTO(
            id = "1000", // BE will autoincrement it correctly
            name = input.cocktailName.default(),
            category = SPEAKEAZY_CATEGORY, // using dedicated value
            instructions = listOf(
                MainSpeakEazyBEInstructionDTO(
                    type = "ingredient",
                    instruction = input.instructions.default()
                )
            ),
            instructionsIt = listOf(
                MainSpeakEazyBEInstructionDTO(
                    type = "ingredient",
                    instruction = input.instructionsIt.default()
                )
            ),
            glass = input.glass.default(),
            isAlcoholic = input.isAlcoholic,
            // todo: upload a default image for now
            imageLink = input.imageUrl.default(DEFAULT_IMAGE_LINK),
            type = input.type.default(),
            method = input.method.default(),
            ingredients = MainSpeakEazyBEIngredientsListDTO(
                ingredients = input.ingredients.map { entry ->
                    val (measureValue, measure) = entry.value.split(" ").also {
                        it.first() to it.last()
                    }
                    MainSpeakEazyBEIngredientDTO(
                        id = entry.key,
                        quantityCl = if (measure == "cl") measureValue else "-",
                        quantityOz = if (measure == "oz") measureValue else "-",
                        quantitySpecial = if (measure != "cl" && measure != "oz") entry.value else null
                    )
                }
            ),
            visualizations = 1,
            tags = TagsRequestDTO(listOf()),
            userId = input.userId,
            username = input.username
        )
    }

    companion object {
        private const val SPEAKEAZY_CATEGORY = "SpeakEazy Original"
        private const val DEFAULT_IMAGE_LINK =
            "https://res.cloudinary.com/dyhi3yudn/image/upload/v1743712987/mojito.png"
    }
}