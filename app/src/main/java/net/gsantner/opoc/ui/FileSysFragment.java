package net.gsantner.opoc.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.gsantner.markor.R;
import net.gsantner.opoc.bean.Dir;
import net.gsantner.opoc.bean.MdFile;
import net.gsantner.opoc.model.FileInfo;
import net.gsantner.opoc.ui.treeview.DirItem;
import net.gsantner.opoc.ui.treeview.DirectoryNodeBinder;
import net.gsantner.opoc.ui.treeview.FileItem;
import net.gsantner.opoc.ui.treeview.FileNodeBinder;
import net.gsantner.opoc.ui.treeview.TreeNode;
import net.gsantner.opoc.ui.treeview.TreeViewAdapter;
import net.gsantner.opoc.util.FileUtil;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * A fragment representing a list of Items.
 */
public class FileSysFragment extends Fragment {

    private FileSysRecyclerViewAdapter mAdapter;
    private String mTitle;
    private List<FileInfo> mFileInfoList = new ArrayList<>();

    public FileSysFragment() {

    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public static FileSysFragment getInstance(String title) {
        FileSysFragment fragment=new FileSysFragment();
        Bundle args=new Bundle();
        args.putString("title",title);
        return new FileSysFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.file_sys_fragment, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
//            mAdapter = new FileSysRecyclerViewAdapter(mFileInfoList);
//            recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            recyclerView.setAdapter(mAdapter);

            initView(recyclerView);
        }
        return view;
    }

    private void readMDFile() {
        List<File> files = new ArrayList<>();
        files.add(new File(Environment.getExternalStorageDirectory().getAbsolutePath()));
        Observable.from(files)
                .flatMap(new Func1<File, Observable<File>>() {
                    @Override
                    public Observable<File> call(File file) {
                        return listFiles(file);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<File>() {
                            @Override
                            public void onCompleted() {
//                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onNext(File file) {
                                FileInfo fileInfo = FileUtil.getFileInfoFromFile(file);
                                Log.d("File Path：", "->" + fileInfo.getFilePath());
                                mFileInfoList.add(fileInfo);

                            }
                        }
                );
    }


    //list all markdown files in SDCard
    private static Observable<File> listFiles(final File f) {
        if (f.isDirectory()) {
            return Observable.from(f.listFiles()).flatMap(new Func1<File, Observable<File>>() {
                @Override
                public Observable<File> call(File file) {
                    return listFiles(file);
                }
            });
        } else {
            return Observable.just(f).filter(new Func1<File, Boolean>() {
                @Override
                public Boolean call(File file) {
                    return f.exists() && f.canRead() && FileUtil.checkSuffix(f.getAbsolutePath(), new String[]{"md"});
                }
            });
        }
    }

    private void initView(RecyclerView recyclerView) {
        Dir android = new Dir(1, 1, "Android", null, null, null, 1, null, null, System.currentTimeMillis());
        Dir java = new Dir(2, 2, "Java", null, null, null, 1, null, null, System.currentTimeMillis());
        Dir kotlin = new Dir(3, 3, "Kotlin", null, null, null, 1, null, null, System.currentTimeMillis());
        Dir jetpack = new Dir(4, 31, "Jetpack", 3, "Kotlin", null, 0, null, null, System.currentTimeMillis());
        Dir room = new Dir(1, 311, "Room", 31, "Jetpack", null, 0, null, null, System.currentTimeMillis());
        MdFile mdFile1 = new MdFile(1, 1, "TypeConverter的使用", null, 311, "zoup", true, null, null, null, null, System.currentTimeMillis());
        List<TreeNode> nodes = new ArrayList<>();
        TreeNode<DirItem> app = new TreeNode<>(new DirItem("Kotlin"));
        nodes.add(app);
        app.addChild(
                new TreeNode<>(new DirItem("Jetpack"))
                        .addChild(new TreeNode<>(new DirItem("Room")))
                        .addChild(new TreeNode(new FileItem("TypeConverter的使用")))
        );
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TreeViewAdapter adapter = new TreeViewAdapter(nodes, Arrays.asList(new FileNodeBinder(), new DirectoryNodeBinder()));
        // whether collapse child nodes when their parent node was close.
//        adapter.ifCollapseChildWhileCollapseParent(true);
        adapter.setOnTreeNodeListener(new TreeViewAdapter.OnTreeNodeListener() {
            @Override
            public boolean onClick(TreeNode node, RecyclerView.ViewHolder holder) {
                if (!node.isLeaf()) {
                    //Update and toggle the node.
                    onToggle(!node.isExpand(), holder);
//                    if (!node.isExpand())
//                        adapter.collapseBrotherNode(node);
                }
                return false;
            }

            @Override
            public void onToggle(boolean isExpand, RecyclerView.ViewHolder holder) {
                DirectoryNodeBinder.ViewHolder dirViewHolder = (DirectoryNodeBinder.ViewHolder) holder;
                final ImageView ivArrow = dirViewHolder.getIvArrow();
                int rotateDegree = isExpand ? 90 : -90;
                ivArrow.animate().rotationBy(rotateDegree)
                        .start();
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
