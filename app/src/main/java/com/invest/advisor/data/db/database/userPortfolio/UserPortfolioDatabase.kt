package com.invest.advisor.data.db.database.userPortfolio

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Database that contains stocks added by user.
 */

@Database(entities = [UserPortfolioEntry::class], version = USER_PORTFOLIO_DATABASE_VERSION, exportSchema = false)
abstract class UserPortfolioDatabase : RoomDatabase() {

    abstract val userPortfolioDao: UserPortfolioDao

    companion object {
        @Volatile
        private var INSTANCE: UserPortfolioDatabase? = null

        fun getInstance(
            context: Context
        ): UserPortfolioDatabase {
            synchronized(this) {
                var instance =
                    INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        UserPortfolioDatabase::class.java,
                        USER_PORTFOLIO_DATABASE_NAME
                    ).build()
                }

                INSTANCE = instance
                return instance
            }
        }
    }
}