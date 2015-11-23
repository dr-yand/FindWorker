package potboiler.client.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import potboiler.client.R;
import potboiler.client.api.ApiClient;
import potboiler.client.model.Category;
import potboiler.client.model.Pot;
import potboiler.client.model.ServerResponseStatus;
import potboiler.client.util.CirclePicassoTransform;
import potboiler.client.util.CommonUtils;
import potboiler.client.util.ImageUtils;

public class CategoriesFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener,ApiClient.OnGetCategories {

    private ListView mCategoryList;
    private List<Category> mCatetories;
    private String mIdParent = "0";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        mIdParent = bundle.getString("idParent");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        initViews(view);
        getItems();
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        getActivity().setTitle("Категории");
    }

    private void getItems(){
        CommonUtils.showProgressDialog(getActivity());
        new ApiClient(getActivity()).getCategories(this, mIdParent);
    }

    private void initViews(View view){
        mCategoryList = (ListView)view.findViewById(R.id.listView);
        mCategoryList.setOnItemClickListener(this);
    }

    private void loadList(){
        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(getActivity(), R.layout.item_category, R.id.title, mCatetories){
            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                Category item = getItem(position);

                ((TextView)view.findViewById(R.id.title)).setText(item.getName());

                return view;
            }
        };
        mCategoryList.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

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
        Category category = (Category) parent.getItemAtPosition(position);
        if (mIdParent.equals("0")){
            NewPotFragment2.POT_TEMP.setCategory(category);
        }
        else{
            NewPotFragment2.POT_TEMP.setSubcategory(category);
        }

        getActivity().getSupportFragmentManager().popBackStackImmediate();
    }

    @Override
    public void onGetCategories(ServerResponseStatus responseStatus, List<Category> list) {
        CommonUtils.hideProgressDialog();
        if(responseStatus==ServerResponseStatus.OK){
            mCatetories = list;
            loadList();
        }
        else{
            Toast.makeText(getActivity(),responseStatus.toString(),Toast.LENGTH_LONG).show();
        }
    }

}