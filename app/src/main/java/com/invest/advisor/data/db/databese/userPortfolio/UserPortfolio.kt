package com.invest.advisor.data.db.databese.userPortfolio

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.invest.advisor.data.db.entity.USER_PORTFOLIO_DATABASE_NAME
import com.invest.advisor.data.db.entity.USER_PORTFOLIO_DATABASE_VERSION
import com.invest.advisor.data.db.entity.UserPortfolioEntry


/**
 * Database that contains stocks added by user.
 */

@Database(entities = [UserPortfolioEntry::class], version = USER_PORTFOLIO_DATABASE_VERSION, exportSchema = false)
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
                        USER_PORTFOLIO_DATABASE_NAME
                    ).build()
                }

                INSTANCE = instance
                return instance
            }
        }
    }
}