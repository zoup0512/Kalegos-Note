package net.gsantner.opoc.ui.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;


import net.gsantner.markor.R;

import java.util.Objects;


public class EditDirDialog {

    private Dialog dialog;
    private Context context;
    private TextView titleTextView;
    private TextView cancelTextView;
    private TextView confirmTextView;
    private EditText contentEditText;

    public EditDirDialog(Context context, String title, String content) {
        this.context = context;
        init(title, content);
    }

    public EditDirDialog(Context context, int title, String content) {
        this.context = context;
        init(context.getString(title), content);
    }

    public EditDirDialog(Context context, String title, int content) {
        this.context = context;
        init(title, context.getString(content));
    }

    public EditDirDialog(Context context, int title, int content) {
        this.context = context;
        init(context.getString(title), context.getString(content));
    }

    public void setCancelListerner(View.OnClickListener listerner) {

        cancelTextView.setOnClickListener(listerner);

    }

    public void setConfirmListerner(View.OnClickListener listerner) {

        confirmTextView.setOnClickListener(listerner);

    }

    public String getTitle() {

        return titleTextView.getText().toString();

    }

    public void setTitle(String title) {

        titleTextView.setText(title);

    }

    public String getContent() {

        return contentEditText.getText().toString();

    }

    public void setContent(String content) {

        contentEditText.setText(content);

    }

    public void dismiss() {
        dialog.dismiss();
    }

    private void init(String title, String content) {

        this.dialog = new Dialog(context);
        dialog.setContentView(R.layout.edit_dir_dialog);
        Objects.requireNonNull(dialog.getWindow()).getDecorView().setPadding(0, 0, 0, 0);
        Window window = dialog.getWindow();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        window.setGravity(Gravity.CENTER | Gravity.BOTTOM);
        layoutParams.width = displayMetrics.widthPixels;
        window.setAttributes(layoutParams);
        titleTextView = dialog.findViewById(R.id.titleTextView);
        contentEditText = dialog.findViewById(R.id.contentEditText);
        cancelTextView = dialog.findViewById(R.id.cancelTextView);
        confirmTextView = dialog.findViewById(R.id.confirmTextView);
        titleTextView.setText(title);
        contentEditText.setText(content);
        contentEditText.setSelection(content.length());
        contentEditText.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

    }

    public void show() {
        dialog.show();
    }

}
