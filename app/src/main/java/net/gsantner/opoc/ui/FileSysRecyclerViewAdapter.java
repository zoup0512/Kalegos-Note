package net.gsantner.opoc.ui;

import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.gsantner.markor.R;
import net.gsantner.opoc.bean.MdFile;

import java.io.File;
import java.util.List;


public class FileSysRecyclerViewAdapter extends RecyclerView.Adapter<FileSysRecyclerViewAdapter.ViewHolder> {

    private final List<MdFile> mData;
    private Context mContext;

    public FileSysRecyclerViewAdapter(List<MdFile> data) {
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
        holder.mdFile = mData.get(position);
        holder.mFileNameText.setText(holder.mdFile.getName());
        holder.mFilePathText.setText(mData.get(position).getFilePath());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) mContext;
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
        public MdFile mdFile;

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