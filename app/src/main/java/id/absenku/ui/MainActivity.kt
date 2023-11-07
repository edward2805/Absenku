package id.absenku.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import id.absenku.databinding.ActivityMainBinding
import id.absenku.ui.auth.LoginActivity
import id.absenku.ui.rekap_absen.RekapActivity
import id.absenku.ui.scan.ScanQrActivity

class MainActivity: AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private lateinit var profile : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        profile = getSharedPreferences("Login_session", MODE_PRIVATE)
        binding!!.username.text = profile.getString("nama_siswa", null).toString()
        binding!!.cardScanQR.setOnClickListener {
            startActivity(Intent(this@MainActivity, ScanQrActivity::class.java))
            finish()
        }
        binding!!.cardDataPresensi.setOnClickListener {
            startActivity(Intent(this@MainActivity, RekapActivity::class.java))
            finish()
        }

        binding!!.btnLogout.setOnClickListener {
            getSharedPreferences("Login_session", MODE_PRIVATE)
                .edit()
                .putString("id_siswa", null)
                .apply()
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        finish()
        super.onBackPressed()
    }
}