package com.brein.api;

import com.brein.domain.BreinResult;

public class RestCallback implements ICallback {

    @Override
    public void callback(BreinResult data) {

        System.out.println("data is:" + data.toString());

    }
}
