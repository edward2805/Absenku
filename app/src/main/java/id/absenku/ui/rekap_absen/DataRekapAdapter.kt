package id.absenku.ui.rekap_absen

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import id.absenku.R
import id.absenku.model.ResponseDataRekap
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DataKaryawanAdapter( val responseDataRekap: ArrayList<ResponseDataRekap>):
    RecyclerView.Adapter<RekapDataViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RekapDataViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rekap_list,parent,false)
        return RekapDataViewHolder(view)
    }

    override fun onBindViewHolder(holder: RekapDataViewHolder, @SuppressLint("RecyclerView") position: Int) {

        return holder.bindView(responseDataRekap[position])
    }

    override fun getItemCount(): Int {
        return responseDataRekap.size
    }

}



private fun <T> Call<T>.enqueue(callback: Callback<ResponseDataRekap>) {

}

class RekapDataViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    private val status: TextView = itemView.findViewById(R.id.status)
    private val tgl_absen: TextView = itemView.findViewById(R.id.tgl_absen)

    fun bindView(responseDataRekap: ResponseDataRekap){
        tgl_absen.text = responseDataRekap.tgl_absen
        status.text = responseDataRekap.status

    }

}