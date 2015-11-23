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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Date;

import potboiler.client.R;
import potboiler.client.api.ApiClient;
import potboiler.client.model.PotTemp;
import potboiler.client.model.ServerResponseStatus;
import potboiler.client.util.CommonUtils;

public class DateFragment extends Fragment {

    private TimePicker mTimePicker;
    private MaterialCalendarView mDatePicker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_date, container, false);

        initViews(view);
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        getActivity().setTitle("Дата и время");
    }

    private void initViews(View view){
        mTimePicker = (TimePicker)view.findViewById(R.id.timePicker);
        mTimePicker.setIs24HourView(true);
        mDatePicker = (MaterialCalendarView)view.findViewById(R.id.datePicker);
        mDatePicker.setSelectedDate(new Date());
    }

    private void createDlg(){

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
        int hour = mTimePicker.getCurrentHour();
        int minutes = mTimePicker.getCurrentMinute();
        time.setHours(hour);
        time.setMinutes(minutes);

        Date date = mDatePicker.getSelectedDate().getDate();

        NewPotFragment2.POT_TEMP.setTime(time);
        NewPotFragment2.POT_TEMP.setDate(date);

        getActivity().getSupportFragmentManager().popBackStackImmediate();
        return super.onOptionsItemSelected(item);
    }

}