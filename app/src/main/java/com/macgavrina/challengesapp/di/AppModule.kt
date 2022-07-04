package com.macgavrina.challengesapp.di

import android.content.Context
import androidx.room.Room
import com.macgavrina.challengesapp.data.ChallengesRepositoryImpl
import com.macgavrina.challengesapp.data.local.AppDatabase
import com.macgavrina.challengesapp.data.local.ChallengeDAO
import com.macgavrina.challengesapp.data.local.ChallengeLocalStore
import com.macgavrina.challengesapp.data.remote.ChallengeRemoteStore
import com.macgavrina.challengesapp.data.remote.ChallengesAPI
import com.macgavrina.challengesapp.domain.ChallengesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideChallengesLocalStore(dao: ChallengeDAO) : ChallengeLocalStore =
        ChallengeLocalStore(dao)

    @Provides
    @Singleton
    fun provideChallengesRepository(
        remoteStore: ChallengeRemoteStore,
        localStore: ChallengeLocalStore
    ) : ChallengesRepository =
        ChallengesRepositoryImpl(remoteStore, localStore)

    @Provides
    fun challengesDao(appDatabase: AppDatabase): ChallengeDAO {
        return appDatabase.challengesDAO()
    }

    @Provides
    @Singleton
    fun provideChallengesDB(@ApplicationContext applicationContext: Context): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "challenges-bd"
        ).build()
    }
}