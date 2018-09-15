//package com.example.android.vcare.pending;
//
//import android.graphics.Typeface;
//import android.os.Bundle;
//import android.support.design.widget.TabLayout;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.example.android.vcare.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class Track_member_device extends Fragment {
//    private TabLayout tabLayout;
//    private ViewPager viewPager;
//
//    public Track_member_device() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View rootView = inflater.inflate(R.layout.fragment_track_member_device, container, false);
//
//        setHasOptionsMenu(true);
//        viewPager = (ViewPager)rootView. findViewById(R.id.viewpager);
//
//
//        if (viewPager != null) {
//            setupViewPager(viewPager);
//        }
//
//        tabLayout = (TabLayout)rootView.findViewById(R.id.tabs);
//
//        int betweenSpace = 100;
//
//        ViewGroup slidingTabStrip = (ViewGroup) tabLayout.getChildAt(0);
//
//
//        tabLayout.setupWithViewPager(viewPager);
//        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SourceSansPro-Regular.otf");
//        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
//        int tabsCount = vg.getChildCount();
//        for (int j = 0; j < tabsCount; j++) {
//            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
//            int tabChildsCount = vgTab.getChildCount();
//            for (int i = 0; i < tabChildsCount; i++) {
//                View tabViewChild = vgTab.getChildAt(i);
//                if (tabViewChild instanceof TextView) {
//                    ((TextView) tabViewChild).setTypeface(tf);
//                }
//            }
//
//        }
//
//        return rootView;
//    }
//
//    private void setupViewPager(ViewPager viewPager) {
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
//        adapter.addFragment(new Memberss(),("M E M B E R S"));
//        adapter.addFragment(new Maps(),("M A P"));
//        viewPager.setAdapter(adapter);
//    }
//
//    class ViewPagerAdapter extends FragmentPagerAdapter {
//        private final List<Fragment> mFragmentList = new ArrayList<>();
//        private final List<String> mFragmentTitleList = new ArrayList<>();
//
//        public ViewPagerAdapter(FragmentManager manager) {
//            super(manager);
//        }
//
//        @Override
//        public android.support.v4.app.Fragment getItem(int position) {
//            return mFragmentList.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return mFragmentList.size();
//        }
//
//        public void addFragment(Fragment fragment, String title) {
//            mFragmentList.add(fragment);
//            mFragmentTitleList.add(title);
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return mFragmentTitleList.get(position);
//        }
//    }
//
//
//}
//
