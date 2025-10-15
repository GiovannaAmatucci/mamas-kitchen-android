package com.giovanna.amatucci.foodbook.di

import com.giovanna.amatucci.foodbook.BuildConfig
import com.giovanna.amatucci.foodbook.data.api.SpoonacularApiService
import com.giovanna.amatucci.foodbook.data.network.KtorClient
import com.giovanna.amatucci.foodbook.data.network.KtorClientImpl
import com.giovanna.amatucci.foodbook.data.repository.RecipeRepositoryImpl
import com.giovanna.amatucci.foodbook.domain.repository.RecipeRepository
import com.giovanna.amatucci.foodbook.domain.usecase.GetRecipeDetailsUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.GetRecipeDetailsUseCaseImpl
import com.giovanna.amatucci.foodbook.domain.usecase.SearchRecipesUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.SearchRecipesUseCaseImpl
import com.giovanna.amatucci.foodbook.presentation.details.DetailsViewModel
import com.giovanna.amatucci.foodbook.presentation.search.SearchViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val dataModule = module {
    single { SpoonacularApiService(client = get()) }
    single<KtorClient> {
        KtorClientImpl(
            baseHostUrl = BuildConfig.BASE_URL,
            requestTimeout = BuildConfig.REQUEST_TIMEOUT,
            connectTimeout = BuildConfig.CONNECT_TIMEOUT,
            isDebug = BuildConfig.DEBUG_MODE
        )
    }
    single { Dispatchers.IO }
    single<RecipeRepository> { RecipeRepositoryImpl(apiService = get(), ioDispatcher = get()) }
}

val domainModule = module {
    factory<SearchRecipesUseCase> { SearchRecipesUseCaseImpl(repository = get()) }
    factory<GetRecipeDetailsUseCase> { GetRecipeDetailsUseCaseImpl(repository = get()) }
}
val viewModelModule = module {
    viewModel { SearchViewModel(get()) }
    viewModel { params -> DetailsViewModel(get(), recipeId = params.get()) }
}

val appModules = listOf(dataModule, domainModule, viewModelModule)