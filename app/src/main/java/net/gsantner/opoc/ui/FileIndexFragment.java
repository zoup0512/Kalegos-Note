package net.gsantner.opoc.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import net.gsantner.markor.R;
import net.gsantner.opoc.activity.GsFragmentBase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class FileIndexFragment extends GsFragmentBase {
    @BindView(R.id.content_tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.content_vp)
    ViewPager2 mViewPager2;
    private FileIndexAdapter mAdapter;
    private List<FileSysFragment> mFragments=new ArrayList<>();

    public static FileIndexFragment getInstance() {
        return new FileIndexFragment();
    }

    @Override
    public String getFragmentTag() {
        return "FileIndexFragment";
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.file_index_fragment;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new FileIndexAdapter(mFragments, getChildFragmentManager(), getLifecycle());
        mFragments.add(FileSysFragment.getInstance("Daily"));
        mFragments.add(FileSysFragment.getInstance("Blog"));
        mFragments.add(FileSysFragment.getInstance("News"));
        mViewPager2.setAdapter(mAdapter);
        new TabLayoutMediator(mTabLayout, mViewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int position) {
                tab.setText(mFragments.get(position).getTitle());
            }
        }).attach();
    }
}
