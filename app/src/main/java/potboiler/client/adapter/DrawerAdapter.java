package potboiler.client.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import potboiler.client.R;

public class DrawerAdapter extends ArrayAdapter {

    public DrawerAdapter(Context context, int resource, int textViewResourceId, Object[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView,
                        ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

//                Typeface type = Typeface.createFromAsset(getAssets(), "fonts/roboto.ttf");
//                ((TextView)view.findViewById(R.id.title)).setTypeface(type);
        String[] menuSections = getContext().getResources().getStringArray(R.array.menu_sections);
        String title = menuSections[position];
        int imageId = R.mipmap.ic_launcher;
        switch (position){
            case 0:imageId = R.drawable.my_pots;
                break;
            case 1:imageId = R.drawable.my_tasks;
                break;
            case 2:imageId = R.drawable.politics;
                break;
            case 3:imageId = R.drawable.user_license;
                break;
            case 4:imageId = R.drawable.share;
                break;
            case 5:imageId = R.drawable.feedback;
                break;
        }
        ((TextView)view.findViewById(R.id.title)).setText(title);
        ((ImageView)view.findViewById(R.id.icon)).setImageResource(imageId);

        return view;
    }
}
