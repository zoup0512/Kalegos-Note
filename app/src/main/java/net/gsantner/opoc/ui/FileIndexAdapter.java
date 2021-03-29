package net.gsantner.opoc.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FileIndexAdapter extends FragmentStateAdapter {
    private List<ArticleFragment> mFragments;
    public FileIndexAdapter(@NonNull @NotNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public FileIndexAdapter(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public FileIndexAdapter(List<ArticleFragment> fragments,@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle){
        this(fragmentManager, lifecycle);
        mFragments=fragments;

    }

    @NotNull
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return mFragments.size();
    }
}
