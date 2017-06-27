package com.example.luxury.testdagger2;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;

/**
 * Created by Luxury on 6/28/2017.
 */
@Singleton
@Component(modules = {EmailModule.class, NetworkModule.class})
public interface EmailComponent {
    Email email();

    OkHttpClient okHttpClient();
}
