package id.absenku.network


import id.absenku.model.*
import retrofit2.Call
import retrofit2.http.*

interface ApiClient {

    //POST
    @FormUrlEncoded
    @POST("login_service.php")
    fun login(
        @Field("username") username : String,
        @Field("password") password : String
    ):Call<ResponseLogin>

    @FormUrlEncoded
    @POST("registrasi_service.php")
    fun registrasi(
        @Field("username") username : String,
        @Field("password") password : String,
        @Field("nama_siswa") nama_siswa : String
    ):Call<ResponseRegistrasi>

    @FormUrlEncoded
    @POST("scan_service.php")
    fun scan_qr(
        @Field("id_siswa") id_siswa: String?,
        @Field("status") status: String
    ):Call<ResponseScan>

    @FormUrlEncoded
    @POST("rekap_data.php")
    fun data_rekap(
        @Field("id_siswa") id_siswa: String
    ):Call<ArrayList<ResponseDataRekap>>

}