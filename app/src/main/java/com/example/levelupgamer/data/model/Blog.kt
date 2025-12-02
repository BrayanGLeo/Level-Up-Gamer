package com.example.levelupgamer.data.model

import com.google.gson.annotations.SerializedName

data class Blog(
    @SerializedName("id")
    val id: String,

    @SerializedName("titulo")
    val title: String,

    @SerializedName("autor")
    val author: String,

    @SerializedName("fechaPublicacion")
    val date: String,

    @SerializedName("contenido")
    val summary: String,

    @SerializedName("contenido")
    val content: String,

    @SerializedName("imagenUrl")
    val image: String
)