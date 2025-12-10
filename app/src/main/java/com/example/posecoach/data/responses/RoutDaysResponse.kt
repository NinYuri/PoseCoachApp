package com.example.posecoach.data.responses

import com.google.gson.annotations.SerializedName
import java.util.UUID

// Cada ejercicio
data class Ejercicio(
    @SerializedName("ejercicio_id") val ejercicioId: UUID,
    val name: String,
    @SerializedName("image_url") val imageUrl: String,
    val series: Int,
    val reps: Int,
    @SerializedName("rest_seconds") val restSeconds: Int
)

// Cada día
data class Dia(
    val nombre: String,
    val musculo: String,
    val detalles: List<Ejercicio>
)

// Principal
data class RoutDaysResponse(
    val id: UUID,
    val dias: Map<String, Dia>
)