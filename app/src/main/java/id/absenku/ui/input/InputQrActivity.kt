package id.absenku.ui.input

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import id.absenku.R
import id.absenku.databinding.InputQrActivityBinding
import id.absenku.model.ResponseScan
import id.absenku.network.RetrofitClient
import id.absenku.ui.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InputQrActivity: AppCompatActivity() {
    private var binding: InputQrActivityBinding?=null
    private lateinit var profile : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = InputQrActivityBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        profile = getSharedPreferences("Login_session", MODE_PRIVATE)
        binding!!.btnPresensi.setOnClickListener {
                    val kode = binding!!.kodePresensi.text.toString()
                    val api = RetrofitClient().instance
                    val id_kelas = profile.getString("id_kelas", null)
                    api.scan_qr(profile.getString("id_siswa", null), id_kelas, "hadir", kode).enqueue(object :
                        Callback<ResponseScan> {
                        override fun onResponse(
                            call: Call<ResponseScan>,
                            response: Response<ResponseScan>
                        ) {
                            if (response.isSuccessful){
                                if (response.body()?.response == true){
                                    var alertDialog = AlertDialog.Builder(this@InputQrActivity)
                                        .setTitle("Berhasil")
                                        .setMessage("Anda Berhasil Absen")
                                        .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                                            startActivity(Intent(this@InputQrActivity, MainActivity::class.java))
                                            finish()
                                        })
                                        .setIcon(R.drawable.berhasil)
                                        .show()
                                }else if (response.body()?.payload == "sudah absen"){
                                    var alertDialog = AlertDialog.Builder(this@InputQrActivity)
                                        .setTitle("Gagal")
                                        .setMessage("Anda Sudah Absen")
                                        .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                                            startActivity(Intent(this@InputQrActivity, MainActivity::class.java))
                                            finish()
                                        })
                                        .setIcon(R.drawable.warning_icon)
                                        .show()
                                }
                            }else{
                                Toast.makeText(
                                    this@InputQrActivity,
                                    "Login Gagal, Kesalahan Terjadi",
                                    Toast.LENGTH_LONG).show()
                            }
                        }

                        override fun onFailure(call: Call<ResponseScan>, t: Throwable) {
                            Log.e("Error" , "{$t}")
                        }

                    })

                }
            }

        }
