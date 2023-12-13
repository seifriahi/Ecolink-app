package com.ecolink.brand

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.ecolink.R
import com.ecolink.api.Api
import com.ecolink.models.News
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class Addproduit : Fragment(R.layout.fragment_add_product1), UploadRequestBody.UploadCallback {

    private lateinit var selectedImageUri: Uri

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val image_view = view.findViewById<ImageView>(R.id.image_view)
        val add = view.findViewById<Button>(R.id.btnsub)

        image_view.setOnClickListener {
            openImageChooser()
        }

        add.setOnClickListener { uploadImage() }
    }

    private fun uploadImage() {
        //val brandId = requireActivity().intent.getStringExtra("id")

        val apiInterface = Api.create()
        //val progress_bar = requireView().findViewById<ProgressBar>(R.id.progress_bar)
        val layout_root = requireView().findViewById<ConstraintLayout>(R.id.layout_root)

        if (selectedImageUri == null) {
            layout_root.snackbar("Select an Image First")
            return
        }

        //val parcelFileDescriptor = requireActivity().contentResolver.openFileDescriptor(
        //selectedImageUri!!, "r", null
        //) ?: return


        val productname = requireView().findViewById<EditText>(R.id.produitname)
        val productdescription = requireView().findViewById<EditText>(R.id.description)

        val filesDir = requireActivity().applicationContext.filesDir
        val file = File(filesDir, requireActivity().contentResolver.getFileName(selectedImageUri!!))

        val inputStream = requireActivity().contentResolver.openInputStream(selectedImageUri)
        val outputStream = FileOutputStream(file)
        inputStream!!.copyTo(outputStream)

        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("image", file.name, requestBody)

        val title = productname.text.toString()
        val description = productdescription.text.toString()

        apiInterface.addProjects(

            part,
            title,
            description,

        ).enqueue(object : Callback<News> {

            override fun onResponse(call: Call<News>, response: Response<News>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "add success!",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Finish activity or navigate to another fragment if needed
                } else {
                    Toast.makeText(
                        requireContext(),
                        "mch te5dem",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                Toast.makeText(requireContext(), "${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("TAG", "onFailure: ${t.message}")
            }
        })
    }

    private fun openImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, REQUEST_CODE_IMAGE)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val image_view = requireView().findViewById<ImageView>(R.id.image_view)
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_IMAGE -> {
                    selectedImageUri = data?.data!!
                    image_view.setImageURI(selectedImageUri)
                }
            }
        }
    }



    companion object {
        const val REQUEST_CODE_IMAGE = 101
    }

    override fun onProgressUpdate(percentage: Int) {
        val progress_bar = requireView().findViewById<ProgressBar>(R.id.progress_bar)
        progress_bar.progress = percentage
    }

    private fun ContentResolver.getFileName(selectedImageUri: Uri): String {
        var name = ""
        val returnCursor = this.query(selectedImageUri, null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }

        return name
    }

    private fun View.snackbar(message: String) {
        Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
            snackbar.setAction("OK") {
                snackbar.dismiss()
            }
        }.show()
    }
}
