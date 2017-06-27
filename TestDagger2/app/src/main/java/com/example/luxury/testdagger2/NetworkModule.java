package com.example.luxury.testdagger2;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * Created by Luxury on 6/28/2017.
 */
@Module
public class NetworkModule {
    @Provides
    @Singleton
    OkHttpClient provideOkhttp(){
        return new OkHttpClient();
    }
}
