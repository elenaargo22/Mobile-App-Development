package com.example.lab101

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Flower(
    @PrimaryKey(autoGenerate = true) val flowerID: Int,
    @ColumnInfo(name = "spanish_name") val spanishName: String?,
    @ColumnInfo(name = "polish_name") val polishName: String?,
    @ColumnInfo(name = "english_name") val englishName: String?
){
    constructor(spanishName: String?,polishName: String?, englishName: String?):
            this(0, spanishName,polishName, englishName)
}
