package xyz.teamgravity.newsapp.injection

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import xyz.teamgravity.newsapp.api.NewsApi
import xyz.teamgravity.newsapp.helper.constants.MyApi
import xyz.teamgravity.newsapp.helper.constants.MyDatabase
import xyz.teamgravity.newsapp.viewmodel.NewsDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return interceptor
    }

    @Singleton
    @Provides
    fun provideHttpClient(interceptor: HttpLoggingInterceptor) = OkHttpClient.Builder().addInterceptor(interceptor).build()

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder().baseUrl(MyApi.NEWS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    @Singleton
    @Provides
    fun provideNewsApi(retrofit: Retrofit): NewsApi = retrofit.create(NewsApi::class.java)

    @Singleton
    @Provides
    fun provideNewsDatabase(app: Application) = Room.databaseBuilder(app, NewsDatabase::class.java, MyDatabase.DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideNewsDao(db: NewsDatabase) = db.newsDao()
}