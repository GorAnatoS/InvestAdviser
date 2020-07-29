package com.invest.advisor.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.invest.advisor.data.db.entity.ColumnsDataConverter
import com.invest.advisor.data.db.entity.Securities

@Database(
    entities = [Securities::class],
    version = 1
)

@TypeConverters(ColumnsDataConverter::class)
abstract class StocksDatabase : RoomDatabase() {
    abstract fun currentStocksList(): CurrentStocksSecIdDao

    companion object {
        @Volatile private var instance: StocksDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                    StocksDatabase::class.java, "MOEXDatabase.db")
                    .build()
    }
}