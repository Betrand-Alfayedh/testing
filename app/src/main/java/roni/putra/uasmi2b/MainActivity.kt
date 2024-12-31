package roni.putra.uasmi2b

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RatingBar
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import roni.putra.uasmi2b.adapter.WisataAdapter
import roni.putra.uasmi2b.api.ApiClient
import roni.putra.uasmi2b.model.WisataResponse

class MainActivity : AppCompatActivity() {
    private lateinit var svJudul: SearchView
    private lateinit var progressBar: ProgressBar
    private lateinit var rvBerita: RecyclerView
    private lateinit var floatBtnTambah: FloatingActionButton
    private lateinit var beritaAdapter: WisataAdapter
    private lateinit var imgNotFound: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        svJudul = findViewById(R.id.svJudul)
        progressBar = findViewById(R.id.progressBar)
        rvBerita = findViewById(R.id.rvBerita)
        imgNotFound = findViewById(R.id.imgNotFound)

        //panggil method getBerita
        getBerita("")

        svJudul.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(pencarian: String?): Boolean {
                getBerita(pencarian.toString())
                return true
            }
        })
        floatBtnTambah = findViewById(R.id.floatBtnTambah)
        floatBtnTambah.setOnClickListener {
            startActivity(Intent(this, TambahWisataActivity::class.java))
        }
    }

    private fun getBerita(namaWisata: String){
        progressBar.visibility = View.VISIBLE
        ApiClient.apiService.getListBerita(namaWisata).enqueue(object: Callback<WisataResponse>{
            override fun onResponse(
                call: Call<WisataResponse>,
                response: Response<WisataResponse>
            ) {
                if (response.isSuccessful){
                    if(response.body()!!.success){
                        //set data ke adapter
                        beritaAdapter = WisataAdapter(response.body()!!.data)
                        rvBerita.layoutManager = LinearLayoutManager(this@MainActivity)
                        rvBerita.adapter = beritaAdapter
                        imgNotFound.visibility = View.GONE
                    } else {
                        //jika data tidak ditemukan
                        beritaAdapter = WisataAdapter(arrayListOf())
                        rvBerita.layoutManager = LinearLayoutManager(this@MainActivity)
                        rvBerita.adapter = beritaAdapter
                        imgNotFound.visibility = View.VISIBLE
                    }
                }
                progressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<WisataResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error : ${t.message}", Toast.LENGTH_LONG)
                    .show()
                progressBar.visibility = View.GONE
            }
        })
    }
}