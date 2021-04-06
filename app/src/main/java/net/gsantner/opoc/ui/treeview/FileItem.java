package net.gsantner.opoc.ui.treeview;


import net.gsantner.markor.R;

/**
 * Created by tlh on 2016/10/1 :)
 */

public class FileItem implements LayoutItemType {
    public String fileName;

    public FileItem(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_file;
    }
}
