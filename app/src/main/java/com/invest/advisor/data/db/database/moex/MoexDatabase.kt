package com.invest.advisor.data.db.database.moex

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.invest.advisor.data.network.moexResponse.ColumnsDataConverter
import com.invest.advisor.data.db.entity.MarketData
import com.invest.advisor.data.db.entity.Securities

//Application's database that consists of Securities and MarketData entries of MOEX

@Database(
    entities = [Securities::class, MarketData::class],
    version = 1,
    exportSchema = true
)

@TypeConverters(ColumnsDataConverter::class)
abstract class MoexDatabase : RoomDatabase() {
    abstract fun moexDao(): MoexDatabaseDao

    companion object {
        @Volatile
        private var instance: MoexDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MoexDatabase::class.java, "MOEXDatabase.db"
            )
                .build()
    }
}