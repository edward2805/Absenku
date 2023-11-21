package id.absenku.ui.auth

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import id.absenku.ui.scan.ScanQrActivity
import id.absenku.databinding.LoginActivityBinding
import id.absenku.model.ResponseLogin
import id.absenku.network.RetrofitClient
import id.absenku.ui.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity: AppCompatActivity() {
    private var binding: LoginActivityBinding? = null
    private var user : String = ""
    private var pass : String = ""
    private lateinit var profile : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        profile = getSharedPreferences("Login_session", MODE_PRIVATE)
        if (profile.getString("id", null)!=null){
            login()
        }else{
            Toast.makeText(this@LoginActivity, "Session Habis", Toast.LENGTH_SHORT)

        }

        binding!!.btnLogin.setOnClickListener{
            user = binding!!.etUsername.text.toString()
            pass = binding!!.etPassword.text.toString()

            when{
                user == "" -> {
                    binding!!.etUsername.error = "nisn Tidak Boleh Kosong"
                }
                pass == "" -> {
                    binding!!.etPassword.error = "Password Tidak Boleh Kosong"
                }
                else -> {
                    binding!!.loading.visibility = View.VISIBLE
                    getData()
                }
            }
        }

        binding!!.btnRegis.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterAcitivity::class.java))
            finish()
        }

//        binding!!.btnRegis.setBackgroundColor(Color.parseColor("#FFFFFF"))



    }



    private fun getData(){
        val api = RetrofitClient().instance
        api.login(user,pass).enqueue(object : Callback<ResponseLogin> {
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                if (response.isSuccessful){
                    getSharedPreferences("Login_session", MODE_PRIVATE)
                        .edit()
                        .putString("id_siswa", response.body()?.payload?.id_siswa)
                        .putString("id_kelas", response.body()?.payload?.id_kelas)
                        .putString("nisn", response.body()?.payload?.nisn)
                        .putString("nama_siswa", response.body()?.payload?.nama_siswa)
                        .apply()
                    if (response.body()?.response == true){
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }else{
                        binding!!.loading.visibility = View.GONE
                        Toast.makeText(this@LoginActivity,
                            "Login Gagal, Periksa Kembali nisn dan Password",
                            Toast.LENGTH_LONG).show()
                    }
                }else{
                    Toast.makeText(
                        this@LoginActivity,
                        "Login Gagal, Kesalahan Terjadi",
                        Toast.LENGTH_LONG).show()
                }
            }


            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                Log.e("Pesan Error", "${t.message}")
            }


        })
    }

    private fun login() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}