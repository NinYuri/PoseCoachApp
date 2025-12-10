package com.example.posecoach.data.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.posecoach.data.mediaPipe.Exercise
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SelectedExVM : ViewModel() {
    private val _selectedExercise = MutableStateFlow<Exercise?>(null)
    val selectedExercise: StateFlow<Exercise?> = _selectedExercise

    fun setExercise(exercise: Exercise) {
        _selectedExercise.value = exercise
    }


    private val _exerciseRules = MutableStateFlow<Exercise?>(null)
    val exerciseRules: StateFlow<Exercise?> = _exerciseRules

    fun loadExerciseRules(context: Context, muscleGroup: String, exerciseName: String) {
        viewModelScope.launch {
            try {
                val fileName = "$muscleGroup.json"
                val json = context.assets.open("exercises/$fileName")
                    .bufferedReader()
                    .use { it.readText() }

                val listType = object : TypeToken<List<Exercise>>() {}.type
                val exercises = Gson().fromJson<List<Exercise>>(json, listType)
                val found = exercises.firstOrNull { it.name.equals(exerciseName, ignoreCase = true) }

                _exerciseRules.value = found
            } catch (e: Exception) {
                e.printStackTrace()
                _exerciseRules.value = null
            }
        }
    }
}