package ru.ircover.socialmobile.model

import com.google.gson.annotations.SerializedName

enum class Period {
    Once, @SerializedName("Every month") Monthly
}