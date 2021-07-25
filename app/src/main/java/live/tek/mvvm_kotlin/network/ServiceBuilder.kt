package live.tek.mvvm_kotlin.network

import android.util.Log
import live.tek.mvvm_kotlin.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*object ServiceBuilder {
    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com/")
        //.baseUrl("https://5e510330f2c0d300147c034c.mockapi.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun <T> buildService(service: Class<T>): T {
        Log.e("retrofit", "creating")
        return retrofit.create(service)
    }
}*/
/*abstract class ServiceBuilder {

   companion object {
       private val client = OkHttpClient.Builder().build()
       val  api = getRetrofit().create(IApi::class.java)

       @Volatile
       private var INSTANCE: Retrofit? = null

       private fun getRetrofit(): Retrofit {

           val temp = INSTANCE
           if (temp != null) {
               return temp
           }
           synchronized(this) {
               val instance = Retrofit.Builder()
                   .baseUrl("https://jsonplaceholder.typicode.com/")
                   .addConverterFactory(GsonConverterFactory.create())
                   .client(client)
                   .build()
               INSTANCE = instance
               return instance
           }
       }
   }
}*/

/*object ServiceBuilder {
   val myApi : IApi by lazy {
       Log.e("retrofit","creating")
       val retrofit = Retrofit.Builder()
           .baseUrl(BuildConfig.API_URL)
           .addConverterFactory(GsonConverterFactory.create())
           .client(OkHttpClient.Builder().build())
           .build()
       return@lazy retrofit.create(IApi::class.java)
   }
}*/

abstract class ServiceBuilder {
    companion object {
        val myApi: IApi by lazy {
            return@lazy getRetrofit().create(IApi::class.java)
        }

        @Volatile
        private var INSTANCE: Retrofit? = null

        private fun getRetrofit(): Retrofit {

            val temp = INSTANCE
            if (temp != null) {
                return temp
            }
            synchronized(this) {
//                Log.e("retrofit", "creating")
                val instance = Retrofit.Builder()
                    .baseUrl(ApiUrl.url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(OkHttpClient.Builder().build())
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
object ApiUrl{
    var url ="https://jsonplaceholder.typicode.com/"
}