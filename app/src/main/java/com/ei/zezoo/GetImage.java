package com.ei.zezoo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class GetImage extends AsyncTask<Void, Void, String> {
    String source;
    ImageView img;
    ProgressBar progress;
    Bitmap b = null;
    int size;

    GetImage(  ImageView img, ProgressBar progress) {
        this.source = source;
        this.img = img;
        this.progress = progress;
        size = 150;
    }

    @Override
    protected String doInBackground(Void... params) {
        String s = "";
        try {
            b = ImageRounded.getRoundedCornerBitmap(getResizedBitmap(reduceResolution(source, size, size),200,200),10);

        } catch (Exception e) {
            s = e.getMessage();
        }
        return s;
    }

    @Override
    protected void onPostExecute(final String token) {
        progress.setVisibility(View.GONE);
        img.setVisibility(View.VISIBLE);
        img.setImageBitmap(b);
    }

    public Bitmap reduceResolution(String filePath, int viewWidth, int viewHeight) {
        int reqHeight = viewHeight;
        int reqWidth = viewWidth;

        BitmapFactory.Options options = new BitmapFactory.Options();

        // First decode with inJustDecodeBounds=true to check dimensions
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        double viewAspectRatio = 1.0 * viewWidth/viewHeight;
        double bitmapAspectRatio = 1.0 * options.outWidth/options.outHeight;

        if (bitmapAspectRatio > viewAspectRatio)
            reqHeight = (int) (viewWidth/bitmapAspectRatio);
        else
            reqWidth = (int) (viewHeight * bitmapAspectRatio);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        System.gc();                                        // TODO - remove explicit gc calls
        return BitmapFactory.decodeFile(filePath, options);
    }


    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth)
    {
        int     width           = bm.getWidth();
        int     height          = bm.getHeight();
        float   scaleWidth      = ((float) newWidth) / width;
        float   scaleHeight     = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return resizedBitmap;
    }
}
