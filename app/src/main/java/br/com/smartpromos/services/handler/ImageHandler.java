package br.com.smartpromos.services.handler;

import android.graphics.Bitmap;

import br.com.smartpromos.services.image.ImageGenerate;

/**
 * Created by Paulo on 20/04/2016.
 */
public class ImageHandler {

    private static Bitmap bitmap;

    public synchronized static Bitmap loadImagem(String url){

        if(url != null || !url.equals("")){

            try{

                ImageGenerate.getImage(url, new ImageGenerate.GenerateImageResponse() {
                    @Override
                    public void getBitmap(Bitmap bm) {
                        bitmap = bm;
                    }
                });

                return bitmap;
            }catch (Exception e){
                e.printStackTrace();

                return null;
            }

        }

        return null;

    }


}
