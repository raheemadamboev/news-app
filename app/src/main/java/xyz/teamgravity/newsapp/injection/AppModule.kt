package xyz.teamgravity.newsapp.injection

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import xyz.teamgravity.newsapp.api.NewsApi
import xyz.teamgravity.newsapp.helper.constants.Constants
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder().baseUrl(Constants.NEWS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

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
    fun provideNewsApi(retrofit: Retrofit): NewsApi  = retrofit.create(NewsApi::class.java)
}