package com.example.lab101
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FlowerDAO {
    @Insert
    fun insertAll(flower: Flower)

    @Delete
    fun delete(flower: Flower)

    @Query("SELECT * FROM flower")
    fun getAll(): List<Flower>

    @Query("SELECT * FROM flower where flowerID IN (:flowerIDs)")
    fun getAllByIDs(flowerIDs: IntArray): List<Flower>
}