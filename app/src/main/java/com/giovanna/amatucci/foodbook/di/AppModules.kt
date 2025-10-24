package com.giovanna.amatucci.foodbook.di

import com.giovanna.amatucci.foodbook.BuildConfig
import com.giovanna.amatucci.foodbook.data.local.db.AppDatabase
import com.giovanna.amatucci.foodbook.data.network.TokenHttpClient
import com.giovanna.amatucci.foodbook.data.network.TokenHttpClientImpl
import com.giovanna.amatucci.foodbook.data.paging.RecipePagingSource
import com.giovanna.amatucci.foodbook.data.remote.api.AuthApi
import com.giovanna.amatucci.foodbook.data.remote.api.AuthApiImpl
import com.giovanna.amatucci.foodbook.data.remote.api.FatSecretRecipeApi
import com.giovanna.amatucci.foodbook.data.remote.api.FatSecretRecipeApiImpl
import com.giovanna.amatucci.foodbook.data.remote.mapper.RecipeDataMapper
import com.giovanna.amatucci.foodbook.data.remote.network.FatSecretApiHttpClient
import com.giovanna.amatucci.foodbook.data.remote.network.FatSecretApiHttpClientImpl
import com.giovanna.amatucci.foodbook.data.repository.AuthRepositoryImpl
import com.giovanna.amatucci.foodbook.data.repository.RecipeRepositoryImpl
import com.giovanna.amatucci.foodbook.data.repository.TokenRepositoryImpl
import com.giovanna.amatucci.foodbook.domain.repository.AuthRepository
import com.giovanna.amatucci.foodbook.domain.repository.RecipeRepository
import com.giovanna.amatucci.foodbook.domain.repository.TokenRepository
import com.giovanna.amatucci.foodbook.domain.usecase.GetRecipeDetailsUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.GetRecipeDetailsUseCaseImpl
import com.giovanna.amatucci.foodbook.domain.usecase.SaveSearchQueryUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.SaveSearchQueryUseCaseImpl
import com.giovanna.amatucci.foodbook.domain.usecase.SearchRecipesUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.SearchRecipesUseCaseImpl
import com.giovanna.amatucci.foodbook.presentation.authentication.AuthViewModel
import com.giovanna.amatucci.foodbook.presentation.details.DetailsViewModel
import com.giovanna.amatucci.foodbook.presentation.search.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val dataModule = module {
    single<FatSecretApiHttpClient> {
        FatSecretApiHttpClientImpl(
            baseHostUrl = BuildConfig.BASE_URL,
            requestTimeout = BuildConfig.REQUEST_TIMEOUT,
            connectTimeout = BuildConfig.CONNECT_TIMEOUT,
            isDebug = BuildConfig.DEBUG_MODE,
            authApi = get(),
            tokenRepository = get()
        )
    }
    single<TokenHttpClient> {
        TokenHttpClientImpl(
            baseHostUrl = BuildConfig.TOKEN_URL,
            requestTimeout = BuildConfig.REQUEST_TIMEOUT,
            connectTimeout = BuildConfig.CONNECT_TIMEOUT,
            isDebug = BuildConfig.DEBUG_MODE
        )
    }
    single { RecipeDataMapper() }
    single { AppDatabase.getDatabase(androidContext()) }
    single { get<AppDatabase>().tokenDao() }
    single { get<AppDatabase>().searchDao() }
    single<AuthApi> {
        AuthApiImpl(client = get())
    }
    single<FatSecretRecipeApi> {
        FatSecretRecipeApiImpl(client = get())
    }
    single<TokenRepository> { TokenRepositoryImpl(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<RecipeRepository> { RecipeRepositoryImpl(get(), get(), get()) }
}

val domainModule = module {
    factory<SearchRecipesUseCase> { SearchRecipesUseCaseImpl(repository = get()) }
    factory<GetRecipeDetailsUseCase> { GetRecipeDetailsUseCaseImpl(repository = get()) }
    factory<SaveSearchQueryUseCase> { SaveSearchQueryUseCaseImpl(repository = get()) }
}
val viewModelModule = module {
    viewModel { AuthViewModel(get(), get()) }
    viewModel { SearchViewModel(get(), get()) }
    viewModel { DetailsViewModel(get(), get()) }

}

val appModules = listOf(dataModule, domainModule, viewModelModule)