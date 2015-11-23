package potboiler.client.fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.squareup.picasso.Picasso;

import potboiler.client.R;
import potboiler.client.api.ApiClient;
import potboiler.client.model.Pot;
import potboiler.client.model.ServerResponseStatus;
import potboiler.client.model.User;
import potboiler.client.util.CirclePicassoTransform;
import potboiler.client.util.CommonUtils;
import potboiler.client.util.FileUtils;
import potboiler.client.util.ImageUtils;
import potboiler.client.util.PreferenceUtils;

public class ProfileFragment extends Fragment implements View.OnClickListener, ApiClient.OnGetUserInfo {

    private ListView mPotsList;
    private String mUserId;
    private Pot mPot;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null){
            mUserId = bundle.getString("userId");
            mPot = bundle.getParcelable("pot");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initViews(view);
        getUserInfo();
        return view;
    }

    private void getUserInfo(){
        CommonUtils.showProgressDialog(getActivity());
        new ApiClient(getActivity()).getUserInfo(this, null);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(mPot==null)
            getActivity().setTitle("Профиль");
        else
            getActivity().setTitle(mPot.getName());
    }

    private void initViews(View view){
        if(mPot==null) {
            User user = PreferenceUtils.getUser(getActivity());
            ((TextView) view.findViewById(R.id.name)).setText(user.username);

            Bitmap bitmap = ImageUtils.getImageFromFile(getActivity(), FileUtils.getImagesPath(getActivity()) + user.serverId);
            if (bitmap != null) {
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

                ((ImageView) view.findViewById(R.id.avatar)).setImageBitmap(circleBitmap);
                ((TextView)view.findViewById(R.id.name)).setText(user.username);
            }
        }
        else{
            Picasso.with(getActivity())
                    .load(mPot.getAvatar())
                    .placeholder(R.drawable.ic_user_incognito)
                    .error(R.drawable.ic_user_incognito)
//                    .resize(ImageUtils.dpToPx(getContext(),50),ImageUtils.dpToPx(getContext(),50))
//                    .centerCrop()
                    .transform(new CirclePicassoTransform(getContext()))
                    .into(((ImageView) view.findViewById(R.id.avatar)));
            ((TextView)view.findViewById(R.id.name)).setText(mPot.getName());
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onGetUserInfo(ServerResponseStatus responseStatus) {
        CommonUtils.hideProgressDialog();
        if(responseStatus==ServerResponseStatus.OK){
        }
        else{
            Toast.makeText(getActivity(), responseStatus.toString(), Toast.LENGTH_LONG).show();
        }
    }
}