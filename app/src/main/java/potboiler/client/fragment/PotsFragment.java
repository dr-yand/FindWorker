package potboiler.client.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ComponentCallbacks;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import potboiler.client.PotsActivity;
import potboiler.client.R;
import potboiler.client.api.ApiClient;
import potboiler.client.model.Pot;
import potboiler.client.model.ServerResponseStatus;
import potboiler.client.util.CirclePicassoTransform;
import potboiler.client.util.CommonUtils;
import potboiler.client.util.ImageUtils;

public class PotsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, ApiClient.OnGetPots {

    private ListView mPotsList;
    private Set<Integer> mExpandedPots = new HashSet<>();
    private List<Pot> mPotList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pots, container, false);

        initViews(view);
        loadPots();
        return view;
    }

    private void loadPots(){
        CommonUtils.showProgressDialog(getActivity());
        new ApiClient(getActivity()).getPots(this);
    }

    private void initViews(View view){
        mPotsList = (ListView)view.findViewById(R.id.listView);
    }

    private void loadList(){
        ArrayAdapter<Pot> adapter = new ArrayAdapter<Pot>(getActivity(), R.layout.item_pot, R.id.title, mPotList){
            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                final Pot pot = getItem(position);
                ImageView imageView = (ImageView)view.findViewById(R.id.avatar);

                Picasso.with(getActivity())
                        .load(pot.getAvatar())
                        .placeholder(R.drawable.ic_user_incognito_50_1)
                        .error(R.drawable.ic_user_incognito_50_1)
                        .resize(ImageUtils.dpToPx(getContext(),50),ImageUtils.dpToPx(getContext(),50))
                        .centerCrop()
                        .transform(new CirclePicassoTransform(getContext()))
                        .into(imageView);
                imageView.setVisibility(View.VISIBLE);

                ((TextView)view.findViewById(R.id.title)).setText(pot.getNamepot());
                ((TextView)view.findViewById(R.id.worker)).setText(pot.getName());

                RatingBar ratingBar = (RatingBar)view.findViewById(R.id.ratingBar);
                LayerDrawable drawable = (LayerDrawable) ratingBar.getProgressDrawable();
                /*Drawable progress = drawable.getDrawable(2);
                DrawableCompat.setTint(progress, getResources().getColor(R.color.rating));
                progress = drawable.getDrawable(1);
                DrawableCompat.setTintMode(progress, PorterDuff.Mode.DST_ATOP);
                DrawableCompat.setTint(progress, getResources().getColor(R.color.rating));
                DrawableCompat.setTintMode(progress, PorterDuff.Mode.SRC_ATOP);
                DrawableCompat.setTint(progress, getResources().getColor(R.color.rating));
                progress = drawable.getDrawable(0);
                DrawableCompat.setTint(progress, getResources().getColor(R.color.rating));*/
                DrawableCompat.setTint(drawable, Color.WHITE);
                ratingBar.setProgressDrawable(drawable);

                LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
                stars.getDrawable(2).setColorFilter(0xFFFF9C00, PorterDuff.Mode.SRC_ATOP);

                if(mExpandedPots.contains(position))
                    view.findViewById(R.id.description).setVisibility(View.VISIBLE);
                else
                    view.findViewById(R.id.description).setVisibility(View.GONE);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        Bundle bundle = new Bundle();
                        bundle.putString("userId", "0");
                        bundle.putParcelable("pot", pot);
                        Fragment fragment = new ProfileFragment();
                        fragment.setArguments(bundle);
                        transaction.replace(R.id.container, fragment, "ProfileFragment");
//                        transaction.addToBackStack("ProfileFragment");
                        transaction.commit();
                    }
                });
                return view;
            }
        };
        mPotsList.setAdapter(adapter);
        mPotsList.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void onResume() {
        super.onResume();

        getActivity().setTitle("Заказы");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        getActivity().setTitle("Детали");
        /*FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        Fragment fragment = new PotFragment();
        fragment.setArguments(bundle);
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack("tr");
        transaction.commit();*/
        if(mExpandedPots.contains(position))
            mExpandedPots.remove(position);
        else
            mExpandedPots.add(position);
        ((BaseAdapter)parent.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onGetPots(ServerResponseStatus responseStatus, List<Pot> potList) {
        CommonUtils.hideProgressDialog();
        if(responseStatus==ServerResponseStatus.OK){
            mPotList = potList;
            loadList();
        }
        else{
            Toast.makeText(getActivity(),responseStatus.toString(),Toast.LENGTH_LONG).show();
        }
    }
}