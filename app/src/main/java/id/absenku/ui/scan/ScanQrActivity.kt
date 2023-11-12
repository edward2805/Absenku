package id.absenku.ui.scan

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import id.absenku.R
import com.budiyev.android.codescanner.ScanMode
import id.absenku.databinding.ScanQrActivityBinding
import id.absenku.model.ResponseScan
import id.absenku.network.RetrofitClient
import id.absenku.ui.MainActivity
import id.absenku.ui.input.InputQrActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ScanQrActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    private var binding: ScanQrActivityBinding?=null
    private lateinit var profile : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ScanQrActivityBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        profile = getSharedPreferences("Login_session", MODE_PRIVATE)
        setupPermissions()
        codeScanner()
        binding!!.btnLink.setOnClickListener {
            startActivity(Intent(this@ScanQrActivity, InputQrActivity::class.java))
            finish()
        }
    }

    private fun codeScanner() {
        codeScanner = CodeScanner(this, binding!!.scn)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                runOnUiThread {
                        val kode = it.text.toString()
                        val api = RetrofitClient().instance
                        val id_kelas = profile.getString("id_kelas", null)
                        api.scan_qr(profile.getString("id_siswa", null), id_kelas, "hadir", kode)
                            .enqueue(object : Callback<ResponseScan>{
                            override fun onResponse(
                                call: Call<ResponseScan>,
                                response: Response<ResponseScan>
                            ) {
                                if (response.isSuccessful){
                                    if (response.body()?.response == true){
                                        var alertDialog = AlertDialog.Builder(this@ScanQrActivity)
                                            .setTitle("Berhasil")
                                            .setMessage("Anda Berhasil Absen")
                                            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                                                startActivity(Intent(this@ScanQrActivity, MainActivity::class.java))
                                                finish()
                                            })
                                            .setIcon(R.drawable.berhasil)
                                            .show()
                                    }else if (response.body()?.payload == "sudah absen"){
                                        var alertDialog = AlertDialog.Builder(this@ScanQrActivity)
                                            .setTitle("Gagal")
                                            .setMessage("Anda Sudah Absen")
                                            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                                                startActivity(Intent(this@ScanQrActivity, MainActivity::class.java))
                                                finish()
                                            })
                                            .setIcon(R.drawable.warning_icon)
                                            .show()
                                    }
                                }else{
                                    Toast.makeText(
                                        this@ScanQrActivity,
                                        "Login Gagal, Kesalahan Terjadi",
                                        Toast.LENGTH_LONG).show()
                                }
                            }

                            override fun onFailure(call: Call<ResponseScan>, t: Throwable) {
                                Log.e("Error" , "{$t}")
                            }

                        })

                    }
                errorCallback = ErrorCallback {
                    runOnUiThread {
                        Log.e("Main", "codeScanner: ${it.message}")
                    }
                }

                }
            }




//            binding!!.scn.setOnClickListener {
//                codeScanner.startPreview()
//            }

        }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQ
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQ -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        "You need the camera permission to use this app",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    companion object {
        private const val CAMERA_REQ = 101
    }
}