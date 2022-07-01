package com.macgavrina.challengesapp.di

import com.macgavrina.challengesapp.data.ChallengesRepositoryImpl
import com.macgavrina.challengesapp.data.remote.ChallengeRemoteStore
import com.macgavrina.challengesapp.data.remote.ChallengesAPI
import com.macgavrina.challengesapp.domain.ChallengesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "http://www.boredapi.com/api/"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideChallengesApi(retrofit : Retrofit) : ChallengesAPI =
        retrofit.create(ChallengesAPI::class.java)

    @Provides
    @Singleton
    fun provideChallengesRemoteStore(api: ChallengesAPI) : ChallengeRemoteStore =
        ChallengeRemoteStore(api)

    @Provides
    @Singleton
    fun provideChallengesRepository(remoteStore: ChallengeRemoteStore) : ChallengesRepository =
        ChallengesRepositoryImpl(remoteStore)
}