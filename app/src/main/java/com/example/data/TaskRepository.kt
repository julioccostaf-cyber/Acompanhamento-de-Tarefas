package com.example.data

import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    val allTasksWithSubTasks: Flow<List<TaskWithSubTasks>> = taskDao.getAllTasksWithSubTasks()

    fun getTasksForDate(date: String): Flow<List<TaskWithSubTasks>> {
        return taskDao.getTasksWithSubTasksForDate(date)
    }

    suspend fun getTaskById(taskId: Long): Task? {
        return taskDao.getTaskById(taskId)
    }

    suspend fun insertTask(task: Task): Long {
        return taskDao.insertTask(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    suspend fun insertSubTask(subTask: SubTask): Long {
        val id = taskDao.insertSubTask(subTask)
        recalculateTaskProgress(subTask.taskId)
        return id
    }

    suspend fun updateSubTask(subTask: SubTask) {
        taskDao.updateSubTask(subTask)
        recalculateTaskProgress(subTask.taskId)
    }

    suspend fun deleteSubTask(subTask: SubTask) {
        taskDao.deleteSubTask(subTask)
        recalculateTaskProgress(subTask.taskId)
    }

    suspend fun deleteSubTasksForTask(taskId: Long) {
        taskDao.deleteSubTasksForTask(taskId)
        recalculateTaskProgress(taskId)
    }

    private suspend fun recalculateTaskProgress(taskId: Long) {
        val taskWithSub = taskDao.getTaskWithSubTasksSync(taskId) ?: return
        val task = taskWithSub.task
        val subTasks = taskWithSub.subTasks
        if (subTasks.isEmpty()) {
            val isCompleted = task.progressPercentage == 100
            taskDao.updateTask(task.copy(isCompleted = isCompleted))
        } else {
            val completedCount = subTasks.count { it.isCompleted }
            val newProgress = (completedCount * 100) / subTasks.size
            val isCompleted = newProgress == 100
            taskDao.updateTask(
                task.copy(
                    progressPercentage = newProgress,
                    isCompleted = isCompleted
                )
            )
        }
    }
}
