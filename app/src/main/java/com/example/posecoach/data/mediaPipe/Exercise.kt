package com.example.posecoach.data.mediaPipe

data class Exercise(
    val name: String,
    val muscle_group: String,
    val ideal_angles: Map<String, AngleData>,
    val common_mistakes: List<ExerciseMistake>
)

data class AngleData(
    val ideal: Int,
    val min: Int,
    val max: Int,
    val landmarks: List<Int>
)

data class ExerciseMistake(
    val condicion: String,
    val error: String
)