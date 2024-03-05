package com.example.notesapp.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.notesapp.database.DateConverter
import kotlinx.parcelize.Parcelize
import java.util.Date
@Parcelize
@Entity(tableName = "note")
data class Note(
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "date") @TypeConverters(DateConverter::class) val date: Date = Date()
) : Parcelable