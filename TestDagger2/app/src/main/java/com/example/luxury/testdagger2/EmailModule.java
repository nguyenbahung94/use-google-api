package com.example.luxury.testdagger2;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * Created by Luxury on 6/28/2017.
 */
@Module
public class EmailModule {
    String emailUrl;

    public EmailModule(String emailUrl) {
        this.emailUrl = emailUrl;
    }
    @Provides
    @Singleton
    Email provideEmail(EmailAPi client) {
        return new Email(client, emailUrl);
    }
     @Provides
     @Singleton
    EmailAPi provideEmailApi(OkHttpClient client) {
        return new EmailAPi(client);
    }
}
