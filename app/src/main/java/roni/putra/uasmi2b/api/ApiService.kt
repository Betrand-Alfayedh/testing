package roni.putra.uasmi2b.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import roni.putra.uasmi2b.model.TambahWisataResponse
import roni.putra.uasmi2b.model.WisataResponse


interface ApiService {

    @FormUrlEncoded
    @POST("wisata/del_wisata.php")
    fun delBerita(
        @Field("id") id: Int
    ): Call<WisataResponse>

    @GET("wisata/get_wisata.php")
    fun getListBerita(@Query("nama_wisata") judul: String): Call<WisataResponse>

    @Multipart
    @POST("wisata/add_wisata.php")
    fun addBerita(
        @Part("nama_wisata") namaWisata: RequestBody,
        @Part("notlpn") noTlp: RequestBody,
        @Part("alamat") alamat: RequestBody,
        @Part("deskripsi_wisata") deskripsi: RequestBody,
        @Part fileGambar: MultipartBody.Part
    ): Call<TambahWisataResponse>
}