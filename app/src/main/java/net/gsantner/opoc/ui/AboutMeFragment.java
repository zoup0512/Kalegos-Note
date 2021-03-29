package net.gsantner.opoc.ui;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.gsantner.markor.R;
import net.gsantner.opoc.activity.GsFragmentBase;

import org.jetbrains.annotations.NotNull;

public class AboutMeFragment extends GsFragmentBase {

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView aboutMeText = view.findViewById(R.id.about_me_content);
        aboutMeText.setText(Html.fromHtml(getString(R.string.about_me_content)));
    }

    @Override
    public String getFragmentTag() {
        return "AboutMe";
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.about_me_fragment;
    }

    public static AboutMeFragment getInstance() {
        return new AboutMeFragment();
    }

    final String ABOUT_ME = "Nice to meet you!" + "<br>" + "github:<strong>" + "https://github.com/zoup0512/Kalegos-Note" + "</strong>";
}
