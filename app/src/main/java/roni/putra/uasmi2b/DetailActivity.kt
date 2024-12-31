package roni.putra.uasmi2b

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {
    private lateinit var imgBerita: ImageView
    private lateinit var tvNamaWisata: TextView
    private lateinit var tvNoTlp: TextView
    private lateinit var tvAlamat: TextView
    private lateinit var tvDeskripsi: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        imgBerita = findViewById(R.id.imgBerita)
        tvNamaWisata = findViewById(R.id.tvNamaWisata)
        tvNoTlp = findViewById(R.id.tvNoTlp)
        tvAlamat = findViewById(R.id.tvAlamat)
        tvDeskripsi = findViewById(R.id.tvDeskripsi)

        Picasso.get().load(intent.getStringExtra("gambar")).into(imgBerita)
        tvNamaWisata.text = intent.getStringExtra("nama_wisata")
        tvNoTlp.text = intent.getStringExtra("notlpn")
        tvAlamat.text = intent.getStringExtra("alamat")
        tvDeskripsi.text = intent.getStringExtra("deskripsi_wisata")


    }
}