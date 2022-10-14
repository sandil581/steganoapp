package com.sandil.imagesteganographylibrary.Text.AsyncTaskCallback;

import com.sandil.imagesteganographylibrary.Text.ImageSteganography;


public interface TextEncodingCallback {

    void onStartTextEncoding();

    void onCompleteTextEncoding(ImageSteganography result);

}
