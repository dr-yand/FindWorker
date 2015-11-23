package potboiler.client.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.util.VKUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import potboiler.client.api.ApiClient;
import ru.ok.android.sdk.Odnoklassniki;
import ru.ok.android.sdk.OkListener;
import ru.ok.android.sdk.util.OkScope;

public class AuthAdapter {

    public interface AuthListener{
        public void onFbLogin(String email, String token, String userId);
        public void onVkLogin(String token, String userId);
        public void onOkLogin(String token);
        public void onError(String error);
    }
    private CallbackManager fbCallbackManager;
    private AccessTokenTracker fbAccessTokenTracker;

    protected static final String OK_APP_ID = "*****16";
    protected static final String OK_APP_KEY = "*****BA";
    //    F9CE9BE60A767D9DF1411291
    protected static final String REDIRECT_URL = "http://*****/";

    protected Odnoklassniki mOdnoklassniki;

    private Context mContext;
    private AuthListener mListener;

    public AuthAdapter(Context context, AuthListener listener){
        mContext = context;
        mListener = listener;

        FacebookSdk.sdkInitialize(mContext);
        LoginManager.getInstance().logOut();

        mOdnoklassniki = Odnoklassniki.createInstance(mContext, OK_APP_ID, OK_APP_KEY);
        mOdnoklassniki.checkValidTokens(new OkListener() {
            @Override
            public void onSuccess(JSONObject json) {
//                Toast.makeText(SplashActivity.this,"Авторизация удачна"+json.toString(),Toast.LENGTH_LONG).show();
//                new ApiClient(SplashActivity.this).signup(SplashActivity.this, 3, email, email, "", token, userId);
            }

            @Override
            public void onError(String error) {
//                Toast.makeText(SplashActivity.this, String.format("%s: %s", "Ошибка", error), Toast.LENGTH_LONG).show();
            }
        });

        fbCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(fbCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        final String userId =  loginResult.getAccessToken().getUserId();
                        final String token = loginResult.getAccessToken().getToken();

                        Profile profile = Profile.getCurrentProfile();
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        if (response.getError() != null) {
//                                            Toast.makeText(SplashActivity.this, "Произошла ошибка", Toast.LENGTH_LONG).show();
                                            mListener.onError("Произошла ошибка");
                                        } else {
                                            String email = object.optString("email");
//                                            Toast.makeText(SplashActivity.this,"userId: "+userId+"\r\ntoken: "+token+"\r\nemail: "+email,Toast.LENGTH_LONG).show();
//                                            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//                                            ClipData clip = ClipData.newPlainText("label", "Text to copy");
//                                            clipboard.setText("userId: "+userId+"\r\ntoken: "+token+"\r\nemail: "+email);

//                                            mProgressDialog.show();
//                                            new ApiClient(SplashActivity.this).signup(SplashActivity.this, 2, email, email, "", token, userId);
                                            mListener.onFbLogin(email, token, userId);
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender,birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
//                        Toast.makeText(SplashActivity.this,"Авторизация отменена",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
//                        Toast.makeText(SplashActivity.this,"Произошла ошибка",Toast.LENGTH_LONG).show();
                        mListener.onError("Произошла ошибка");
                    }
                });

        fbAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
            }
        };

        String[] fingerprints = VKUtil.getCertificateFingerprint(mContext, mContext.getPackageName());
    }

    public void vkLogin(Activity activity){
//        VKSdk.logout();
        VKSdk.login(activity, new String[]{VKScope.EMAIL, VKScope.STATS});
    }

    public void fbActivityResult(int requestCode, int resultCode, Intent data){
        fbCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
    public boolean vkActivityResult(int requestCode, int resultCode, Intent data){
        return VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
//                Toast.makeText(SplashActivity.this, "Пользователь успешно авторизовался,"+res.email+","+res.userId
//                        +"\r\n"+res.accessToken, Toast.LENGTH_LONG).show();
//                mProgressDialog.show();
//                new ApiClient(SplashActivity.this).signup(SplashActivity.this, 1, "", "", "", res.accessToken, res.userId);
                mListener.onVkLogin(res.accessToken, res.userId);
            }
            @Override
            public void onError(VKError error) {
//                Toast.makeText(SplashActivity.this, "Произошла ошибка авторизации", Toast.LENGTH_LONG).show();
                mListener.onError("Произошла ошибка авторизации");
            }
        });
    }

    public void fbLogin(Activity activity){
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile", "user_friends", "email"));
    }

    public void okLogin(){
        mOdnoklassniki.requestAuthorization(new OkListener() {
            @Override
            public void onSuccess(final JSONObject json) {
                try {
//                        Toast.makeText(SplashActivity.this, String.format("access_token: %s", json.getString("access_token")), Toast.LENGTH_SHORT).show();
//                    mProgressDialog.show();
//                    new ApiClient(SplashActivity.this).signup(SplashActivity.this, 3, "", "", "", json.getString("access_token"), "0");
//                        new LoadFriends().execute(new Void[]{});
                    mListener.onOkLogin(json.getString("access_token"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
//                Toast.makeText(SplashActivity.this, String.format("%s: %s", "Ошибка", error), Toast.LENGTH_SHORT).show();
                mListener.onError(String.format("%s: %s", "Ошибка", error));
            }
        }, REDIRECT_URL, false, OkScope.VALUABLE_ACCESS);
    }

    private class LoadFriends extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                //непосредственный вызов метода api
                return mOdnoklassniki.request("users.getCurrentUser", null, "get");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                Log.v("APIOK", "Response on friends.get: " + result);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
