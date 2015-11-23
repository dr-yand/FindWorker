package potboiler.client.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.transform.Templates;

import potboiler.client.R;
import potboiler.client.api.ApiClient;
import potboiler.client.model.Pot;
import potboiler.client.model.PotTemp;
import potboiler.client.model.ServerResponseStatus;
import potboiler.client.util.CommonUtils;

public class NewPotFragment2 extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, ApiClient.OnNewPot {

    public static PotTemp POT_TEMP;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        POT_TEMP = new PotTemp();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_pot_2, container, false);

        initViews(view);
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        getActivity().setTitle("Создать");
    }

    private void initViews(View view){
        ((Button)view.findViewById(R.id.show_advanced_options)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.create)).setOnClickListener(this);
        ((LinearLayout)view.findViewById(R.id.select_category)).setOnClickListener(this);
        ((LinearLayout)view.findViewById(R.id.select_subcategory)).setOnClickListener(this);
        ((LinearLayout)view.findViewById(R.id.select_date)).setOnClickListener(this);
        ((LinearLayout)view.findViewById(R.id.select_address)).setOnClickListener(this);
        ((LinearLayout)view.findViewById(R.id.select_cost)).setOnClickListener(this);

//        ((RangeSeekBar)view.findViewById(R.id.age)).set

        if(POT_TEMP.getCategory()!=null)
            ((TextView)view.findViewById(R.id.category)).setText(POT_TEMP.getCategory().getName());
        if(POT_TEMP.getSubcategory()!=null)
            ((TextView)view.findViewById(R.id.subcategory)).setText(POT_TEMP.getSubcategory().getName());
        if(POT_TEMP.getDate()!=null) {
            String datetime = "";
            datetime+=new SimpleDateFormat("dd.MM.yyyy").format(POT_TEMP.getDate());
            datetime+=" "+new SimpleDateFormat("HH:mm").format(POT_TEMP.getTime());
            ((TextView) view.findViewById(R.id.date)).setText(datetime);
        }
    }

    private void createDlg(){

    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.show_advanced_options){
            ((LinearLayout)getView().findViewById(R.id.advanced_options_layout)).setVisibility(View.VISIBLE);
            ((Button)getView().findViewById(R.id.show_advanced_options)).setVisibility(View.GONE);
        }
        if(v.getId()==R.id.create){
            if(POT_TEMP.getCategory()==null) {
                Toast.makeText(getContext(), "Введите данные", Toast.LENGTH_LONG).show();
            }
            else {
                CommonUtils.showProgressDialog(getActivity());
                new ApiClient(getContext()).newPot(this, POT_TEMP);
            }
        }
        if(v.getId()==R.id.select_category){
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putString("idParent", "0");
            Fragment fragment = new CategoriesFragment();
            fragment.setArguments(bundle);
            transaction.replace(R.id.container, fragment);
            transaction.addToBackStack("tr");
            transaction.commit();
        }
        if(v.getId()==R.id.select_date){
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            Fragment fragment = new DateFragment();
            fragment.setArguments(bundle);
            transaction.replace(R.id.container, fragment);
            transaction.addToBackStack("tr");
            transaction.commit();
        }
        if(v.getId()==R.id.select_address){
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            Fragment fragment = new MapFragment();
            fragment.setArguments(bundle);
            transaction.replace(R.id.container, fragment);
            transaction.addToBackStack("tr");
            transaction.commit();
        }
        if(v.getId()==R.id.select_cost){
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            Fragment fragment = new CostFragment();
            fragment.setArguments(bundle);
            transaction.replace(R.id.container, fragment);
            transaction.addToBackStack("tr");
            transaction.commit();
        }
        if(v.getId()==R.id.select_subcategory){
            if(POT_TEMP.getCategory()==null){
                Toast.makeText(getActivity(),"Выберите категорию", Toast.LENGTH_LONG).show();
            }
            else {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("idParent", POT_TEMP.getCategory().getId());
                Fragment fragment = new CategoriesFragment();
                fragment.setArguments(bundle);
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack("tr");
                transaction.commit();
            }
        }
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
    }

    @Override
    public void onNewPot(ServerResponseStatus responseStatus) {
        CommonUtils.hideProgressDialog();
        if(responseStatus==ServerResponseStatus.OK){
            getActivity().getSupportFragmentManager().popBackStackImmediate();
        }
        else{
            Toast.makeText(getActivity(),responseStatus.toString(),Toast.LENGTH_LONG).show();
        }
    }
}