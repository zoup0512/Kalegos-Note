package net.gsantner.opoc.ui

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.import_file_activity.*
import net.gsantner.markor.R
import java.io.File


class ImportFileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.import_file_activity)

    }

    private fun addListeners() {
        back_image.setOnClickListener {
            openFileView()
        }
    }

    fun openFileView() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, 0x01)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0x01&&resultCode== Activity.RESULT_OK) {
            val uri: Uri? = data!!.data
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            val actualimagecursor: Cursor = managedQuery(uri, proj, null, null, null)
            val actual_image_column_index: Int =
                actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            actualimagecursor.moveToFirst()
            val img_path: String = actualimagecursor.getString(actual_image_column_index)
            val file = File(img_path)
            Toast.makeText(this@ImportFileActivity, file.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}