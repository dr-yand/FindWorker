package potboiler.client.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.edmodo.rangebar.RangeBar;

import java.util.Date;
import java.util.List;

import potboiler.client.R;
import potboiler.client.api.ApiClient;
import potboiler.client.model.Category;
import potboiler.client.model.ServerResponseStatus;
import potboiler.client.util.CommonUtils;

public class CostFragment extends Fragment{

    private RangeBar mRangebar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cost, container, false);

        initViews(view);
        return view;
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
    public void onResume(){
        super.onResume();
        getActivity().setTitle("Стоимость");
    }

    private void initViews(View view){
//        mRangebar = (RangeBar) view.findViewById(R.id.rangebar1);
    }


}