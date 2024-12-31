package roni.putra.uasmi2b

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import roni.putra.uasmi2b.api.ApiClient
import roni.putra.uasmi2b.model.TambahWisataResponse
import java.io.File

class TambahWisataActivity : AppCompatActivity() {
    private lateinit var etJudul: EditText
    private lateinit var etNoTlp: EditText
    private lateinit var etAlamat: EditText
    private lateinit var etDeskripsi: EditText
    private lateinit var btnTambah: Button
    private lateinit var btnGambar: Button
    private lateinit var imgGambar: ImageView
    private lateinit var progressBar: ProgressBar
    private var imageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_wisata)

        // Inisialisasi view
        etJudul = findViewById(R.id.etNamaWisata)
        etNoTlp = findViewById(R.id.etNoTlp)
        etAlamat = findViewById(R.id.etAlamat)
        etDeskripsi = findViewById(R.id.etDeskripsi)
        btnTambah = findViewById(R.id.btnTambah)
        btnGambar = findViewById(R.id.btnGambar)
        imgGambar = findViewById(R.id.imgGambar)
        progressBar = findViewById(R.id.progressBar)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Tambahkan listener untuk tombol pilih gambar
        btnGambar.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start()
        }
        btnTambah.setOnClickListener {
            imageFile?.let { file ->
                tambahBerita(
                    etJudul.text.toString(),
                    file,
                    etNoTlp.text.toString(),
                    etDeskripsi.text.toString(),
                    etAlamat.text.toString()
                )
            }
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val uri = data.data!!
            imageFile = File(uri.path!!)
            imgGambar.visibility = View.VISIBLE
            imgGambar.setImageURI(uri)
        }
    }

    //proses tambah berita
    private fun tambahBerita(namaWisata: String, fileGambar: File, noTlp: String, deskripsi: String, alamat: String) {
        progressBar.visibility = View.VISIBLE
        val requestBody = fileGambar.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val partFileGambar = MultipartBody.Part.createFormData("fileGambar", fileGambar.name, requestBody)

        val NamaWisataBody = namaWisata.toRequestBody("text/plain".toMediaTypeOrNull())
        val NoTlpBody = noTlp.toRequestBody("text/plain".toMediaTypeOrNull())
        val AlamatBody = alamat.toRequestBody("text/plain".toMediaTypeOrNull())
        val DeskripsiBody = deskripsi.toRequestBody("text/plain".toMediaTypeOrNull())

        ApiClient.apiService.addBerita(NamaWisataBody, NoTlpBody, AlamatBody, DeskripsiBody, partFileGambar).enqueue(object :
            Callback<TambahWisataResponse>{
            override fun onResponse(
                call: Call<TambahWisataResponse>,
                response: Response<TambahWisataResponse>
            ) {
                if(response.isSuccessful){
                    if(response.body()!!.success){

                        startActivity(
                            Intent(
                                this@TambahWisataActivity,
                                MainActivity::class.java)
                        )
                    }
                    else {
                        Toast.makeText(this@TambahWisataActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else
                {
                    Toast.makeText(this@TambahWisataActivity,
                        response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                progressBar.visibility = View.GONE

            }


            override fun onFailure(call: Call<TambahWisataResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
            }
        })
    }
}
