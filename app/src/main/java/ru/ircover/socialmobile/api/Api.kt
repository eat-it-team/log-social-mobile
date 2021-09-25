package ru.ircover.socialmobile.api

import com.google.gson.JsonObject
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.ircover.socialmobile.model.Subsidy

interface Api {
    @GET("api/registry/v1/subsidy")
    fun getUserSubsidies(@Query("userId") userId: Int): Single<ArrayList<Subsidy>>

    @GET("api/registry/v1/subsidy/all")
    fun getAllSubsidies(): Single<ArrayList<Subsidy>>
}