package com.brein.api;

import com.brein.domain.BreinResult;

public interface ICallback <T extends BreinResult> {

    void callback(T data);
}
