package potboiler.client;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import potboiler.client.adapter.DrawerAdapter;
import potboiler.client.api.ApiClient;
import potboiler.client.fragment.ChatsFragment;
import potboiler.client.fragment.FeedbackFragment;
import potboiler.client.fragment.LicenseFragment;
import potboiler.client.fragment.MapFragment;
import potboiler.client.fragment.NewPotFragment;
import potboiler.client.fragment.PoliticsFragment;
import potboiler.client.fragment.PotsFragment;
import potboiler.client.fragment.ProfileFragment;
import potboiler.client.model.ServerResponseStatus;
import potboiler.client.model.User;
import potboiler.client.util.FileUtils;
import potboiler.client.util.ImageUtils;
import potboiler.client.util.PreferenceUtils;

public class PotsActivity extends ActionBarActivity implements View.OnClickListener, ApiClient.OnNewPot {

    private ProgressDialog mProgressDialog;

    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] mMenuItems;
    private ListView mCommandsList;

    public static final int SELECT_CAMERA = 1;

    private ImageView mPotsImage, mChatsImage, mNewPotImage, mMapImage, mProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pots);

        initViews();
        initDlg();
        initMenu();
        createDrawerMenu();

        selectItem(0);
    }

    private void createDrawerMenu(){

        mMenuItems = getResources().getStringArray(R.array.menu_sections);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer = (LinearLayout)findViewById(R.id.left_drawer);
        mCommandsList = (ListView) findViewById(R.id.commands);

        User user = PreferenceUtils.getUser(this);
        ((TextView)findViewById(R.id.name)).setText(user.username);

        Bitmap bitmap = ImageUtils.getImageFromFile(this, FileUtils.getImagesPath(this) + user.serverId);
        if(bitmap!=null) {
        /*Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        BitmapShader shader = new BitmapShader (bitmap,  Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        paint.setAntiAlias(true);
        Canvas c = new Canvas(circleBitmap);
        c.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);*/

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

            ((ImageView) findViewById(R.id.avatar)).setImageBitmap(circleBitmap);
        }

        mDrawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));

        DrawerAdapter adapter = new DrawerAdapter(this, R.layout.item_drawer_list, R.id.title, mMenuItems);
        mCommandsList.setAdapter(adapter);
        mCommandsList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.string.pots,
                R.string.pots
        ) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                syncActionBarArrowState();
//                invalidateOptionsMenu();
//                mDrawerToggle.syncState();
//                setRefreshMenu();
            }

            public void onDrawerOpened(View drawerView) {
//                super.onDrawerClosed(drawerView);
                super.onDrawerOpened(drawerView);
                mDrawerToggle.setDrawerIndicatorEnabled(true);
//                invalidateOptionsMenu();
//                mDrawerToggle.syncState();
//                setRefreshMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                float xPositionOpenDrawer = mCommandsList.getWidth();
                float xPositionWindowContent = (slideOffset * xPositionOpenDrawer);
                FrameLayout mHostFragment = (FrameLayout)findViewById(R.id.content_frame);
                mHostFragment.setX(xPositionWindowContent);
                getActionBarView().setX(xPositionWindowContent);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        getSupportFragmentManager().addOnBackStackChangedListener(mOnBackStackChangedListener);

        findViewById(R.id.signout).setOnClickListener(this);
    }

    private FragmentManager.OnBackStackChangedListener
            mOnBackStackChangedListener = new FragmentManager.OnBackStackChangedListener() {
        @Override
        public void onBackStackChanged() {
            syncActionBarArrowState();
        }
    };

    @Override
    protected void onDestroy() {
        getSupportFragmentManager().removeOnBackStackChangedListener(mOnBackStackChangedListener);
        super.onDestroy();
    }

    private void syncActionBarArrowState() {
        int backStackEntryCount =
                getSupportFragmentManager().getBackStackEntryCount();
        mDrawerToggle.setDrawerIndicatorEnabled(backStackEntryCount == 0);
    }

    public View getActionBarView() {
        Window window = this.getWindow();
        final View decorView = window.getDecorView();
        final String packageName =  getPackageName();
        final int resId = this.getResources().getIdentifier("action_bar_container", "id", packageName);
        final View actionBarView = decorView.findViewById(resId);
        return actionBarView;
    }

    private void initMenu(){
        setTitle(R.string.pots);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setElevation(0);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
//        mDrawerLayout.openDrawer(mDrawer);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onNewPot(ServerResponseStatus responseStatus) {
        mProgressDialog.dismiss();
        if(responseStatus==ServerResponseStatus.OK){
        }
        else{
            Toast.makeText(this, responseStatus.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        setTitle(mMenuItems[position]);
        mCommandsList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawer);
        Fragment fragment = null;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();

        switch (position){
            case 0: fragment = new PotsFragment();
                break;
            case 1: fragment = new PotsFragment();
                break;
            case 2: fragment = new PoliticsFragment();
                break;
            case 3: fragment = new LicenseFragment();
                break;
            case 4:
                shareTextUrl();
                break;
            case 5: fragment = new FeedbackFragment();
                break;
        }

        if(fragment!=null) {
            clearBackStake();
            clearMenuSelector();
            fragment.setArguments(bundle);
            transaction.replace(R.id.container, fragment);
        }
        transaction.commit();
        invalidateOptionsMenu();
    }

    private void shareTextUrl() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "Potboiler");
        share.putExtra(Intent.EXTRA_TEXT, "Установи классное приложение Potboiler https://play.google.com/store/apps/details?id=potboiler.client");

        startActivity(Intent.createChooser(share, "Рассказать друзьям"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (mDrawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
        if (mDrawerToggle.isDrawerIndicatorEnabled() &&
                mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else if (item.getItemId() == android.R.id.home &&
                getSupportFragmentManager().popBackStackImmediate()) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
//        return super.onOptionsItemSelected(item);
    }

    private void initDlg(){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Пожалуйста подождите...");
    }


    private void initViews(){
        mPotsImage = (ImageView)findViewById(R.id.pots_image);
        mChatsImage = (ImageView)findViewById(R.id.chats_image);
        mNewPotImage = (ImageView)findViewById(R.id.new_pot_image);
        mMapImage = (ImageView)findViewById(R.id.map_image);
        mProfileImage = (ImageView)findViewById(R.id.profile_image);
        findViewById(R.id.pots).setOnClickListener(this);
        findViewById(R.id.chats).setOnClickListener(this);
        findViewById(R.id.map).setOnClickListener(this);
        findViewById(R.id.new_pot).setOnClickListener(this);
        findViewById(R.id.profile).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.signout){
            PreferenceUtils.removeUser(this);
            finish();
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
        }
        if(v.getId()==R.id.map){
            clearBackStake();
            showMap();
            clearMenuSelector();
            mMapImage.setSelected(true);
        }
        if(v.getId()==R.id.new_pot){
            clearBackStake();
            clearMenuSelector();
            mNewPotImage.setSelected(true);

            setTitle("Создать");
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            Fragment fragment = new NewPotFragment();
            fragment.setArguments(bundle);
            transaction.replace(R.id.container, fragment);
            transaction.commit();

//            mProgressDialog.show();
////            showCamera();
//            new ApiClient(this).newPot(this, "asdf");
//            clearMenuSelector();
        }
        if(v.getId()==R.id.chats){
            clearBackStake();
            clearMenuSelector();
            mChatsImage.setSelected(true);

            setTitle("Чаты");
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            Fragment fragment = new ChatsFragment();
            fragment.setArguments(bundle);
            transaction.replace(R.id.container, fragment);
            transaction.commit();
        }
        if(v.getId()==R.id.profile){
            clearBackStake();
            clearMenuSelector();
            mProfileImage.setSelected(true);

            setTitle("Профиль");
//            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//            FragmentManager fm = getSupportFragmentManager();
//            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
//                fm.popBackStack();
//            }
//            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentByTag("fragment")).commit();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            Fragment fragment = new ProfileFragment();
            fragment.setArguments(bundle);
            transaction.replace(R.id.container, fragment);
            transaction.commit();
//            clearBackStake();
        }
        if(v.getId()==R.id.pots){
            clearBackStake();
            clearMenuSelector();
            mPotsImage.setSelected(true);

            setTitle("Заказы");
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            Fragment fragment = new PotsFragment();
            fragment.setArguments(bundle);
            transaction.replace(R.id.container, fragment);
            transaction.commit();
        }
    }

    private void clearBackStake(){
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    private void clearMenuSelector(){
        mMapImage.setSelected(false);
        mNewPotImage.setSelected(false);
        mPotsImage.setSelected(false);
        mProfileImage.setSelected(false);
        mChatsImage.setSelected(false);
    }

    public void showMap(){
        MapFragment fragment = new MapFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    public void showCamera(){
        Intent intent = new Intent(this, PhotoshotActivity.class);
        startActivityForResult(intent, SELECT_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_CAMERA) {
                try {
                    String[] files = data.getStringArrayExtra("files");
                    if(files.length>0){
//                        sendData(files[0]);
                    }
                } catch (Exception e) {
                }
            }
        }
    }
}
