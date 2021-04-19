package net.gsantner.opoc.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import net.gsantner.markor.App
import net.gsantner.markor.R
import net.gsantner.opoc.ui.treeview.*
import net.gsantner.opoc.ui.treeview.TreeViewAdapter.OnTreeNodeListener
import net.gsantner.opoc.util.addTo
import java.util.*


class FileSysFragment : Fragment() {
    var title: String? = null
    lateinit var mRecyclerView: RecyclerView
    lateinit var mTreeAdapter: TreeViewAdapter
    var mNodes = mutableListOf<TreeNode<*>>()
    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRecyclerView =
            inflater.inflate(R.layout.file_sys_fragment, container, false) as RecyclerView
        initData()
        initView(mRecyclerView)
        return mRecyclerView
    }

    private fun initView(recyclerView: RecyclerView) {
        mTreeAdapter =
            TreeViewAdapter(mNodes, Arrays.asList(FileNodeBinder(), DirectoryNodeBinder()))
        mTreeAdapter.setOnTreeNodeListener(object : OnTreeNodeListener {
            override fun onClick(node: TreeNode<*>, holder: RecyclerView.ViewHolder): Boolean {
                if (!node.isLeaf) {
                    //Update and toggle the node.
                    onToggle(!node.isExpand, holder)
                    //                    if (!node.isExpand())
//                        adapter.collapseBrotherNode(node);
                }
                return false
            }

            override fun onToggle(isExpand: Boolean, holder: RecyclerView.ViewHolder) {
                val dirViewHolder = holder as DirectoryNodeBinder.ViewHolder
                val ivArrow = dirViewHolder.ivArrow
                val rotateDegree = if (isExpand) 90 else -90
                ivArrow.animate().rotationBy(rotateDegree.toFloat())
                    .start()
            }
        })
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = mTreeAdapter
    }

    private fun initData() {
        mNodes.clear()
        App.getMyDatabase().dirDAO.queryRootDirList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list ->
                run {
                    list?.forEach {
                        mNodes.add(TreeNode(DirItem(it?.dirName)))
                    }
                    initView(mRecyclerView)
                }
            }.addTo(compositeDisposable)
    }

    companion object {
        @JvmStatic
        fun getInstance(title: String?): FileSysFragment {
            val fragment = FileSysFragment()
            val args = Bundle()
            args.putString("title", title)
            return FileSysFragment()
        }
    }
}