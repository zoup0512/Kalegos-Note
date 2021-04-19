package net.gsantner.opoc.ui

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.FileUtils
import android.provider.OpenableColumns
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.import_file_activity.*
import net.gsantner.markor.App
import net.gsantner.markor.R
import net.gsantner.opoc.bean.Dir
import net.gsantner.opoc.bean.MdFile
import net.gsantner.opoc.ui.dialog.EditDirDialog
import net.gsantner.opoc.util.addTo
import org.angmarch.views.NiceSpinner
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random


class ImportFileActivity : AppCompatActivity() {
    var mDirList: List<Dir?>? = null
    var mDirNameList: List<String?>? = null
    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    lateinit var disposable: Disposable
    var mDir: Dir? = null
    var mFile: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.import_file_activity)
        addListeners()
        initData()
    }

    private fun initData() {
        initSpinner()
    }

    private fun addListeners() {
        select_file_button.setOnClickListener {
            openDMS()
        }
        add_dir_image.setOnClickListener {
            val dialog = EditDirDialog(this, "选择目录", "")
            dialog.setConfirmListerner {
                val dirName = dialog.content
                val dir =
                    Dir(null, dirName, null, null, null, 1, null, null, System.currentTimeMillis())
                App.getMyDatabase().dirDAO.insertDir(dir)
                initSpinner()
                dialog.dismiss()
            }
            dialog.show()
        }
        import_finish_text.setOnClickListener {
            val mdFile = MdFile(
                null,
                mFile?.name,
                null,
                mDir?.dirId,
                null,
                true,
                null,
                mFile?.absolutePath,
                null,
                null,
                System.currentTimeMillis()
            )
            App.getMyDatabase().fileDAO.insert(mdFile)
            finish()
        }
        import_back_image.setOnClickListener {
            finish()
        }
    }

    private fun initSpinner() {
        App.getMyDatabase().dirDAO.queryRootDirList().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list ->
                if (list != null && list.isNotEmpty()) {
                    mDirList = list
                    mDirNameList = list.map { it?.dirName }
                    mDirNameList?.let {
                        choose_dir_spinner.attachDataSource(it)
                    }
                }
            }.addTo(compositeDisposable)
        choose_dir_spinner.setOnSpinnerItemSelectedListener { niceSpinner: NiceSpinner, view: View, position: Int, id: Long ->
            mDir = mDirList?.get(position)
        }
    }

    private fun openDMS() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, 0x01)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0x01 && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            uri?.let {
                mFile = uriToFileQ(this, uri)
                Toast.makeText(this, mFile?.path, Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun uriToFileQ(context: Context, uri: Uri): File? =
        if (uri.scheme == ContentResolver.SCHEME_FILE)
            File(requireNotNull(uri.path))
        else if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            //把文件保存到沙盒
            val contentResolver = context.contentResolver
            val displayName = run {
                val cursor = contentResolver.query(uri, null, null, null, null)
                cursor?.let {
                    if (it.moveToFirst())
                        it.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    else null
                }
            }
                ?: "${System.currentTimeMillis()}${Random.nextInt(0, 9999)}.${
                    MimeTypeMap.getSingleton()
                        .getExtensionFromMimeType(contentResolver.getType(uri))
                }"

            val ios = contentResolver.openInputStream(uri)
            if (ios != null) {
                File("${context.filesDir!!.absolutePath}/$displayName")
                    .apply {
                        val fos = FileOutputStream(this)
                        FileUtils.copy(ios, fos)
                        fos.close()
                        ios.close()
                    }
            } else null
        } else null

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}