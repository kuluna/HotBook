package jp.kuluna.hotbook.models

import android.arch.lifecycle.LiveData
import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface Api {
    @Headers("User-Agent: HotBook by twitter @kuluna")
    @GET("/api/ipad.hotentry")
    fun getEntries(@Query("category") category: String): Call<List<Entry>>
    @GET("/entry/jsonlite/")
    fun getComments(@Query("url") url: String): Call<CommentResponse>
}

object ApiClient {
    val hatena: Api
        get() {
            val retrofit = Retrofit.Builder()
                    .baseUrl("http://b.hatena.ne.jp")
                    .client(OkHttpClient())
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
            return retrofit.create(Api::class.java)
        }
}

class RetrofitLiveData<T>(private val call: Call<T>) : LiveData<ResponseBody<T>>(), Callback<T> {
    private var retry = 3

    override fun onActive() {
        if (!call.isCanceled && !call.isExecuted) call.enqueue(this)
    }

    override fun onFailure(call: Call<T>?, t: Throwable) {
        Log.w("HttpError", "remaining retry count: $retry\n${t.message}", t)
        retry -= 1
        if (retry >= 0) {
            val error = ResponseError(0, t.localizedMessage)
            postValue(ResponseBody(null, error))
        }
    }

    override fun onResponse(call: Call<T>?, response: Response<T>?) {
        if (response != null) {
            if (response.isSuccessful) {
                postValue(ResponseBody(response.body(), null))
            } else {
                val err = ResponseError(response.code(), response.errorBody()?.toString() ?: "")
                postValue(ResponseBody(null, err))
            }
        }
    }


    fun cancel() = if(!call.isCanceled) call.cancel() else Unit
}
