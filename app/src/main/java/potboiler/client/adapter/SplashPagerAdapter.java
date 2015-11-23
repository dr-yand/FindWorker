package potboiler.client.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import potboiler.client.R;

public class SplashPagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;

    public SplashPagerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewList = new ArrayList<>();
        for(int layoutId:mLayouts){
            View itemView = mLayoutInflater.inflate(layoutId, null, false);
            mViewList.add(itemView);
        }
    }

    private int[] mLayouts = {R.layout.fragment_splash_1,R.layout.fragment_splash_2,
            R.layout.fragment_splash_3,R.layout.fragment_splash_4};

    private List<View> mViewList;

    @Override
    public int getCount() {
        return mLayouts.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mViewList.get(position);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

        /*@Override
        public int getCount() {
            return 4;
        }

        public int getStartIndex(){
            return layouts.length*1000;
        }

        public int getCurrentPosition(int position){
            return position%layouts.length;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
//            int currentPositon = position%layouts.length;
            LayoutInflater layoutInflater = (LayoutInflater) container
                    .getContext().getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);

            View view = null;
            if (position <= container.getChildCount() - 1)
                view = container.getChildAt(position);
            if (view != null)
                return view;

            view = layoutInflater.inflate(layouts[position], null);
            ((ViewPager) container).addView(view, position);
            return view;
        }

        @Override
        public void destroyItem(View view, int position, Object object) {
            ((ViewPager) view).removeView((View) object);

        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);

        }*/

}