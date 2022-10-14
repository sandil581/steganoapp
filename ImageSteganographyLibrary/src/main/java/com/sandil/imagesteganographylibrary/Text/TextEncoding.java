package com.sandil.imagesteganographylibrary.Text;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.sandil.imagesteganographylibrary.Text.AsyncTaskCallback.TextEncodingCallback;
import com.sandil.imagesteganographylibrary.Utils.Utility;

import java.util.List;


public class TextEncoding extends AsyncTask<ImageSteganography, Integer, ImageSteganography> {


    private static final String TAG = TextEncoding.class.getName();

    private final ImageSteganography result;

    private final TextEncodingCallback callbackInterface;
    private int maximumProgress;
    private final ProgressDialog progressDialog;

    public TextEncoding(Activity activity, TextEncodingCallback callbackInterface) {
        super();
        this.progressDialog = new ProgressDialog(activity);
        this.callbackInterface = callbackInterface;
        //making result object
        this.result = new ImageSteganography();
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();


        if (progressDialog != null) {
            progressDialog.setMessage("Loading, Please Wait...");
            progressDialog.setTitle("Encoding Message");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    @Override
    protected void onPostExecute(ImageSteganography textStegnography) {
        super.onPostExecute(textStegnography);


        if (progressDialog != null) {
            progressDialog.dismiss();
        }


        callbackInterface.onCompleteTextEncoding(result);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);


        if (progressDialog != null) {
            progressDialog.incrementProgressBy(values[0]);
        }
    }

    @Override
    protected ImageSteganography doInBackground(ImageSteganography... imageSteganographies) {

        maximumProgress = 0;

        if (imageSteganographies.length > 0) {

            ImageSteganography textStegnography = imageSteganographies[0];


            Bitmap bitmap = textStegnography.getImage();


            int originalHeight = bitmap.getHeight();
            int originalWidth = bitmap.getWidth();


            List<Bitmap> src_list = Utility.splitImage(bitmap);



            List<Bitmap> encoded_list = EncodeDecode.encodeMessage(src_list, textStegnography.getEncrypted_message(), new EncodeDecode.ProgressHandler() {


                @Override
                public void setTotal(int tot) {
                    maximumProgress = tot;
                    progressDialog.setMax(maximumProgress);
                    Log.d(TAG, "Total Length : " + tot);
                }

                @Override
                public void increment(int inc) {
                    publishProgress(inc);
                }

                @Override
                public void finished() {
                    Log.d(TAG, "Message Encoding end....");
                    progressDialog.setIndeterminate(true);
                }
            });


            for (Bitmap bitm : src_list)
                bitm.recycle();


            System.gc();

            Bitmap srcEncoded = Utility.mergeImage(encoded_list, originalHeight, originalWidth);


            result.setEncoded_image(srcEncoded);
            result.setEncoded(true);
        }

        return result;
    }
}
