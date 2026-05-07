package com.example.newdownloader26.di

import com.example.newdownloader26.BuildConfig
import com.example.newdownloader26.data.local.AutoPasteManager
import com.example.newdownloader26.data.local.DeviceVideoStore
import com.example.newdownloader26.data.local.SettingsPreferences
import com.example.newdownloader26.data.remote.VideoApiService
import com.example.newdownloader26.data.repository.VideoRepositoryImpl
import com.example.newdownloader26.domain.repository.VideoRepository
import com.example.newdownloader26.domain.usecase.DownloadVideoUseCase
import com.example.newdownloader26.presentation.downloads.DownloadsViewModel
import com.example.newdownloader26.presentation.downloader.DownloaderViewModel
import com.example.newdownloader26.presentation.language.LanguageViewModel
import com.example.newdownloader26.presentation.settings.SettingsViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.android.ext.koin.androidContext

import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "http://110.93.223.194:5695/"

val appModule = module {
    single {
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single { DeviceVideoStore(androidContext()) }
    single { SettingsPreferences(androidContext()) }
    single { AutoPasteManager(androidContext(), get()) }
    single<VideoApiService> { get<Retrofit>().create(VideoApiService::class.java) }
    single<VideoRepository> { VideoRepositoryImpl(androidContext(), get()) }
    single { DownloadVideoUseCase(get()) }
    viewModel { DownloaderViewModel(androidApplication(), get(), get(), get()) }
    viewModel { DownloadsViewModel(get()) }
    viewModel { (initialTag: String) -> LanguageViewModel(initialTag = initialTag) }
    viewModel { SettingsViewModel(get()) }
}
