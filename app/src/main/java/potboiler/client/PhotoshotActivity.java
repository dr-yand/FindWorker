package potboiler.client;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import potboiler.client.util.FileUtils;
import potboiler.client.view.Preview;

public class PhotoshotActivity  extends Activity implements SensorEventListener, OnClickListener{
    protected static final String TAG = "photo";
	private Preview mPreview;
	private Camera mCamera;
    private int numberOfCameras;
    private int cameraCurrentlyLocked; 
    
    private int defaultCameraId;   
     
    private ImageButton mShot, mLock;
    
    private int mRotateInd=1;
    
    private TextView mPhotoCount;
    
    boolean mLockState=false;
    
    private SensorManager mSensorManager;
    private Sensor mOrientation;
    
    private int photoCount=0;
    
    private List<String> files = new ArrayList<String>();


	@Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
 
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_photoshot);
          
        mPreview = new Preview(this);
        ((FrameLayout) findViewById(R.id.preview)).addView(mPreview);

        Intent in = getIntent();
	     
        numberOfCameras = Camera.getNumberOfCameras();
 
        CameraInfo cameraInfo = new CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
                defaultCameraId = i;
            }
        }
        
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);   

        initView();
        
    }

    
    @Override
	public void onBackPressed() {
    	/*Intent intent = new Intent();
    	intent.putExtra("files", files.toArray(new String[files.size()]));
    	setResult(RESULT_OK, intent);*/
		super.onBackPressed();
	}

    private void initView(){
    	mShot = (ImageButton)findViewById(R.id.shot);
    	mShot.setOnClickListener(this);
    	
    	mLock = (ImageButton)findViewById(R.id.lock_indicator);
    	mLock.setOnClickListener(this);
    	
    	mPhotoCount = (TextView)findViewById(R.id.photo_count);
    	mPhotoCount.setText(photoCount+"");
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        mSensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_NORMAL);
         
        mCamera = Camera.open();
        
        
        Camera.Parameters parameters = mCamera.getParameters();
        List<Size> sizes = parameters.getSupportedPictureSizes();
        Size size = sizes.get(1);
         
        int n=1000;        
        for(Size s: sizes){       
        	if(Math.abs(1300-s.width)<n){
        		n=Math.abs(1300-s.width);
        		size=s;        		        		
        	}
        	 
        }                        
        parameters.setPictureSize(size.width, size.height);
        mCamera.setParameters(parameters);   
        
        cameraCurrentlyLocked = defaultCameraId;
        
        mCamera.setDisplayOrientation(90);
        
        mPreview.setCamera(mCamera);
    }

    @Override
    protected void onPause() {
        super.onPause();
        
        mSensorManager.unregisterListener(this);
 
        if (mCamera != null) {
         	mCamera.stopPreview();
             mPreview.setCamera(null);
             mCamera.release();
             mCamera = null;
         }
        
//        Intent mIntent = new Intent();
//        mIntent.putExtra("fileName", fileName);
//        
//    	setResult(resCode, mIntent);
//    	finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { 
        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.camera_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { 
        switch (item.getItemId()) {
//        case R.id.switch_cam: 
//            if (numberOfCameras == 1) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setMessage(this.getString(R.string.camera_alert))
//                       .setNeutralButton("Close", null);
//                AlertDialog alert = builder.create();
//                alert.show();
//                return true;
//            }
// 
//            if (mCamera != null) {
//                mCamera.stopPreview();
//                mPreview.setCamera(null);
//                mCamera.release();
//                mCamera = null;
//            }
// 
//            mCamera = Camera
//                    .open((cameraCurrentlyLocked + 1) % numberOfCameras);
//            cameraCurrentlyLocked = (cameraCurrentlyLocked + 1)
//                    % numberOfCameras;
//            mPreview.switchCamera(mCamera);
// 
//            mCamera.startPreview();
//            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
	ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
 
		}
	};

 
	PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			
 
		}
	};

	/** Handles data for jpeg picture */
	PictureCallback jpegCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			try {
				System.gc();
				FileOutputStream outStream = null;
				if (data == null){					
					mCamera.startPreview();
					mShot.setEnabled(true);				     
					return;
				}
				
				 Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
				  
				 if(mRotateInd==1){
					 Matrix matrix = new Matrix();
					 matrix.postRotate(90);				
					 bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
				 }
				 else if(mRotateInd==0){
					 Matrix matrix = new Matrix();
					 matrix.postRotate(180);				
					 bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
				 } 
				 
				 if (bmp == null){
					 mCamera.startPreview();
					 mShot.setEnabled(true);
					 return;
				 }
								  
//				File dir = new File (_context.getExternalFilesDir("") + "Photos");
//				dir.mkdirs();
//				String tempFile = FileUtils.getTempFile(getApplicationContext());
				
				String imagename = FileUtils.generateId();

//					String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
					String dateStr = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
					imagename=dateStr;
					imagename = imagename.replaceAll("[^\\w]", "_");
					imagename = imagename.replaceAll("_{2,}", "_");

				String filename = FileUtils.getImagesPath(getApplicationContext())+imagename;
				 
		    	outStream = new FileOutputStream(filename);
		    	 
		    	bmp.compress(CompressFormat.JPEG, 70, outStream);

		 
		    	outStream.flush();
				outStream.close();
				
				photoCount++;
 
				mPhotoCount.setText(photoCount + "");
				 
				mCamera.startPreview();
				mShot.setEnabled(true);
				
				files.add(filename);
				
				bmp.recycle();
				bmp=null;
				System.gc();

                Intent intent = new Intent();
                intent.putExtra("files", files.toArray(new String[files.size()]));
                setResult(RESULT_OK, intent);
                finish();
				 
			} catch (Exception e) { 
				e.printStackTrace();
			} finally {
			}
		 
		}
	};
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	 
	}
	 
	@SuppressLint("NewApi")
	@Override
	public void onSensorChanged(SensorEvent arg0) {
		float azimuth_angle = arg0.values[0];
	    float pitch_angle = arg0.values[1];
	    float roll_angle = arg0.values[2];
	    
	    if(!mLockState){
		    if(roll_angle>60){
		    	mRotateInd=2;
		    } 
			else if(Math.abs(pitch_angle)<32&&Math.abs(roll_angle)<60){
				mRotateInd=2;
			}
		    else{
		    	mRotateInd=1;
		    }
		     
		    /*if(mRotateInd==2){
//				((ImageButton)findViewById(R.id.photoOrientationButton)).setImageResource(R.drawable.land2);
		    	mPhotoCount.setRotation(90);
		    	mLock.setRotation(90);
			}
			else{
//				((ImageButton)findViewById(R.id.photoOrientationButton)).setImageResource(R.drawable.port2);
				mPhotoCount.setRotation(0);
				mLock.setRotation(0);
			}*/
		    if(mRotateInd==2){
			    if (Build.VERSION.SDK_INT < 11) {
			    	RotateAnimation animation = new RotateAnimation(0, 0,
		    			Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
	    			animation.setInterpolator(new LinearInterpolator());
	    			animation.setDuration(1);
	    			animation.setFillAfter(true);
 
			        mPhotoCount.startAnimation(animation);
			        mLock.startAnimation(animation);
			    } else {
			    	mPhotoCount.setRotation(90);
			    	mLock.setRotation(90);
			    }
		    }
		    else{
		    	if (Build.VERSION.SDK_INT < 11) {
		    		RotateAnimation animation = new RotateAnimation(270, 270,
			    			Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
	    			animation.setInterpolator(new LinearInterpolator());
	    			animation.setDuration(1);
	    			animation.setFillAfter(true);
			        mPhotoCount.startAnimation(animation);
			        mLock.startAnimation(animation);
			    } else {
			    	mPhotoCount.setRotation(0);
			    	mLock.setRotation(0);
			    }
		    }
	    }
	}
  
	@Override
	public void onClick(View v) {
		 if(v.getId()==R.id.shot){
			 try{				 
				 mShot.setEnabled(false);
				 mCamera.autoFocus(new AutoFocusCallback() {
					public void onAutoFocus(boolean success, Camera camera) {
						mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);
					}
				});
			 } 
			 catch(Exception ex){
				 ex.printStackTrace();
			 }
		 }
		 if(v.getId()==R.id.lock_indicator){
//			 v.setTag(!v.getTag());			 
//			 v.setPressed(!v.get);
			 mLockState=!mLockState;
			 v.setSelected(mLockState);
		 }
	}
	
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if(resultCode == RESULT_OK){
//			finish();
//		}
//	}
//	
}
 