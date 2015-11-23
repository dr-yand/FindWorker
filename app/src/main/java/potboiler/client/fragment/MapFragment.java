package potboiler.client.fragment;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import potboiler.client.R;

public class MapFragment extends Fragment implements OnCameraChangeListener, OnMarkerClickListener, GoogleMap.OnMapClickListener {

	private GoogleMap mMap;
	private Marker mMarkerA, mMarkerB;
	private EditText mAddressA, mAddressB;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_map, container, false);

		setHasOptionsMenu(true);
		setUpMapIfNeeded();
		initViews(view);

        return view;
    }

	private void initViews(View view){
		mAddressA = (EditText)view.findViewById(R.id.address1);
		mAddressB = (EditText)view.findViewById(R.id.address2);

		mAddressA.setOnEditorActionListener(new EditText.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				mMarkerA = addMarkerByAddress(v.getText().toString(),mMarkerA,"Адрес А", BitmapDescriptorFactory.HUE_RED);
				return false;
			}
		});
		mAddressB.setOnEditorActionListener(new EditText.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				mMarkerB = addMarkerByAddress(v.getText().toString(),mMarkerB,"Адрес Б", BitmapDescriptorFactory.HUE_BLUE);
				return false;
			}
		});
	}

	public void setUpMapIfNeeded() {
		if (mMap == null) {
			mMap = ((SupportMapFragment) getChildFragmentManager()
					.findFragmentById(R.id.maps)).getMap();
			if (mMap != null)
				setUpMap();
		}
	}

	private void setUpMap() {
		mMap.setMyLocationEnabled(true);
		mMap.setOnMapClickListener(this);
//		mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("My Home").snippet("Home Address"));
//		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,
//				longitude), 12.0f));
	}


//	private static HashMap<Marker, Image> mMarkersMap;
	
	@Override
	public void onResume() {	
		super.onResume();

		getActivity().setTitle("Отметьте адрес");

//		GoogleMap map = ((SupportMapFragment) getFragmentManager()
//				.findFragmentById(R.id.maps)).getMap();
		
//		GoogleMap map=getMap();
//		map.setOnMarkerClickListener(this);
		
		BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);
		
//		Marker m  = map.addMarker(new MarkerOptions()
//		   .position(new LatLng(51, 39))
//		   .icon(icon)
//		   .title("Воронеж"));
		
//		map.setOnCameraChangeListener(this);


//		getActivity().setTitle("Адрес");
		
		loadData();
	}

	public void loadData(){
//		mMarkersMap = new HashMap<Marker, Image>();
		
		/*DbCommonHelper dbHelper = new DbCommonHelper(getActivity());
		ImageDbAdapter imageDbAdapter = new ImageDbAdapter();
		List<Image> items = imageDbAdapter.getPostImages(dbHelper.open(), false, 0, false);
		GoogleMap map=getMap();
		map.clear();
		for (Image i:items){
			Log.i("lat", i.getLat());
			if(i.getLat()!=null&&!i.getLat().equals("0")){
				String file = FileUtils.getThumbnailsPath(getActivity()) + i.getFilename();
				BitmapFactory.Options o = new BitmapFactory.Options();
				Bitmap bitmap = BitmapFactory.decodeFile(file, o);
				int w = bitmap.getWidth()/2;
				int h = bitmap.getHeight()/2;
				bitmap = Bitmap.createScaledBitmap(bitmap, w, h, false);
				BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bitmap);
				
				Marker m  = map.addMarker(new MarkerOptions()
				   .position(new LatLng(Float.parseFloat(i.getLat()), Float.parseFloat(i.getLon())))
				   .icon(icon));
				 
				mMarkersMap.put(m, i);
			}
		}
		dbHelper.close();*/
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		
	}
	 
	@Override
	public void onCameraChange(CameraPosition position) {
//		GoogleMap map=getMap();
//		if(map!=null){
//			LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
//			Log.i("bounds!!!!!!!!1", bounds.toString());
//		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		/*Image image = mMarkersMap.get(marker);
		Intent intent = new Intent(getActivity(), PhotoviewerActivity.class);
		intent.putExtra("image_id", image.getId());
		intent.putExtra("ind", MainActivity.MAP);
		getActivity().startActivityForResult(intent, MainActivity.PHOTO_DETAIL);*/
		return false;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		MenuItem newPotMenuItem = menu.add("Выбрать");
		newPotMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Date time = new Date();


		getActivity().getSupportFragmentManager().popBackStackImmediate();
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onMapClick(LatLng latLng) {
		if(mAddressA.isFocused()){
			/*if(mMarkerA!=null)
				mMarkerA.remove();
			mMarkerA = mMap.addMarker(new MarkerOptions()
					.position(latLng)
					.title("Адрес А")
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
			mAddressA.setText(getAddress(latLng.latitude, latLng.longitude));*/
			mMarkerA=addMarker(mMarkerA,latLng.latitude, latLng.longitude,"Адрес А",BitmapDescriptorFactory.HUE_RED,mAddressA);
		}
		if(mAddressB.isFocused()){
			/*if(mMarkerB!=null)
				mMarkerB.remove();
			mMarkerB = mMap.addMarker(new MarkerOptions()
					.position(latLng)
					.title("Адрес Б")
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
			mAddressB.setText(getAddress(latLng.latitude, latLng.longitude));*/
			mMarkerB=addMarker(mMarkerB,latLng.latitude, latLng.longitude,"Адрес Б",BitmapDescriptorFactory.HUE_BLUE,mAddressB);
		}
	}

	private Marker addMarker(Marker marker, Double lat, Double lon, String title, float color, EditText address){
		if(marker!=null)
			marker.remove();
		marker = mMap.addMarker(new MarkerOptions()
				.position(new LatLng(lat, lon))
				.title(title)
				.icon(BitmapDescriptorFactory
						.defaultMarker(color)));
		if(address!=null)
			address.setText(getAddress(lat, lon));
		return marker;
	}

	private String getAddress(Double lat, Double lon){
		String result = "";
		try {
			List<Address> addresses;
			Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

			addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

			String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
			String city = addresses.get(0).getLocality();
			String state = addresses.get(0).getAdminArea();
			String country = addresses.get(0).getCountryName();
			String postalCode = addresses.get(0).getPostalCode();
			String knownName = addresses.get(0).getFeatureName();

			if(address!=null)
				result += address;
			if(city!=null)
				result+=", "+city;
			if(state!=null)
				result+=", "+state;
			if(country!=null)
				result+=", "+country;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}

	private Marker addMarkerByAddress(String address, Marker marker, String title, float color){
		try {
			Geocoder geocoder = new Geocoder(getActivity());
			List<Address> addresses;
			addresses = geocoder.getFromLocationName(address, 1);
			if (addresses.size() > 0) {
				double latitude = addresses.get(0).getLatitude();
				double longitude = addresses.get(0).getLongitude();
				marker = addMarker(marker,latitude, longitude,title, color, null);
				mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,
				longitude), mMap.getCameraPosition().zoom));
			}
		}
		catch (Exception e){

		}
		return  marker;
	}
}
