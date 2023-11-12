package id.absenku

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.skyfishjy.library.RippleBackground
import id.absenku.ui.scan.ScanQrActivity
import java.util.Locale
import kotlin.math.pow

class LokasiActivity : AppCompatActivity() {
    companion object{
        const val ID_LOCATION_PERMISSION = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lokasi)

        checkPermissionLocation()
        onClick()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ID_LOCATION_PERMISSION){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Berhasil di izinkan", Toast.LENGTH_SHORT).show()

                if (!isLocationEnabled()){
                    Toast.makeText(this,"Tolong nyalakan lokasi anda", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
            }else{
                Toast.makeText(this,"Gagal di izinkan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermissionLocation(){
        if (checkPermission()){
            if (!isLocationEnabled()){
                Toast.makeText(this,"Tolong nyalakan lokasi anda", Toast.LENGTH_SHORT).show()
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        }else{
            requestPermission()
        }
    }

    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    private fun isLocationEnabled(): Boolean{
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            return true
        }
        return false
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            ID_LOCATION_PERMISSION
        )
    }

    private fun onClick(){
        val fab_cekin = findViewById<FloatingActionButton>(R.id.fab_cekin)
        fab_cekin.setOnClickListener{
            loadScanLocation()
            Handler(Looper.getMainLooper()).postDelayed({
                getLastLocation()
            }, 4000)
        }
    }
    private fun loadScanLocation(){
        val rippleBg  = findViewById<RippleBackground>(R.id.rippleBackground)
        val tv_scan   = findViewById<TextView>(R.id.tv_scaning)
        val tv_sukses = findViewById<TextView>(R.id.tv_checkinsukses)
        rippleBg.startRippleAnimation()
        tv_scan.visibility = View.VISIBLE
        tv_sukses.visibility = View.GONE
    }

    private fun stopScanLocation(){
        val rippleBg  = findViewById<RippleBackground>(R.id.rippleBackground)
        val tv_scan   = findViewById<TextView>(R.id.tv_scaning)
        rippleBg.stopRippleAnimation()
        tv_scan.visibility = View.GONE
    }

    private fun getLastLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                LocationServices.getFusedLocationProviderClient(this).lastLocation
                    .addOnSuccessListener { location ->
                        val currentLat = location.latitude
                        val currentLong = location.longitude
                        val tvSukses = findViewById<TextView>(R.id.tv_checkinsukses)

//                        tvSukses.visibility = View.VISIBLE
//                        tvSukses.text = "lat : $currentLat long: $currentLong"

                        val distance = hitungJarak(
                            currentLat,
                            currentLong,
                            getAddresses()[0].latitude,
                            getAddresses()[0].longitude) * 1000

                        if (distance <1000.0 ){
                            startActivity(Intent(this@LokasiActivity, ScanQrActivity::class.java))
                            finish()
                            tvSukses.visibility = View.VISIBLE
                            tvSukses.text = "Silahkan Lanjut Absen"
                        }else{
                            tvSukses.visibility = View.VISIBLE
                            tvSukses.text = "Anda Tidak Berada Di Sekolah"
                        }

                        stopScanLocation()
                    }
            } else {
                Toast.makeText(this, "Tolong nyalakan lokasi anda", Toast.LENGTH_SHORT).show()
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        } else {
            requestPermission()
        }
    }

    private fun getAddresses(): List<Address>{
        val destinasiTempat = "Gresik Helmet"
        val geocode = Geocoder(this, Locale.getDefault())
        return geocode.getFromLocationName(destinasiTempat, 100) as List<Address>
    }

    fun hitungJarak(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6372.8 // in kilometers
        val radiansLat1 = Math.toRadians(lat1)
        val radiansLat2 = Math.toRadians(lat2)
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        return 2 * r * Math.asin(
            Math.sqrt(
                Math.sin(dLat / 2).pow(2.0) + Math.sin(dLon / 2)
                    .pow(2.0) * Math.cos(radiansLat1) * Math.cos(radiansLat2)
            )
        )
    }
}