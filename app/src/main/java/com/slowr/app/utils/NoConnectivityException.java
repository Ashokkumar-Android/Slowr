package com.slowr.app.utils;

import java.io.IOException;

/**
 * Created by Ashok on 10/28/2017.
 */

public class NoConnectivityException extends IOException {

    @Override
    public String getMessage() {
        return "No Internet Connection";
    }
}
