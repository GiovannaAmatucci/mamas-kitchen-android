package com.giovanna.amatucci.foodbook.di

import androidx.room.Room
import com.giovanna.amatucci.foodbook.BuildConfig
import com.giovanna.amatucci.foodbook.data.local.db.AppDatabase
import com.giovanna.amatucci.foodbook.data.local.db.CryptographyManager
import com.giovanna.amatucci.foodbook.data.remote.api.AuthApi
import com.giovanna.amatucci.foodbook.data.remote.api.AuthApiImpl
import com.giovanna.amatucci.foodbook.data.remote.api.FatSecretRecipeApi
import com.giovanna.amatucci.foodbook.data.remote.api.FatSecretRecipeApiImpl
import com.giovanna.amatucci.foodbook.data.remote.mapper.RecipeDataMapper
import com.giovanna.amatucci.foodbook.data.remote.network.NetworkHttpClient
import com.giovanna.amatucci.foodbook.data.remote.network.NetworkHttpClientImpl
import com.giovanna.amatucci.foodbook.data.remote.network.TokenHttpClient
import com.giovanna.amatucci.foodbook.data.remote.network.TokenHttpClientImpl
import com.giovanna.amatucci.foodbook.data.repository.AuthRepositoryImpl
import com.giovanna.amatucci.foodbook.data.repository.FavoritesRepositoryImpl
import com.giovanna.amatucci.foodbook.data.repository.RecipeRepositoryImpl
import com.giovanna.amatucci.foodbook.data.repository.SearchRepositoryImpl
import com.giovanna.amatucci.foodbook.data.repository.TokenRepositoryImpl
import com.giovanna.amatucci.foodbook.domain.repository.AuthRepository
import com.giovanna.amatucci.foodbook.domain.repository.FavoritesRepository
import com.giovanna.amatucci.foodbook.domain.repository.RecipeRepository
import com.giovanna.amatucci.foodbook.domain.repository.SearchRepository
import com.giovanna.amatucci.foodbook.domain.repository.TokenRepository
import com.giovanna.amatucci.foodbook.domain.usecase.auth.CheckAuthenticationStatusUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.auth.CheckAuthenticationStatusUseCaseImpl
import com.giovanna.amatucci.foodbook.domain.usecase.auth.FetchAndSaveTokenUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.auth.FetchAndSaveTokenUseCaseImpl
import com.giovanna.amatucci.foodbook.domain.usecase.details.GetRecipeDetailsUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.details.GetRecipeDetailsUseCaseImpl
import com.giovanna.amatucci.foodbook.domain.usecase.favorites.AddFavoritesUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.favorites.AddFavoritesUseCaseImpl
import com.giovanna.amatucci.foodbook.domain.usecase.favorites.DeleteAllFavoritesUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.favorites.DeleteAllFavoritesUseCaseImpl
import com.giovanna.amatucci.foodbook.domain.usecase.favorites.GetFavoritesDetailsUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.favorites.GetFavoritesDetailsUseCaseImpl
import com.giovanna.amatucci.foodbook.domain.usecase.favorites.GetFavoritesUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.favorites.GetFavoritesUseCaseImpl
import com.giovanna.amatucci.foodbook.domain.usecase.favorites.GetRecentFavoritesUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.favorites.GetRecentFavoritesUseCaseImpl
import com.giovanna.amatucci.foodbook.domain.usecase.favorites.IsFavoritesUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.favorites.IsFavoritesUseCaseImpl
import com.giovanna.amatucci.foodbook.domain.usecase.favorites.RemoveFavoritesUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.favorites.RemoveFavoritesUseCaseImpl
import com.giovanna.amatucci.foodbook.domain.usecase.search.ClearSearchHistoryUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.search.ClearSearchHistoryUseCaseImpl
import com.giovanna.amatucci.foodbook.domain.usecase.search.GetSearchQueriesUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.search.GetSearchQueriesUseCaseImpl
import com.giovanna.amatucci.foodbook.domain.usecase.search.SaveSearchQueryUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.search.SaveSearchQueryUseCaseImpl
import com.giovanna.amatucci.foodbook.domain.usecase.search.SearchRecipesUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.search.SearchRecipesUseCaseImpl
import com.giovanna.amatucci.foodbook.presentation.authentication.viewmodel.AuthViewModel
import com.giovanna.amatucci.foodbook.presentation.details.viewmodel.DetailsViewModel
import com.giovanna.amatucci.foodbook.presentation.favorites.viewmodel.FavoritesViewModel
import com.giovanna.amatucci.foodbook.presentation.search.viewmodel.SearchViewModel
import com.giovanna.amatucci.foodbook.util.LogWriter
import com.giovanna.amatucci.foodbook.util.TimberLogWriter
import com.giovanna.amatucci.foodbook.util.constants.KeyStoreConstants
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
val coreModule = module {
    single<LogWriter> { TimberLogWriter() }
}
val networkModule = module {
    single<NetworkHttpClient>(createdAtStart = true) {
        NetworkHttpClientImpl(
            contextNet = androidContext(),
            baseHostUrl = BuildConfig.BASE_URL,
            requestTimeout = BuildConfig.REQUEST_TIMEOUT,
            connectTimeout = BuildConfig.CONNECT_TIMEOUT,
            isDebug = BuildConfig.DEBUG_MODE,
            token = get(),
            auth = get(),
            logWriter = get()
        )
    }
    single<TokenHttpClient> {
        TokenHttpClientImpl(baseUrl = BuildConfig.TOKEN_URL)
    }
    single<AuthApi> { AuthApiImpl(logWriter = get(), get()) }
    single<FatSecretRecipeApi> { FatSecretRecipeApiImpl(client = get(), logWriter = get()) }
    single { RecipeDataMapper() }
}
val databaseModule = module {
    single { CryptographyManager() }
    single(createdAtStart = true) {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            KeyStoreConstants.KEY_STORE_ALIAS
        ).fallbackToDestructiveMigration(true).build()
    }
    single { get<AppDatabase>().accessTokenDao() }
    single { get<AppDatabase>().searchDao() }
    single { get<AppDatabase>().favoriteDao() }
}
val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }
    single<TokenRepository> { TokenRepositoryImpl(get(), get(), get()) }
    single<RecipeRepository> { RecipeRepositoryImpl(get(), get(), get()) }
    single<FavoritesRepository> { FavoritesRepositoryImpl(get(), get(), get()) }
    single<SearchRepository> { SearchRepositoryImpl(get(), get()) }
}
val domainModule = module {
    factory<CheckAuthenticationStatusUseCase> { CheckAuthenticationStatusUseCaseImpl(get()) }
    factory<FetchAndSaveTokenUseCase> { FetchAndSaveTokenUseCaseImpl(get()) }
    factory<GetRecipeDetailsUseCase> { GetRecipeDetailsUseCaseImpl(repository = get()) }
    factory<AddFavoritesUseCase> { AddFavoritesUseCaseImpl(repository = get()) }
    factory<DeleteAllFavoritesUseCase> { DeleteAllFavoritesUseCaseImpl(repository = get()) }
    factory<GetFavoritesDetailsUseCase> { GetFavoritesDetailsUseCaseImpl(repository = get()) }
    factory<GetFavoritesUseCase> { GetFavoritesUseCaseImpl(repository = get()) }
    factory<RemoveFavoritesUseCase> { RemoveFavoritesUseCaseImpl(repository = get()) }
    factory<GetRecentFavoritesUseCase> { GetRecentFavoritesUseCaseImpl(repository = get()) }
    factory<IsFavoritesUseCase> { IsFavoritesUseCaseImpl(repository = get()) }
    factory<SaveSearchQueryUseCase> { SaveSearchQueryUseCaseImpl(repository = get()) }
    factory<GetSearchQueriesUseCase> { GetSearchQueriesUseCaseImpl(repository = get()) }
    factory<SearchRecipesUseCase> { SearchRecipesUseCaseImpl(repository = get()) }
    factory<ClearSearchHistoryUseCase> { ClearSearchHistoryUseCaseImpl(repository = get()) }

}
val viewModelModule = module {
    viewModel { AuthViewModel(get(), get()) }
    viewModel { SearchViewModel(get(), get(), get(), get(), get()) }
    viewModel { DetailsViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { FavoritesViewModel(get(), get()) }
}

val appModules = listOf(
    coreModule, networkModule, databaseModule, repositoryModule, domainModule, viewModelModule
)