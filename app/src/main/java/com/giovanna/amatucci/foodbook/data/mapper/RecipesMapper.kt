package com.giovanna.amatucci.foodbook.data.mapper

import android.text.Html
import com.giovanna.amatucci.foodbook.data.model.RecipeInformationDto
import com.giovanna.amatucci.foodbook.data.model.RecipeSearchResultDto
import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.domain.model.RecipeSummary

fun RecipeSearchResultDto.toDomain(): RecipeSummary {
    return RecipeSummary(
        id = this.id,
        title = this.title,
        imageUrl = this.image
    )
}

fun RecipeInformationDto.toDomain(): RecipeDetails {
    val ingredientList = this.ingredients.map { it.original }
    val instructionList = this.instructions
        .firstOrNull()
        ?.steps
        ?.map { stepDto -> "${stepDto.number}. ${stepDto.step}" }
        ?: emptyList()

    val summaryCleaned = if (this.summary.isNotBlank()) {
        Html.fromHtml(this.summary, Html.FROM_HTML_MODE_COMPACT).toString()
    } else {
        ""
    }

    return RecipeDetails(
        id = this.id,
        title = this.title,
        imageUrl = this.image,
        summary = summaryCleaned,
        servings = this.servings,
        readyInMinutes = this.readyInMinutes,
        ingredients = ingredientList,
        instructions = instructionList
    )
}