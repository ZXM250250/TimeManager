package com.example.myapplication.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tasktable")
class TaskEntity(
        @ColumnInfo(name = "taskName")
        var taskName:String,
        @ColumnInfo(name = "deadline")
        var deadline:String,
        @ColumnInfo(name = "remindtime")
        var remindtime:String,
        @ColumnInfo(name = "way")
        var way:String,
        @ColumnInfo(name = "remark")
        var remark:String,
        @ColumnInfo(name = "list")
        var list:String,
        @ColumnInfo(name = "description")
        var description:String,
        @ColumnInfo(name = "isfinsh")
        var isfinish:Boolean,
        @ColumnInfo(name = "isouttime")
        var isouttime:Boolean
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}