package com.invest.advisor.data.network.response

//import com.invest.advisor.data.db.entity.MOEXdata
import com.invest.advisor.data.db.entity.Securities
import com.invest.advisor.data.network.ConnectivityInterceptor
import com.invest.advisor.data.network.ConnectivityInterceptorImpl
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by qsufff on 7/26/2020.
 */

//http://iss.moex.com/iss/engines/stock/markets/shares/boards/TQBR/securities/AFKS.xml?iss.meta=off&iss.only=securities&securities.columns=SECID,SECNAME,LATNAME

//http://iss.moex.com/iss/engines/stock/markets/shares/boards/TQBR/securities/AFKS.json?iss.meta=off&iss.only=securities,marketdata


//http://iss.moex.com/iss/engines/stock/markets/shares/boards/TQBR/securities.json?iss.meta=off&iss.only=securities&securities.columns=SECID

interface IssApiService{
    @GET("securities.json?iss.meta=off&iss.only=securities")
    fun getSecuritiesListAsync():
            Deferred<CurrentSecuritiesResponse>

    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): IssApiService {
            val requestInterceptor = Interceptor {chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    //.addQueryParameter("key", API_KEY)
                    .build()

                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://iss.moex.com/iss/engines/stock/markets/shares/boards/TQBR/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(IssApiService::class.java)
        }
    }
}