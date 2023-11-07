package id.absenku.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RetrofitClient {

    val instance: ApiClient by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.78:8080/DESNA/")
//            .baseUrl("http://10.0.2.2/DESNA/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiClient::class.java)
    }

}