package id.absenku.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import id.absenku.R
import id.absenku.databinding.RegisterActivityBinding
import id.absenku.model.ResponseRegistrasi
import id.absenku.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterAcitivity: AppCompatActivity() {
    private var binding : RegisterActivityBinding? = null
    private var nama_lengkap: String= ""
    private var username : String = ""
    private var pass : String = ""
    private var NIK : String = ""
    private var jabatan : String = "Karyawan"
    private var jenis_kelamin : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  RegisterActivityBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        binding!!.btnTambah.setOnClickListener {
            nama_lengkap = binding!!.regNamaLengkap.text.toString()
            username = binding!!.regUsername.text.toString()
            pass = binding!!.regPassword.text.toString()
            NIK = binding!!.regNIK.text.toString()
            jenis_kelamin = when (binding!!.genderRadio.checkedRadioButtonId) {
                R.id.radioButton1 -> "Laki-Laki"
                R.id.radioButton2 -> "Perempuan"
                else -> "NA"
            }
            when{
                username == "" -> {
                    binding!!.regNamaLengkap.error = "Username Tidak Boleh Kosong"
                    binding!!.ivRegUsername.visibility = View.VISIBLE
                }
                nama_lengkap == "" ->{
                    binding!!.regUsername.error = "Nama Lengkap Tidak Boleh Kosong"
                    binding!!.ivNamaLengkap.visibility = View.VISIBLE
                }
                pass == "" -> {
                    binding!!.regPassword.error = "Password Tidak Boleh Kosong"
                }
                else -> {
                    binding!!.loading.visibility = View.VISIBLE
                    RegisData()
                }
            }
        }

        binding!!.btnCancel.setOnClickListener {
            startActivity(Intent(this@RegisterAcitivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun RegisData(){
        val api = RetrofitClient().instance
        api.registrasi(username,pass,nama_lengkap).enqueue(object : Callback<ResponseRegistrasi> {
            override fun onResponse(
                call: Call<ResponseRegistrasi>,
                response: Response<ResponseRegistrasi>
            ) {
                if (response.isSuccessful){
                    if (response.body()?.response == true){
                        binding!!.loading.visibility = View.GONE
                        Toast.makeText(this@RegisterAcitivity,
                            "Registrasi Berhasil",
                            Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@RegisterAcitivity, LoginActivity::class.java))
                        finish()
                    }else{
                        binding!!.loading.visibility = View.GONE
                        Toast.makeText(this@RegisterAcitivity,
                            "Resgistrasi Gagal, Periksa Kembali Nama, username dan Password",
                            Toast.LENGTH_LONG).show()
                    }
                }else{
                    Toast.makeText(
                        this@RegisterAcitivity,
                        "Registrasi Gagal, Kesalahan Terjadi",
                        Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseRegistrasi>, t: Throwable) {
                Log.e("Error", "{$t}")
            }


        })
    }

    override fun onBackPressed() {
        startActivity(Intent(this@RegisterAcitivity, LoginActivity::class.java))
        finish()
        super.onBackPressed()
    }
}