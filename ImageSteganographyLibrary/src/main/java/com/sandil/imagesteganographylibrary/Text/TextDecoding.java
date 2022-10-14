package com.sandil.imagesteganographylibrary.Text;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.sandil.imagesteganographylibrary.Text.AsyncTaskCallback.TextDecodingCallback;
import com.sandil.imagesteganographylibrary.Utils.Utility;

import java.util.List;


public class TextDecoding extends AsyncTask<ImageSteganography, Void, ImageSteganography> {


    private final static String TAG = TextDecoding.class.getName();

    private final ImageSteganography result;

    private final TextDecodingCallback textDecodingCallback;
    private ProgressDialog progressDialog;

    public TextDecoding(Activity activity, TextDecodingCallback textDecodingCallback) {
        super();
        this.progressDialog = new ProgressDialog(activity);
        this.textDecodingCallback = textDecodingCallback;

        this.result = new ImageSteganography();
    }


    public void setProgressDialog(ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();


        if (progressDialog != null) {
            progressDialog.setMessage("Loading, Please Wait...");
            progressDialog.setTitle("Decoding Message");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

        }
    }

    @Override
    protected void onPostExecute(ImageSteganography imageSteganography) {
        super.onPostExecute(imageSteganography);


        if (progressDialog != null)
            progressDialog.dismiss();


        textDecodingCallback.onCompleteTextEncoding(result);
    }

    @Override
    protected ImageSteganography doInBackground(ImageSteganography... imageSteganographies) {


        if (imageSteganographies.length > 0) {

            ImageSteganography imageSteganography = imageSteganographies[0];


            Bitmap bitmap = imageSteganography.getImage();


            List<Bitmap> srcEncodedList = Utility.splitImage(bitmap);


            String decoded_message = EncodeDecode.decodeMessage(srcEncodedList);

            Log.d(TAG, "Decoded_Message : " + decoded_message);


            if (!Utility.isStringEmpty(decoded_message)) {
                result.setDecoded(true);
            }


            String decrypted_message = ImageSteganography.decryptMessage(decoded_message, imageSteganography.getSecret_key());
            Log.d(TAG, "Decrypted message : " + decrypted_message);


            if (!Utility.isStringEmpty(decrypted_message)) {


                result.setSecretKeyWrong(false);



                result.setMessage(decrypted_message);



                for (Bitmap bitm : srcEncodedList)
                    bitm.recycle();


                System.gc();
            }
        }

        return result;
    }
}
