package ro.ubbcluj.scs.bdir2463.androidapp.todo.data.remote

import retrofit2.http.*
import ro.ubbcluj.scs.bdir2463.androidapp.core.Api
import ro.ubbcluj.scs.bdir2463.androidapp.todo.data.Person

object PersonApi {
    interface Service {
        @GET("/api/person")
        suspend fun find(): List<Person>

        @GET("/api/person/{id}")
        suspend fun read(@Path("id") personId: String): Person;

        @Headers("Content-Type: application/json")
        @POST("/api/person")
        suspend fun create(@Body person: Person): Person

        @Headers("Content-Type: application/json")
        @PUT("/api/person/{id}")
        suspend fun update(@Path("id") personId: String, @Body person: Person): Person
    }

    val service: Service = Api.retrofit.create(Service::class.java)
}