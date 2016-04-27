package br.com.smartpromos.services.image;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import br.com.smartpromos.smartpromosapplication.SmartPromosApp;

/**
 * Created by Paulo on 20/04/2016.
 */
public class ImageGenerate {

    public static void getImage(String url, final GenerateImageResponse generateImageResponse){

        RequestQueue rq = Volley.newRequestQueue(SmartPromosApp.context);

        rq.getCache().invalidate(url, true);
        ImageRequest ir = new ImageRequest(url, new Response.Listener<Bitmap>() {

            @Override
            public void onResponse(Bitmap response) {
                generateImageResponse.getBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, null);

        rq.getCache().remove(url);
        rq.getCache().clear();

        rq.add(ir);

    }

    public interface GenerateImageResponse {
        void getBitmap(Bitmap bm);
    }

}
