package com.example.posecoach.data.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.posecoach.data.mediaPipe.Exercise
import com.example.posecoach.data.repository.ExerciseRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExerciseViewModel(
    private val context: Context,
    private val repo: ExerciseRepo = ExerciseRepo()
) : ViewModel() {

    private val _exercises = MutableStateFlow<List<Exercise>>(emptyList())
    val exercises = _exercises.asStateFlow()

    init {
        viewModelScope.launch {
            _exercises.value = repo.loadAllExercises(context)
        }
    }
}