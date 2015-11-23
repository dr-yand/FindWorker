package potboiler.client.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import potboiler.client.R;

public class PoliticsFragment extends Fragment implements View.OnClickListener {

    private ListView mPotsList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_politics, container, false);

        initViews(view);
        return view;
    }

    private void initViews(View view){
    }

    @Override
    public void onClick(View v) {

    }
}