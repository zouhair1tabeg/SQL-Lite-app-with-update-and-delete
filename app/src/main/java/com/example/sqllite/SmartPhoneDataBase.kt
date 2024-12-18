package com.example.sqllite

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update

@Entity(tableName = "smartphone_table")
data class SmartPhone(
    @PrimaryKey(autoGenerate = true) val id : Int=0,
    val nom : String,
    val prix : Double,
    val image : String
)

@Dao
interface SmartphoneDao{
    @Insert
    fun insertSmartphone(smartPhone: SmartPhone)

    @Query("SELECT * FROM smartphone_table")
    fun getSmartPhone(): List<SmartPhone>

    @Update
    fun updateSmartphone(smartphone: SmartPhone)

    @Query("DELETE FROM smartphone_table WHERE id = :id")
    fun deleteSmartphoneById(id: Int)
}

@Database(entities = [SmartPhone::class], version = 1, exportSchema = false)
abstract class SmartPhoneDatabase : RoomDatabase(){
    abstract fun smartphoneDao(): SmartphoneDao

    companion object{
        private var INSTANCE: SmartPhoneDatabase? = null

        fun getDataBase(context: Context):SmartPhoneDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SmartPhoneDatabase::class.java,
                    "smartphone_database"
                ).allowMainThreadQueries()
                    .build()
                INSTANCE=instance
                instance
            }
        }
    }
}