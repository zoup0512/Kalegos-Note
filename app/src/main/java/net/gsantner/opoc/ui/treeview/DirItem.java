package net.gsantner.opoc.ui.treeview;


import net.gsantner.markor.R;

/**
 * Created by tlh on 2016/10/1 :)
 */

public class DirItem implements LayoutItemType {
    public String dirName;

    public DirItem(String dirName) {
        this.dirName = dirName;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_dir;
    }
}
