package com.giovanna.amatucci.foodbook.di

import com.giovanna.amatucci.foodbook.BuildConfig
import com.giovanna.amatucci.foodbook.data.local.db.AppDatabase
import com.giovanna.amatucci.foodbook.data.local.db.CryptoManager
import com.giovanna.amatucci.foodbook.data.local.ds.KeyDataStore
import com.giovanna.amatucci.foodbook.data.local.ds.KeyStorage
import com.giovanna.amatucci.foodbook.data.local.ds.TokenDataStore
import com.giovanna.amatucci.foodbook.data.local.ds.TokenStorage
import com.giovanna.amatucci.foodbook.data.remote.api.AuthApi
import com.giovanna.amatucci.foodbook.data.remote.api.AuthApiImpl
import com.giovanna.amatucci.foodbook.data.remote.api.FatSecretRecipeApi
import com.giovanna.amatucci.foodbook.data.remote.api.FatSecretRecipeApiImpl
import com.giovanna.amatucci.foodbook.data.remote.mapper.RecipeDataMapper
import com.giovanna.amatucci.foodbook.data.remote.network.NetworkHttpClient
import com.giovanna.amatucci.foodbook.data.remote.network.NetworkHttpClientImpl
import com.giovanna.amatucci.foodbook.data.repository.AuthRepositoryImpl
import com.giovanna.amatucci.foodbook.data.repository.RecipeRepositoryImpl
import com.giovanna.amatucci.foodbook.data.repository.TokenRepositoryImpl
import com.giovanna.amatucci.foodbook.di.util.LogWriter
import com.giovanna.amatucci.foodbook.di.util.TimberLogWriter
import com.giovanna.amatucci.foodbook.domain.repository.AuthRepository
import com.giovanna.amatucci.foodbook.domain.repository.RecipeRepository
import com.giovanna.amatucci.foodbook.domain.repository.TokenRepository
import com.giovanna.amatucci.foodbook.domain.usecase.CheckAuthenticationStatusUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.CheckAuthenticationStatusUseCaseImpl
import com.giovanna.amatucci.foodbook.domain.usecase.FetchAndSaveTokenUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.FetchAndSaveTokenUseCaseImpl
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


/**
 * Módulo para utilitários e serviços centrais da aplicação.
 */
val coreModule = module {
    single<LogWriter> { TimberLogWriter() }
}

/**
 * Módulo para todas as dependências relacionadas à rede (Ktor, APIs, Mappers).
 */
val networkModule = module {
    single<NetworkHttpClient> {
        NetworkHttpClientImpl(
            baseHostUrl = BuildConfig.BASE_URL,
            requestTimeout = BuildConfig.REQUEST_TIMEOUT,
            connectTimeout = BuildConfig.CONNECT_TIMEOUT,
            isDebug = BuildConfig.DEBUG_MODE, token = get(), auth = get(), logWriter = get()
        )
    }
    single<AuthApi> { AuthApiImpl(logWriter = get()) }
    single<FatSecretRecipeApi> { FatSecretRecipeApiImpl(client = get(), logWriter = get()) }
    single { RecipeDataMapper() }
}

/**
 * Módulo para todas as dependências de armazenamento local (Room, DataStore, Crypto).
 */
val databaseModule = module {
    single<KeyStorage> { KeyDataStore(androidContext()) }
    single<TokenStorage> { TokenDataStore(androidContext()) }
    single { CryptoManager(get()) }
    single {
        AppDatabase.getDatabase(
            context = androidContext(), cryptoManager = get()
        )
    }
    single { get<AppDatabase>().searchDao() }
}

/**
 * Módulo que liga as interfaces dos repositórios às suas implementações.
 */
val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }
    single<TokenRepository> { TokenRepositoryImpl(get()) }
    single<RecipeRepository> { RecipeRepositoryImpl(get(), get(), get(), get()) }
}

/**
 * Módulo para os Casos de Uso (UseCases) da camada de domínio.
 */
val domainModule = module {
    factory<CheckAuthenticationStatusUseCase> { CheckAuthenticationStatusUseCaseImpl(get()) }
    factory<FetchAndSaveTokenUseCase> { FetchAndSaveTokenUseCaseImpl(get()) }
    factory<SearchRecipesUseCase> { SearchRecipesUseCaseImpl(repository = get()) }
    factory<GetRecipeDetailsUseCase> { GetRecipeDetailsUseCaseImpl(repository = get()) }
    factory<SaveSearchQueryUseCase> { SaveSearchQueryUseCaseImpl(repository = get()) }
}

/**
 * Módulo para os ViewModels da camada de apresentação.
 */
val viewModelModule = module {
    viewModel { AuthViewModel(get(), get()) }
    viewModel { SearchViewModel(get(), get()) }
    viewModel { DetailsViewModel(get(), get()) }
}

val appModules = listOf(
    coreModule, networkModule, databaseModule, repositoryModule, domainModule, viewModelModule
)