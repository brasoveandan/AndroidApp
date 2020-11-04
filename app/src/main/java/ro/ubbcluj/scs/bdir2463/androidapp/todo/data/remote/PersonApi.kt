package ro.ubbcluj.scs.bdir2463.androidapp.todo.data.remote

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import ro.ubbcluj.scs.bdir2463.androidapp.todo.data.Person

object PersonApi {
    private const val URL = "http://192.168.0.102:3000/"

    interface Service {
        @GET("/person")
        suspend fun find(): List<Person>

        @GET("/person/{id}")
        suspend fun read(@Path("id") personId: String): Person;

        @Headers("Content-Type: application/json")
        @POST("/person")
        suspend fun create(@Body person: Person): Person

        @Headers("Content-Type: application/json")
        @PUT("/person/{id}")
        suspend fun update(@Path("id") personId: String, @Body person: Person): Person
    }

    private val client: OkHttpClient = OkHttpClient.Builder().build()

    private var gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    val service: Service = retrofit.create(Service::class.java)
}