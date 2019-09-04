package com.example.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class DdaPendingFragment extends Fragment {

    TabLayout mtablayout;
    Toolbar mtoolbar;
    TabItem assigned;
    TabItem notassigned;
    TabPageDdaAdapter mpageAdapter;
    ViewPager page;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending,container,false);
        mtablayout = view.findViewById(R.id.tab);
        assigned = view.findViewById(R.id.assigned);
        notassigned = view.findViewById(R.id.notassigned);
        page = view.findViewById(R.id.viewPage);
        mpageAdapter = new TabPageDdaAdapter(getChildFragmentManager(),mtablayout.getTabCount());
        page.setAdapter(mpageAdapter);

        /*mtabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mtabLayout));*/
        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
