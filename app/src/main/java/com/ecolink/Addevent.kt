package com.ecolink
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import android.Manifest
import android.database.Cursor
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.ecolink.ViewModel.EventViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import okhttp3.MediaType.Companion.toMediaTypeOrNull

class Addevent : AppCompatActivity() {
    private val REQUEST_IMAGE_GALLERY = 101
    private lateinit var selectedImageView: ImageView
    private val client = OkHttpClient()
    private var selectedImageUri: Uri? = null
    private val PERMISSION_REQUEST_CODE = 1001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addevent_layout)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_cancel_24) // Définissez ici votre icône de retour
            title = "crée un evenement" // Titre de la Toolbar
        }
        selectedImageView = findViewById(R.id.selectedImageView) // Associer l'instance avec la vue dans le layout XML
        val dateAndTimeButton: Button = findViewById(R.id.dateAndTimeButton)
        val uploadImageButton: Button = findViewById(R.id.uploadImageButton)
        val addEventButton: Button = findViewById(R.id.addEventButton)
        dateAndTimeButton.setOnClickListener {
            showDateTimePicker(dateAndTimeButton)
        }
        uploadImageButton.setOnClickListener {
            openGallery()
        }
        addEventButton.setOnClickListener {
            sendFormDataToServer()
        }
    }
    private fun sendFormDataToServer() {
        val url = "https://ecolink.onrender.com/api/evenement"

        val eventName = findViewById<TextInputEditText>(R.id.tiname).text.toString()
        val eventDescription = findViewById<TextInputEditText>(R.id.description).text.toString()
        val eventLocation = findViewById<TextInputEditText>(R.id.location).text.toString()
        val textwhats = findViewById<TextInputEditText>(R.id.whats).text.toString()
        val eventDateTime = findViewById<Button>(R.id.dateAndTimeButton).text.toString() // Récupérer la date et l'heure du bouton


        val file = selectedImageUri?.let { uri ->
            if (uri.scheme == "content") {
                File(getRealPathFromURI(uri))
            } else {
                File(uri.path ?: "")
            }
        }

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("eventName", eventName)
            .addFormDataPart("eventDescription", eventDescription)
            .addFormDataPart("eventLocation", eventLocation)
            .addFormDataPart("textwhats", textwhats)
            .addFormDataPart("eventDate", eventDateTime)
            .addFormDataPart("image", "image.jpg", RequestBody.create(
                "image/jpeg".toMediaTypeOrNull(),
                file!!
            ))

            .build()



        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                Log.e("FormDataToServer", "Error: ${e.message}") // Enregistrer les erreurs
            }

            // Après onResponse() dans sendFormDataToServer()

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(applicationContext, "Événement créé avec succès", Toast.LENGTH_SHORT).show()
                        // Rafraîchir la liste des événements après la création réussie
                        val eventViewModel = ViewModelProvider(this@Addevent).get(EventViewModel::class.java)
                        eventViewModel.fetchEvents()
                    }
                    finish() // Revenir à l'écran précédent
                    Log.d("FormDataToServer", "Request successful")
                } else {
                    runOnUiThread {
                        Toast.makeText(applicationContext, "Erreur lors de la création de l'événement", Toast.LENGTH_SHORT).show()
                    }
                    Log.e("FormDataToServer", "Request failed: ${response.code}")
                }
            }

        })
    }


    private fun getRealPathFromURI(uri: Uri): String? {
        var cursor: Cursor? = null
        try {
            val cursor = contentResolver.query(uri, null, null, null, null)
            return cursor?.use {
                it.moveToFirst()
                val index = it.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                it.getString(index)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            cursor?.close()
        }
    }




    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data

            try {
                selectedImageUri?.let { uri ->
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    selectedImageView.setImageBitmap(bitmap)
                    selectedImageView.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                e.printStackTrace()

            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else {

            }
        }
    }


    private fun showDateTimePicker(button: Button) {
        val currentDateTime = LocalDateTime.now()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

        val datePickerDialog = DatePickerDialog(
            this@Addevent,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDateTime = LocalDateTime.of(selectedYear, selectedMonth + 1, selectedDay, currentDateTime.hour, currentDateTime.minute)
                val timePickerDialog = TimePickerDialog(
                    this@Addevent,
                    TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                        // Modifier la date et l'heure sélectionnées
                        val modifiedDateTime = selectedDateTime.withHour(selectedHour).withMinute(selectedMinute)
                        val formattedDateTime = modifiedDateTime.format(dateFormatter)
                        button.text = formattedDateTime
                    },
                    currentDateTime.hour,
                    currentDateTime.minute,
                    true
                )
                timePickerDialog.show()
            },
            currentDateTime.year,
            currentDateTime.monthValue - 1,
            currentDateTime.dayOfMonth
        )

        // Limiter la sélection à partir de la date actuelle
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000 // Bloquer la sélection de la date passée
        datePickerDialog.show()
    }


}