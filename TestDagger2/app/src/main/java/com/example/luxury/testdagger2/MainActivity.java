package com.example.luxury.testdagger2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EmailComponent component=DaggerEmailComponent.builder()
                .emailModule(new EmailModule("hungnguyenba94@gmail.com"))
                .networkModule(new NetworkModule()).build();

        Email email=component.email();
        email.send("haah");

        Email email2=component.email();
        email2.send("ahsdkj");

    }
}
