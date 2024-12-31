package roni.putra.uasmi2b.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import roni.putra.uasmi2b.DetailActivity
import roni.putra.uasmi2b.R
import roni.putra.uasmi2b.api.ApiClient
import roni.putra.uasmi2b.model.WisataResponse

class WisataAdapter (
    val dataBerita : ArrayList<WisataResponse.ListItem>
) :RecyclerView.Adapter<WisataAdapter.ViewHolder>(){
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //Inisialisasi Widget //
        val imgBerita = view.findViewById<ImageView>(R.id.imgBerita)
        val tvNamaWisata = view.findViewById<TextView>(R.id.tvNamaWisata)
        val tvNoTlp = view.findViewById<TextView>(R.id.tvNoTlp)
        val tvAlamat = view.findViewById<TextView>(R.id.tvAlamat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout,parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataBerita.size
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        //Tampilkan data//
        val hasilResponse = dataBerita[position]
        Picasso.get().load(hasilResponse.gambar).into(holder.imgBerita)
        holder.tvNamaWisata.text = hasilResponse.nama_wisata
        holder.tvNoTlp.text = hasilResponse.notlpn
        holder.tvAlamat.text = hasilResponse.alamat

        //klik item berita
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java).apply {
                putExtra("gambar", hasilResponse.gambar)
                putExtra("nama_wisata", hasilResponse.nama_wisata)
                putExtra("notlpn", hasilResponse.notlpn)
                putExtra("alamat", hasilResponse.alamat)
                putExtra("deskripsi_wisata", hasilResponse.deskripsi_wisata)
            }
            holder.imgBerita.context.startActivity(intent)
        }
        //remove item
        holder.itemView.setOnLongClickListener {
            AlertDialog.Builder(holder.itemView.context).apply {
                setTitle("Konfirmasi")
                setMessage("Apakah anda ingin melanjutkan?")
                setIcon(R.drawable.ic_delete)

                setPositiveButton("Yakin") { dialogInterface, i ->
                    ApiClient.apiService.delBerita(hasilResponse.id)
                        .enqueue(object : Callback<WisataResponse> {
                            override fun onResponse(
                                call: Call<WisataResponse>,
                                response: Response<WisataResponse>
                            ) {
                                if (response.body()!!.success) {
                                    removeItem(position)
                                } else {
                                    Toast.makeText(
                                        holder.itemView.context,
                                        response.body()!!.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }

                            override fun onFailure(call: Call<WisataResponse>, t: Throwable) {
                                Toast.makeText(
                                    holder.itemView.context,
                                    "Ada Kesalahan Server",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        })
                    dialogInterface.dismiss()
                }

                setNegativeButton("Batal") { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
            }.show()

            true
        }
    }

    fun removeItem(position: Int) {
        dataBerita.removeAt(position)
        notifyItemRemoved(position) // Notify the position of the removed item
        notifyItemRangeChanged(position, dataBerita.size - position) // Optional: Adjust for index shifts
    }

    fun setData(data: List<WisataResponse.ListItem>) {
        dataBerita.clear()
        dataBerita.addAll(data)
    }
}