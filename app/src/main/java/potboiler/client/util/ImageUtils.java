package potboiler.client.util;

import java.io.FileOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

public class ImageUtils {
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	
	    if (height > reqHeight || width > reqWidth) {
	
//	        final int halfHeight = height / 2;
//	        final int halfWidth = width / 2;
	
	        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
	        // height and width larger than the requested height and width.
	        while ((height / inSampleSize) > reqHeight
	                || (width / inSampleSize) > reqWidth) {
//	        	Log.i("DisplayMetrics", reqWidth+";"+reqHeight);
//	        	Log.i("halfWidth halfHeight inSampleSize", width+";"+height+";"+inSampleSize+"");
	            inSampleSize *= 2;
	        }
	    }
	    
//	    Log.i("inSampleSize", inSampleSize+"");

	    return inSampleSize+2;
	}

	public static Bitmap resizeBitmap(Bitmap bitmap,
	        int reqWidth, int reqHeight) {

		return Bitmap.createScaledBitmap(bitmap, reqWidth, reqHeight, false);
	}
	
	public static Bitmap decodeBitmapFromFile(Context context, String file,
	        int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(file, options);

	    // Calculate inSampleSize
	    options.inSampleSize =  calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeFile(file,options);
	}
	
	/*public static Bitmap decodeBitmapFromFileSmall(Context context, String file) {
		try{
			Bitmap bitmapTemp = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_launcher);
			Bitmap bitmap = BitmapFactory.decodeFile(FileUtils.getImagesPath(context)+file);
	//		return decodeBitmapFromFile(context, FileUtils.getImagesPath(context)+file, bitmap.getWidth(), bitmap.getHeight());
			return resizeBitmap(bitmap, bitmapTemp.getWidth(), bitmapTemp.getHeight());
		}
		catch(Exception e){
			e.printStackTrace();
			Bitmap bitmapTemp = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_launcher);
			Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.small_default);
			return resizeBitmap(bitmap, bitmapTemp.getWidth(), bitmapTemp.getHeight());
		}
//		return null;
	}
	*/
	/*public static Bitmap decodeBitmapFromFileDoubleSmall(Context context, String file) {
		try{
			Bitmap bitmapTemp = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_launcher);
			Bitmap bitmap = BitmapFactory.decodeFile(FileUtils.getImagesPath(context)+file);
	//		return decodeBitmapFromFile(context, FileUtils.getImagesPath(context)+file, bitmap.getWidth(), bitmap.getHeight());
			return resizeBitmap(bitmap,(int)(bitmapTemp.getWidth()*1.5), (int)(bitmapTemp.getHeight()*1.5));
		}
		catch(Exception e){
			e.printStackTrace();
			Bitmap bitmapTemp = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_launcher);
			Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.small_default);
	//		return decodeBitmapFromFile(context, FileUtils.getImagesPath(context)+file, bitmap.getWidth(), bitmap.getHeight());
			return resizeBitmap(bitmap,(int)(bitmapTemp.getWidth()*1.5), (int)(bitmapTemp.getHeight()*1.5));
		}
//		return null;
	}*/
	
	/*public static Bitmap decodeBitmapFromFile(Context context, String file) {
		try{
			Bitmap bitmapTemp = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_launcher);
			Bitmap bitmap = BitmapFactory.decodeFile(FileUtils.getImagesPath(context)+file);
	//		return decodeBitmapFromFile(context, FileUtils.getImagesPath(context)+file, bitmap.getWidth(), bitmap.getHeight());
			return bitmap;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}*/
	
	/*public static Bitmap decodeBitmapFromFileDoubleSize(Context context, String file) {
		Bitmap bitmapTemp = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_launcher);
		try{
//			Bitmap bitmapTemp = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_launcher);
			Bitmap bitmap = BitmapFactory.decodeFile(FileUtils.getImagesPath(context)+file);
			return resizeBitmap(bitmap, bitmap.getWidth()*2, bitmap.getHeight()*2);
//			return decodeBitmapFromFile(context, FileUtils.getImagesPath(context)+file, bitmap.getWidth(), bitmap.getHeight());
//			return bitmap;
		}
		catch(Exception e){
			e.printStackTrace();
			Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.small_default);
			return resizeBitmap(bitmap, bitmapTemp.getWidth()*2, bitmapTemp.getHeight()*2);
		}
//		return null;
	}*/
	
	/*public static Bitmap decodeBitmapFromFileWithWidth(Context context, String file, int width) {
		try{ 
			Bitmap bitmap = BitmapFactory.decodeFile(FileUtils.getImagesPath(context)+file);
			float k = bitmap.getWidth()*1f/width;
			int height = (int)(bitmap.getHeight()/k);
			return resizeBitmap(bitmap, width, height);
		}
		catch(Exception e){
			e.printStackTrace();
			Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.big_default);
			float k = bitmap.getWidth()*1f/width;
			int height = (int)(bitmap.getHeight()/k);
			return resizeBitmap(bitmap, width, height);
		}
//		return null;
	}*/
	
	public static Bitmap decodeThumbnailFromFile(Context context, String file) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(file, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, 200, 200);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeFile(file,options);
	}

	public static Bitmap getImageFromFile(Context context, String file) {
		return BitmapFactory.decodeFile(file);
	}
	
	/*public static void createThumbnail(Bitmap bitmap, String filename) throws Exception{ 
        final int THUMBNAIL_SIZE = 64;

//            FileInputStream fis = new FileInputStream(fileName);
//            Bitmap imageBitmap = BitmapFactory.decodeStream(fis);

        bitmap = Bitmap.createScaledBitmap(bitmap, THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);

        FileOutputStream out = new FileOutputStream(filename);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        out.flush();
  	  	out.close(); 
	}*/
	
	public static void createThumbnail(Context context, String imagename, String thumbnailName) throws Exception{ 
        Bitmap bitmap = decodeThumbnailFromFile(context, imagename);
        FileOutputStream out = new FileOutputStream(thumbnailName);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        out.flush();
  	  	out.close(); 
	}

	public static Bitmap createCircleImage(Bitmap bitmap){
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		int radius = Math.min(h / 2, w / 2);
		Bitmap circleBitmap = Bitmap.createBitmap(w + 8, h + 8, Bitmap.Config.ARGB_8888);

		Paint paint = new Paint();
		paint.setAntiAlias(true);

		Canvas c = new Canvas(circleBitmap);
		c.drawARGB(0, 0, 0, 0);
		paint.setStyle(Paint.Style.FILL);

		c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

		c.drawBitmap(bitmap, 4, 4, paint);
		paint.setXfermode(null);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(3);
		c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, paint);
		return circleBitmap;
	}

	public static Bitmap resizeBitmap(Bitmap bitmap, int width) {
		try{
			float k = bitmap.getWidth()*1f/width;
			int height = (int)(bitmap.getHeight()/k);
			return resizeBitmap(bitmap, width, height);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static float pxToDp(Context context, int px){
		float density = context.getResources().getDisplayMetrics().density;
		float dp = px / density;
		return dp;
	}

	public static int dpToPx(Context context, int dp){
		float density = context.getResources().getDisplayMetrics().density;
		float px = dp * density;
		return (int)px;
	}
}
