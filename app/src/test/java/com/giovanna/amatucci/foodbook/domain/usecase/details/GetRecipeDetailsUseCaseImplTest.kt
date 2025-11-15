package com.giovanna.amatucci.foodbook.domain.usecase.details

import com.giovanna.amatucci.foodbook.di.util.ResultWrapper
import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.domain.repository.RecipeRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GetRecipeDetailsUseCaseImplTest {

    @MockK
    lateinit var repository: RecipeRepository

    private lateinit var useCase: GetRecipeDetailsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = GetRecipeDetailsUseCaseImpl(repository)
    }

    @Test
    fun `invoke SHOULD call repository with correct id and RETURN its result`() = runTest {
        val recipeId = "123"
        val mockDetails = mockk<RecipeDetails>()
        val expectedResult = ResultWrapper.Success(mockDetails)

        coEvery { repository.getRecipeDetails(recipeId) } returns expectedResult

        val result = useCase(recipeId)

        assertEquals(expectedResult, result)
        coVerify(exactly = 1) { repository.getRecipeDetails(recipeId) }
    }
}