package net.gsantner.opoc.ui;

import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.gsantner.markor.R;
import net.gsantner.markor.activity.DocumentActivity;
import net.gsantner.opoc.model.FileInfo;
import net.gsantner.opoc.ui.placeholder.PlaceholderContent.PlaceholderItem;

import java.io.File;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ArticleRecyclerViewAdapter extends RecyclerView.Adapter<ArticleRecyclerViewAdapter.ViewHolder> {

    private final List<FileInfo> mData;
    private Context mContext;

    public ArticleRecyclerViewAdapter(List<FileInfo> data) {
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.opoc_filesystem_item, parent, false);
        mContext = parent.getContext();
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mFileInfo = mData.get(position);
        holder.mFileNameText.setText(mData.get(position).getFileName());
        holder.mFilePathText.setText(mData.get(position).getFilePath());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) mContext;
                DocumentActivity.launch(activity, new File(holder.mFileInfo.getFilePath()), false, true, null);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mFileNameText;
        public final TextView mFilePathText;
        public FileInfo mFileInfo;

        public ViewHolder(View view) {
            super(view);
            mFileNameText = (TextView) view.findViewById(R.id.ui__filesystem_item__title);
            mFilePathText=(TextView) view.findViewById(R.id.ui__filesystem_item__description);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mFileNameText.getText() + "'";
        }
    }
}