package potboiler.client.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Random;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import org.apache.http.util.ByteArrayBuffer;

public class FileUtils {

	public static void copyFile(String fileIn, String fileOut) throws IOException {
		InputStream in = new FileInputStream(new File(fileIn));
		OutputStream out = new FileOutputStream(new File(fileOut));
	    byte[] buffer = new byte[1024]; 
	    int read; 
	    while((read = in.read(buffer)) != -1){ 
	      out.write(buffer, 0, read); 
	    } 
	}
	
	public static String getAudioFilePathFromUri(Context context, Uri uri) {
	      Cursor cursor = context.getContentResolver()
	            .query(uri, null, null, null, null);
	      cursor.moveToFirst();
	      int index = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
	      return cursor.getString(index);
	}
	
	public static String getTempFile(Context context){
//    	String destinationFile = context.getExternalFilesDir(null).getPath() + File.separator + "~abcdefgh";
    	String destinationFile = context.getExternalFilesDir(null).getPath() + File.separator + "temp.jpg";
    	return destinationFile;
    }
	
	 
	public static void copy(String sourceFile, String destinationFile) throws IOException {
	  InputStream is = new FileInputStream(sourceFile);
	  OutputStream os = new FileOutputStream(destinationFile);
	  copyStream(is, os);
	  os.flush();
	  os.close();
	  is.close();
	}
	
	private static void copyStream(InputStream Input, OutputStream Output) throws IOException {
	  byte[] buffer = new byte[5120];
	  int length = Input.read(buffer);
	  while (length > 0) {
	    Output.write(buffer, 0, length);
	    length = Input.read(buffer);
	  }
	}
	
	public static String getImagesPath(Context context){
    	String destinationFile = context.getExternalFilesDir(null).getPath() + File.separator+"im" + File.separator;
    	File dir = new File(context.getExternalFilesDir(null).getPath(), "im");  
        if(!dir.exists()) {
            dir.mkdir();
        }
    	return destinationFile;
    }

	public static String generateId(){
		String result="";
		Random random = new Random();
		for(int i=0;i<15;i++){
			result+=random.nextInt(1000);
		}
		return result;
	}

	public static void downloadFile(String urlString, String filename){
		try {
			File file = new File(filename);
			InputStream is = (InputStream) new URL(urlString).getContent();
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baf.toByteArray());
			fos.close();
		}
		catch (Exception e){

		}
	}
}
