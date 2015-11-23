package potboiler.client.api;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import potboiler.client.model.ServerResponseStatus;
import potboiler.client.model.User;
import potboiler.client.util.PreferenceUtils;


public class SendTask extends AsyncTask<Void, Void, ServerResponseStatus>{

	public interface OnResultListener {
		public void onSendResult(ServerResponseStatus responseStatus, String errorMessage, String file);
	}

	private OnResultListener mListener;
    private Context mContext;
    private double mLat, mLon;
    private boolean mLocationFallback;
    private String mFile;
    private String mErrorMessage;


	public SendTask(OnResultListener listener, Context context,  String file, double lat, double lon, boolean locationFallback){
		this.mListener=listener;
        this.mContext = context;
        this.mFile = file;
        this.mLat = lat;
        this.mLon = lon;
        this.mLocationFallback = locationFallback;
	}

    @Override
    protected ServerResponseStatus doInBackground(Void... par) {
        HttpClient httpclient = new DefaultHttpClient();

        User user = PreferenceUtils.getUser(mContext);

        HttpPost httppost = new HttpPost("http:///save-qr");
        /*String authentication = android.util.Base64.encodeToString((user.phone + ":" + user.password).getBytes(), android.util.Base64.NO_WRAP);
        httppost.addHeader("Authorization", "Basic " + authentication.trim());
        httppost.addHeader("Content-type", "application/json");*/

        ServerResponseStatus responseStatus = ServerResponseStatus.ERROR;

        try {
            MultipartEntity mpEntity = new MultipartEntity();
            File file = new File(mFile);
            if(file.exists()&&file.isFile()){
                ContentBody cbFile = new FileBody(file, "image/jpeg");
                mpEntity.addPart("file", cbFile);
            }

            Charset chars = Charset.forName("UTF-8");
           /* if(!user.qrFlag) {
                mpEntity.addPart("credentials.login", new StringBody(user.phone, chars));
                mpEntity.addPart("credentials.password", new StringBody(user.password, chars));
            }
            else{
                mpEntity.addPart("credentials.qrCode", new StringBody(user.qrcode, chars));
                mpEntity.addPart("credentials.qrMessage", new StringBody(user.qrmessage, chars));
            }*/
            mpEntity.addPart("locator.longitude", new StringBody(mLon+"",chars));
            mpEntity.addPart("locator.latitude", new StringBody(mLat+"",chars));
            mpEntity.addPart("locator.locationFallback", new StringBody(mLocationFallback+"",chars));

//            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            httppost.setEntity(mpEntity);

            HttpResponse response = httpclient.execute(httppost);

            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                String responseString = out.toString();

                mErrorMessage = parseResponse(responseString);
                responseStatus = ServerResponseStatus.OK;
            } else{
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseStatus;
    }

    private String parseResponse(String data) throws JSONException {
        JSONObject jObject = new JSONObject(data);
//        JSONArray dataArray = jObject.getJSONArray("data");
        boolean valid = jObject.getBoolean("valid");
        String errorMessage = jObject.getString("errorMessage");
        if(valid)
            return null;
        else
            return errorMessage;
    }

    @Override
	protected void onCancelled() {
		mListener = null;
		super.onCancelled();
	}

	@Override
    protected void onPostExecute(ServerResponseStatus responseStatus) {
        super.onPostExecute(responseStatus);

        if(mListener!=null)
            mListener.onSendResult(responseStatus, mErrorMessage, mFile);
    }

}