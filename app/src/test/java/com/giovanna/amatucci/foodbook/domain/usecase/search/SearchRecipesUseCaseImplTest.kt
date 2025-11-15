package com.giovanna.amatucci.foodbook.domain.usecase.search

import androidx.paging.PagingData
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.domain.repository.RecipeRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class SearchRecipesUseCaseImplTest {
    @MockK
    lateinit var recipeRepository: RecipeRepository

    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private lateinit var searchRecipesUseCase: SearchRecipesUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        searchRecipesUseCase = SearchRecipesUseCaseImpl(recipeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `SearchRecipesUseCase SHOULD call repository with trimmed query`() = runTest {
        val query = "  chicken  "
        val trimmedQuery = "chicken"
        val pagingData = PagingData.empty<RecipeItem>()
        val expectedFlow = flowOf(pagingData)
        coEvery { recipeRepository.searchRecipesPaginated(trimmedQuery) } returns expectedFlow

        val resultFlow = searchRecipesUseCase(query)

        assertEquals(expectedFlow, resultFlow)
        coVerify(exactly = 1) { recipeRepository.searchRecipesPaginated(trimmedQuery) }
    }
}