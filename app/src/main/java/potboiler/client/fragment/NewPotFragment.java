package potboiler.client.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import potboiler.client.R;

public class NewPotFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView mChatsList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_pot, container, false);

        initViews(view);
//        loadList();
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        getActivity().setTitle("Создать");
    }

    private void initViews(View view){
//        mChatsList = (ListView)view.findViewById(R.id.listView);
        ((ImageView)view.findViewById(R.id.new_pot)).setOnClickListener(this);
    }

    private void loadList(){
        String[] data = new String[]{"1","1","1",};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.item_chat, R.id.title, data){
            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                return view;
            }
        };
        mChatsList.setAdapter(adapter);
        mChatsList.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.new_pot){
            showNextPage();
        }
    }

    private void showNextPage(){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        Fragment fragment = new NewPotFragment2();
        fragment.setArguments(bundle);
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack("NewPotFragment2");
        transaction.commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem newPotMenuItem = menu.add("Создать");
        newPotMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        newPotMenuItem.setIcon(R.drawable.new_pot_item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        showNextPage();
        return super.onOptionsItemSelected(item);
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
}