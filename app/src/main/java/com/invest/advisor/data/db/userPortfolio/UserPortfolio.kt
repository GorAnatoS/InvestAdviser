package com.invest.advisor.data.db.userPortfolio

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


/**
 * Created by qsufff on 9/13/2020.
 */


@Database(entities = [UserPortfolioEntry::class], version = DATABASE_VERSION)
abstract class UserPortfolio : RoomDatabase() {

    abstract val userPortfolioDao: UserPortfolioDao

    companion object {


        @Volatile
        private var INSTANCE: UserPortfolio? = null

        fun getInstance(
            context: Context
        ): UserPortfolio {
            synchronized(this) {
                var instance =
                    INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        UserPortfolio::class.java,
                        DATABASE_NAME
                    ).build()
                }

                INSTANCE = instance
                return instance
            }
        }
    }
}