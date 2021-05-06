package com.example.myapplication.model

import androidx.room.*

@Dao
interface TaskDao {


    @Query("SELECT *FROM tasktable")
    fun getAlltasks():List<TaskEntity>

    /**
     * 就是要去查询  未完成且未超时的的任务
     */
    @Query("SELECT *FROM tasktable WHERE isfinsh=:isfinsh and isouttime=:isouttime")
    fun getneedtaks(isfinsh:Boolean,isouttime:Boolean):List<TaskEntity>

    /**
     * 需要去查  是否完成的任务
     */
    @Query("SELECT *FROM tasktable WHERE isfinsh=:isfinsh")
    fun getfinshtask(isfinsh: Boolean):List<TaskEntity>

    /**
     * 去查询是否超时的任务
     */
    @Query("SELECT *FROM tasktable WHERE isouttime=:isouttime")
    fun getisouttimetask(isouttime: Boolean):List<TaskEntity>

    /**
     * 去查询 未完成且未
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inserttask(entity: TaskEntity)

    @Delete
    fun  deletetask(entity: TaskEntity)

    @Update
    fun updatetask(entity: TaskEntity)

   @Query("DELETE FROM tasktable")
    fun deteall()


}