package com.example.luxury.testdagger2;

/**
 * Created by Luxury on 6/28/2017.
 */

public class Email {
    EmailAPi client;
    String emainURl;

    public Email(EmailAPi emailAPi, String emainURl) {
        client = emailAPi;
        this.emainURl = emainURl;
    }

    public boolean send(String body) {
        client.sendEmail(body, emainURl);
        return true;
    }
}
