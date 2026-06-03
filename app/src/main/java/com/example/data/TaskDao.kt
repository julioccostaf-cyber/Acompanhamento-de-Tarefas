package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Transaction
    @Query("SELECT * FROM tasks ORDER BY id DESC")
    fun getAllTasksWithSubTasks(): Flow<List<TaskWithSubTasks>>

    @Transaction
    @Query("SELECT * FROM tasks WHERE date = :date ORDER BY id DESC")
    fun getTasksWithSubTasksForDate(date: String): Flow<List<TaskWithSubTasks>>

    @Query("SELECT * FROM tasks WHERE id = :taskId LIMIT 1")
    suspend fun getTaskById(taskId: Long): Task?

    @Transaction
    @Query("SELECT * FROM tasks WHERE id = :taskId LIMIT 1")
    suspend fun getTaskWithSubTasksSync(taskId: Long): TaskWithSubTasks?

    @Query("SELECT * FROM subtasks WHERE taskId = :taskId")
    suspend fun getSubTasksForTaskSync(taskId: Long): List<SubTask>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubTask(subTask: SubTask): Long

    @Update
    suspend fun updateSubTask(subTask: SubTask)

    @Delete
    suspend fun deleteSubTask(subTask: SubTask)

    @Query("DELETE FROM subtasks WHERE taskId = :taskId")
    suspend fun deleteSubTasksForTask(taskId: Long)
}
