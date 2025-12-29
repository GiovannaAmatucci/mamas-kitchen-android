package com.giovanna.amatucci.foodbook.di

import android.content.Context
import androidx.room.Room
import com.giovanna.amatucci.foodbook.BuildConfig
import com.giovanna.amatucci.foodbook.data.local.db.AppDatabase
import com.giovanna.amatucci.foodbook.data.remote.network.NetworkHttpClient
import com.giovanna.amatucci.foodbook.data.remote.network.NetworkHttpClientImpl
import com.giovanna.amatucci.foodbook.data.remote.network.TokenHttpClient
import com.giovanna.amatucci.foodbook.data.remote.network.TokenHttpClientImpl
import com.giovanna.amatucci.foodbook.domain.repository.AuthRepository
import com.giovanna.amatucci.foodbook.domain.repository.TokenRepository
import com.giovanna.amatucci.foodbook.util.LogWriter
import com.giovanna.amatucci.foodbook.util.constants.KeyStoreConstants
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.giovanna.amatucci.foodbook")
class AppModule {
    @Single
    fun provideDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            KeyStoreConstants.KEY_STORE_ALIAS
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }

    @Single
    fun provideTokenDao(db: AppDatabase) = db.accessTokenDao()

    @Single
    fun provideSearchDao(db: AppDatabase) = db.searchDao()

    @Single
    fun provideFavoriteDao(db: AppDatabase) = db.favoriteDao()

    @Single
    fun provideNetworkClient(
        context: Context,
        token: TokenRepository,
        auth: AuthRepository,
        logWriter: LogWriter
    ): NetworkHttpClient {
        return NetworkHttpClientImpl(
            contextNet = context,
            baseHostUrl = BuildConfig.BASE_URL,
            requestTimeout = BuildConfig.REQUEST_TIMEOUT,
            connectTimeout = BuildConfig.CONNECT_TIMEOUT,
            isDebug = BuildConfig.DEBUG_MODE,
            token = token,
            auth = auth,
            logWriter = logWriter
        )
    }

    @Single
    fun provideTokenHttpClient(): TokenHttpClient = TokenHttpClientImpl(
        baseUrl = BuildConfig.TOKEN_URL
    )
}