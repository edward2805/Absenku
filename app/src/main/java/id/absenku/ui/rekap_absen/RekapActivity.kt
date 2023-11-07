package id.absenku.ui.rekap_absen

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import id.absenku.databinding.ActivityRekapDataBinding
import id.absenku.model.ResponseDataRekap
import id.absenku.network.RetrofitClient
import id.absenku.ui.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RekapActivity: AppCompatActivity() {
    private var binding: ActivityRekapDataBinding?=null
    private var list = ArrayList<ResponseDataRekap>()
    private lateinit var profile : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRekapDataBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        binding!!.rvRekaplist.setHasFixedSize(true)
        binding!!.rvRekaplist.layoutManager = LinearLayoutManager(this)
        profile = getSharedPreferences("Login_session", MODE_PRIVATE)
        var id_siswa = profile.getString("id_siswa", null)

        val api = RetrofitClient().instance
        api.data_rekap(id_siswa.toString()).enqueue(object :
            Callback<ArrayList<ResponseDataRekap>> {
            override fun onResponse(
                call: Call<ArrayList<ResponseDataRekap>>,
                response: Response<ArrayList<ResponseDataRekap>>
            ) {
                response.body()?.let { list.addAll(it) }
                Log.e("Data : ", list.toString())
                var adapter = DataKaryawanAdapter(list)
                binding!!.rvRekaplist.adapter = adapter


            }

            override fun onFailure(call: Call<ArrayList<ResponseDataRekap>>, t: Throwable) {
                t.printStackTrace()
                Log.e("Error", t.message.toString())
            }

        })
    }

    override fun onBackPressed() {
        startActivity(Intent(this@RekapActivity, MainActivity::class.java))
        super.onBackPressed()
    }
}