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
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.import_file_activity.*
import net.gsantner.markor.App
import net.gsantner.markor.R
import net.gsantner.opoc.bean.Dir
import net.gsantner.opoc.ui.dialog.EditDirDialog
import org.angmarch.views.NiceSpinnerAdapter
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random


class ImportFileActivity : AppCompatActivity() {
    //    val dirList= mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.import_file_activity)
        addListeners()
        initData()
    }

    private fun initData() {
//        choose_dir_spinner.attachDataSource(dirList)
        initSpinner()
    }

    private fun addListeners() {
        select_file_button.setOnClickListener {
            openFileView()
        }
        add_dir_image.setOnClickListener {
            val dialog = EditDirDialog(this, "选择目录", "")
            dialog.setConfirmListerner {
                val dirName = dialog.content
                val dir = Dir(1, dirName, null, null, null, 1, null, null, System.currentTimeMillis())
                App.getDirDAO().insertDir(dir)
                initSpinner()
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    private fun initSpinner() {
        App.getDirDAO().queryRootDirList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if(it!=null&&it.isNotEmpty()){
                        choose_dir_spinner.attachDataSource(it)
                    }
                }
    }

    fun openFileView() {
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
                val file = uriToFileQ(this, uri)
                Toast.makeText(this, file?.path, Toast.LENGTH_SHORT).show()
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
                        ?: "${System.currentTimeMillis()}${Random.nextInt(0, 9999)}.${MimeTypeMap.getSingleton()
                                .getExtensionFromMimeType(contentResolver.getType(uri))}"

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
}