package potboiler.client;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import potboiler.client.adapter.SplashPagerAdapter;
import potboiler.client.api.ApiClient;
import potboiler.client.fragment.PotsFragment;
import potboiler.client.fragment.SpeedDialFragment;
import potboiler.client.model.ServerResponseStatus;
import potboiler.client.model.User;
import potboiler.client.util.AuthAdapter;
import potboiler.client.util.PreferenceUtils;

public class SplashActivity extends ActionBarActivity implements View.OnClickListener, ApiClient.OnSignup, AuthAdapter.AuthListener {

    private ViewPager mViewPager;

    private ImageView mPage1, mPage2, mPage3, mPage4;
    private SplashPagerAdapter mAdapter;

    private TextView info;

    private ProgressDialog mProgressDialog;

    private AuthAdapter mAuthAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        User user = PreferenceUtils.getUser(this);
        if(user.serverId>-1){
            Intent intent = new Intent(this, PotsActivity.class);
            startActivity(intent);
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuthAdapter = new AuthAdapter(this, this);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new SplashPagerAdapter(this);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(onPageChangeListener);
        mViewPager.setPageMargin(10);

        initViews();
        initDlg();
        setPage(0);
    }


    private void initDlg(){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Пожалуйста подождите...");
    }

    @Override
    protected void onResume() {
        super.onResume();
//        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        accessTokenTracker.stopTracking();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mAuthAdapter.vkActivityResult(requestCode,resultCode,data)) {
            mAuthAdapter.fbActivityResult(requestCode,resultCode,data);
        }
    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int pageSelected) {
            setPage(pageSelected);
        }

        @Override
        public void onPageScrolled(int pageSelected, float positionOffset,
                                   int positionOffsetPixel) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private void setPage(int page){
        mPage1.setSelected(false);
        mPage2.setSelected(false);
        mPage3.setSelected(false);
        mPage4.setSelected(false);
        if(page==0) {
            mPage1.setSelected(true);
            ((TextView)findViewById(R.id.title)).setText(Html.fromHtml(getString(R.string.title_splash_page1)));
        }
        if(page==1) {
            mPage2.setSelected(true);
            ((TextView)findViewById(R.id.title)).setText(Html.fromHtml(getString(R.string.title_splash_page2)));
        }
        if(page==2) {
            mPage3.setSelected(true);
            ((TextView)findViewById(R.id.title)).setText(Html.fromHtml(getString(R.string.title_splash_page3)));
        }
        if(page==3) {
            mPage4.setSelected(true);
            ((TextView)findViewById(R.id.title)).setText(Html.fromHtml(getString(R.string.title_splash_page4)));
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.signin){
            Intent intent = new Intent(this, SigninActivity.class);
            startActivity(intent);
        }
        if(v.getId()==R.id.signup){
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);
        }
        if(v.getId()==R.id.signin_fb){
            mAuthAdapter.fbLogin(this);
        }
        if(v.getId()==R.id.signin_vk){
            mAuthAdapter.vkLogin(this);
        }
        if(v.getId()==R.id.signin_ok){
            mAuthAdapter.okLogin();
        }
    }


    private void initViews(){
        mPage1 = (ImageView)findViewById(R.id.page_1);
        mPage2 = (ImageView)findViewById(R.id.page_2);
        mPage3 = (ImageView)findViewById(R.id.page_3);
        mPage4 = (ImageView)findViewById(R.id.page_4);

        ((ImageView)findViewById(R.id.signin_fb)).setOnClickListener(this);
        ((ImageView)findViewById(R.id.signin_vk)).setOnClickListener(this);
        ((ImageView)findViewById(R.id.signin_ok)).setOnClickListener(this);
        ((Button)findViewById(R.id.signin)).setOnClickListener(this);
        ((Button)findViewById(R.id.signup)).setOnClickListener(this);
    }

    @Override
    public void onFbLogin(String email, String token, String userId) {
            mProgressDialog.show();
            new ApiClient(SplashActivity.this).signup(SplashActivity.this, 2, email, email, "", token, userId);
    }

    @Override
    public void onVkLogin(String token, String userId) {
        mProgressDialog.show();
        new ApiClient(SplashActivity.this).signup(SplashActivity.this, 1, "", "", "", token, userId);
    }

    @Override
    public void onOkLogin(String token) {
        mProgressDialog.show();
        new ApiClient(SplashActivity.this).signup(SplashActivity.this, 3, "", "", "", token, "0");
    }

    @Override
    public void onError(String error) {
        Toast.makeText(SplashActivity.this, error, Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSignup(ServerResponseStatus responseStatus) {
        mProgressDialog.dismiss();
        if(responseStatus==ServerResponseStatus.OK){
            Intent intent = new Intent(this, PotsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(this,responseStatus.toString(),Toast.LENGTH_LONG).show();
        }
    }
}
