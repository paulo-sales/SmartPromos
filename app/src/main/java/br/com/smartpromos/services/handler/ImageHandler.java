package br.com.smartpromos.services.handler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

import br.com.smartpromos.services.image.ImageGenerate;

/**
 * Created by Paulo on 20/04/2016.
 */
public class ImageHandler {

    private static Bitmap bitmap;
    private static File pathLive = new File(Environment.getExternalStorageDirectory()+File.separator+"SmartPromos/imageCupons");

    public synchronized static String generateFeedfileImage(String url, String name)
    {

        final String photo = "promo-"+name+".png";
        try {


            ImageGenerate.getImage(url, new ImageGenerate.GenerateFacebookImageResponse() {
                @Override
                public void getBitmap(Bitmap bm) {
                    //deleteImage(name);
                    if(!isLoaded(photo)){
                        Log.d("IMAGE_NOT_EXIST", "Imagem não existe no cache.");
                        saveImage(bm, photo);
                    }else{
                        Log.d("IMAGE_EXIST", "Imagem já existe no cache.");
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

        return name;
    }

    public static boolean isLoaded(String name){

        File file = new File(pathLive+File.separator+name);
        Bitmap b = null;
        try
        {
            b = BitmapFactory.decodeFile(file.getAbsolutePath());
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public static Bitmap getImageBitmap(String name, String url)
    {

        final String namePhoto = "promo-"+name+".png";

        File file = new File(pathLive+File.separator+namePhoto);
        Bitmap b = null;
        try
        {
            b = BitmapFactory.decodeFile(file.getAbsolutePath());
            return b;
        }
        catch(Exception e){
            e.printStackTrace();
        }

        if(b == null){
            generateFeedfileImage(name, url);
            getImageBitmap(name, url);
        }

        return null;
    }

    public static void saveImage(Bitmap bitmap, String name){

        try {
            if(createNewPath()) {
                File file = new File(pathLive, name);

                FileOutputStream fos = null;

                fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

                fos.flush();
                fos.close();

                Log.e("IMAGEM_SAVED", pathLive.toString()+"/"+name);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERROR_IMAGE", e.getMessage());

        }

    }

    public static void cleanCache()
    {

        try{

            File[] arquivos = pathLive.listFiles();

            for(File path:arquivos){
                path.delete();
            }
            Log.e("CACHE_CLEANED", "Finalizado");
        }catch(Exception e){

        }
    }

    public static boolean createNewPath(){
        boolean success;

        if(!pathLive.exists()){
            success = pathLive.mkdirs();
        }else{
            success = true;
        }

        return success;
    }


}
