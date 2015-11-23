package potboiler.client.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.HashSet;
import java.util.Set;

import potboiler.client.R;

public class ChatsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView mChatsList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        initViews(view);
        loadList();
        return view;
    }

    private void initViews(View view){
        mChatsList = (ListView)view.findViewById(R.id.listView);
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