package com.zioanacleto.speakeazy.ui.presentation.create.data.datamappers

import com.zioanacleto.buffa.datamappers.DataMapper
import com.zioanacleto.buffa.default
import com.zioanacleto.speakeazy.ui.presentation.create.data.dto.CreateCocktailRequestDTO
import com.zioanacleto.speakeazy.ui.presentation.create.data.dto.TagsRequestDTO
import com.zioanacleto.speakeazy.ui.presentation.create.domain.model.CreateCocktailModel
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.MainSpeakEazyBEIngredientDTO
import com.zioanacleto.speakeazy.ui.presentation.main.data.dto.MainSpeakEazyBEIngredientsListDTO
import kotlinx.serialization.json.Json

class CreateCocktailDataMapper : DataMapper<CreateCocktailModel, CreateCocktailRequestDTO> {
    override fun mapInto(input: CreateCocktailModel): CreateCocktailRequestDTO {
        return CreateCocktailRequestDTO(
            id = "1000", // BE will autoincrement it correctly
            name = input.cocktailName.default(),
            category = "SpeakEazy Original", // using dedicated value
            instructions = input.instructions.default(),
            instructionsIt = input.instructionsIt.default(),
            glass = input.glass.default(),
            isAlcoholic = input.isAlcoholic,
            imageLink = input.imageUrl.default(),
            type = input.type.default(),
            method = input.method.default(),
            ingredients = MainSpeakEazyBEIngredientsListDTO(
                ingredients = input.ingredients.map { entry ->
                    val (measureValue , measure)  = entry.value.split(" ").also {
                        it.first() to it.last()
                    }
                    MainSpeakEazyBEIngredientDTO(
                        id = entry.key,
                        quantityCl = if(measure == "cl") measureValue else "",
                        quantityOz = if(measure == "oz") measureValue else "",
                        quantitySpecial = if(measure != "cl" && measure != "oz") measureValue else ""
                    )
                }
            ),
            visualizations = 1,
            tags = TagsRequestDTO(listOf()),
            userId = input.userId,
            username = input.username
        )
    }
}