package com.example.posecoach.data.repository

import android.content.Context
import com.example.posecoach.data.mediaPipe.Exercise
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ExerciseRepo {
    fun loadExercisesFromAsset(context: Context, fileName: String): List<Exercise> {
        val json = context.assets.open("exercises/$fileName")
            .bufferedReader()
            .use { it.readText() }

        val gson = Gson()
        val type = object : TypeToken<List<Exercise>>() {}.type
        return gson.fromJson(json, type)
    }

    fun loadAllExercises(context: Context): List<Exercise> {
        val files = listOf(
            "pierna.json",
            "gluteo.json",
            "pecho.json",
            "espalda.json",
            "hombros.json",
            "abdomen.json",
            "brazos.json",
            "cuerpo_completo.json"
        )

        return files.flatMap { file -> loadExercisesFromAsset(context, file) }
    }
}