package potboiler.client.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.squareup.picasso.Transformation;

public class CirclePicassoTransform implements Transformation {

    private Context mContext;

    public CirclePicassoTransform(Context context){
        mContext = context;
    }

    @Override
    public Bitmap transform(Bitmap source) {
//        int size = Math.min(source.getWidth(), source.getHeight());
//
//        int x = (source.getWidth() - size) / 2;
//        int y = (source.getHeight() - size) / 2;
//
//        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
//        if (squaredBitmap != source) {
//            source.recycle();
//        }
//
//        Bitmap bitmap = Bitmap.createBitmap(size + 8, size + 8, source.getConfig());
//
//        Canvas canvas = new Canvas(bitmap);
//        Paint paint = new Paint();
//        BitmapShader shader = new BitmapShader(squaredBitmap,
//                BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
//        paint.setShader(shader);
//        paint.setAntiAlias(true);
//
//        float r = size / 2f;
//        paint.setStyle(Paint.Style.FILL);
//        canvas.drawCircle(r + 4, r + 4, r, paint);
//
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//
//        canvas.drawBitmap(squaredBitmap, 4, 4, paint);
//        paint.setXfermode(null);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setColor(Color.WHITE);
//        paint.setStrokeWidth(3);
//        canvas.drawCircle(r + 4, r + 4, r, paint);
//
//        squaredBitmap.recycle();

//        int w = source.getWidth();
//        int h = source.getHeight();
//
//        int radius = Math.min(h / 2, w / 2);
//
//        Bitmap squaredBitmap = Bitmap.createBitmap(source, radius, radius, radius, radius);
//        if (squaredBitmap != source) {
//            source.recycle();
//        }
//
//        Bitmap circleBitmap = Bitmap.createBitmap(w + 8, h + 8, Bitmap.Config.ARGB_8888);
//
//        Paint paint = new Paint();
//        paint.setAntiAlias(true);
//
//        Canvas c = new Canvas(circleBitmap);
//        c.drawARGB(0, 0, 0, 0);
//        paint.setStyle(Paint.Style.FILL);
//
//        c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, paint);
//
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//
//        c.drawBitmap(source, 4, 4, paint);
//        paint.setXfermode(null);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setColor(Color.WHITE);
//        paint.setStrokeWidth(3);
//        c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, paint);
//
//        squaredBitmap.recycle();

//        Bitmap b = ImageUtils.resizeBitmap(source, ImageUtils.dpToPx(mContext,50), ImageUtils.dpToPx(mContext,50));


        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        if (squaredBitmap != source) {
            source.recycle();
        }

//        Bitmap bitmap = Bitmap.createBitmap(size + 8, size + 8, source.getConfig());
        Bitmap circleBitmap = ImageUtils.createCircleImage(squaredBitmap);

        squaredBitmap.recycle();
//        b.recycle();

        return circleBitmap;
    }

    @Override
    public String key() {
        return "circle";
    }
}
