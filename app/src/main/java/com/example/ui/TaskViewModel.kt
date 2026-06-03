package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.SubTask
import com.example.data.Task
import com.example.data.TaskRepository
import com.example.data.TaskWithSubTasks
import com.example.data.CustomPreset
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    val selectedDate = MutableStateFlow(getTodayDateString())

    val tasksForSelectedDate: StateFlow<List<TaskWithSubTasks>> = selectedDate
        .flatMapLatest { date ->
            repository.getTasksForDate(date)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val allTasks: StateFlow<List<TaskWithSubTasks>> = repository.allTasksWithSubTasks
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val customPresets: StateFlow<List<CustomPreset>> = repository.allCustomPresets
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun setDate(date: String) {
        selectedDate.value = date
    }

    fun insertTask(
        title: String,
        description: String,
        category: String,
        difficulty: String,
        date: String,
        subtaskTitles: List<String>
    ) {
        viewModelScope.launch {
            val xpReward = when (difficulty) {
                "CANDY_EASY" -> 30
                "CANDY_MEDIUM" -> 60
                "CANDY_HARD" -> 100
                else -> 50
            }
            val newTask = Task(
                title = title,
                description = description,
                category = category,
                difficulty = difficulty,
                date = date,
                xpReward = xpReward,
                progressPercentage = 0,
                isCompleted = false
            )
            val taskId = repository.insertTask(newTask)
            subtaskTitles.forEach { subTitle ->
                if (subTitle.isNotBlank()) {
                    repository.insertSubTask(
                        SubTask(
                            taskId = taskId,
                            title = subTitle,
                            isCompleted = false
                        )
                    )
                }
            }
        }
    }

    fun toggleTaskCompletion(taskWithSubs: TaskWithSubTasks) {
        viewModelScope.launch {
            val task = taskWithSubs.task
            val subTasks = taskWithSubs.subTasks
            val newCompletedState = !task.isCompleted
            val newProgress = if (newCompletedState) 100 else 0

            // Update all subtasks
            subTasks.forEach { subTask ->
                repository.updateSubTask(subTask.copy(isCompleted = newCompletedState))
            }

            // Update the main task progress and completion status
            repository.updateTask(
                task.copy(
                    progressPercentage = newProgress,
                    isCompleted = newCompletedState
                )
            )
        }
    }

    fun setTaskProgressManual(task: Task, progress: Int) {
        viewModelScope.launch {
            val isCompleted = progress == 100
            repository.updateTask(
                task.copy(
                    progressPercentage = progress,
                    isCompleted = isCompleted
                )
            )
        }
    }

    fun toggleSubTask(subTask: SubTask) {
        viewModelScope.launch {
            repository.updateSubTask(subTask.copy(isCompleted = !subTask.isCompleted))
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun updateTaskDetails(
        task: Task,
        newTitle: String,
        newDescription: String,
        newCategory: String,
        newDifficulty: String,
        newProgress: Int,                     // used if no subtasks
        existingSubtasksToUpdate: List<SubTask>, // already modified list of subtasks
        newSubtaskTitlesToAdd: List<String>      // new subtasks to add
    ) {
        viewModelScope.launch {
            val xpReward = when (newDifficulty) {
                "CANDY_EASY" -> 30
                "CANDY_MEDIUM" -> 60
                "CANDY_HARD" -> 100
                else -> 50
            }

            // 1. Update existing subtasks
            existingSubtasksToUpdate.forEach { subTask ->
                repository.updateSubTask(subTask)
            }

            // 2. Add new subtasks
            newSubtaskTitlesToAdd.forEach { subTitle ->
                if (subTitle.isNotBlank()) {
                    repository.insertSubTask(
                        SubTask(
                            taskId = task.id,
                            title = subTitle,
                            isCompleted = false
                        )
                    )
                }
            }

            // 3. If there are no subtasks in total at the end, set the progress manually.
            // Let's check updated total subtasks
            val currentTaskWithSubs = repository.getTaskById(task.id)
            // Wait, we can fetch all subtasks for task to determine count
            val subtasksLeft = repository.allTasksWithSubTasks
                .stateIn(viewModelScope)
                .value
                .firstOrNull { it.task.id == task.id }
                ?.subTasks ?: emptyList()

            val progressValue = if (subtasksLeft.isEmpty()) {
                newProgress
            } else {
                task.progressPercentage // Will be auto-recalculated anyway by DB hooks
            }

            val isCompletedValue = progressValue == 100

            repository.updateTask(
                task.copy(
                    title = newTitle,
                    description = newDescription,
                    category = newCategory,
                    difficulty = newDifficulty,
                    xpReward = xpReward,
                    progressPercentage = progressValue,
                    isCompleted = isCompletedValue
                )
            )
        }
    }

    fun deleteSubTaskDirectly(subTask: SubTask) {
        viewModelScope.launch {
            repository.deleteSubTask(subTask)
        }
    }

    fun insertCustomPreset(
        title: String,
        description: String,
        category: String,
        difficulty: String,
        emoji: String,
        subtasksRaw: String
    ) {
        viewModelScope.launch {
            repository.insertCustomPreset(
                CustomPreset(
                    title = title,
                    description = description,
                    category = category,
                    difficulty = difficulty,
                    emoji = emoji,
                    subtasksRaw = subtasksRaw
                )
            )
        }
    }

    fun deleteCustomPreset(preset: CustomPreset) {
        viewModelScope.launch {
            repository.deleteCustomPreset(preset)
        }
    }

    companion object {
        fun getTodayDateString(): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return sdf.format(Date())
        }
        
        fun formatDateToDisplay(dateString: String): String {
            try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date = inputFormat.parse(dateString) ?: return dateString
                val outputFormat = SimpleDateFormat("dd 'de' MMMM", Locale("pt", "BR"))
                return outputFormat.format(date)
            } catch (e: Exception) {
                return dateString
            }
        }
    }
}

class TaskViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
