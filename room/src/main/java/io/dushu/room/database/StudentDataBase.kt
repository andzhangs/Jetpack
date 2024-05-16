package io.dushu.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import io.dushu.room.BuildConfig
import io.dushu.room.dao.CourseDao
import io.dushu.room.dao.StudentDao
import io.dushu.room.database.converters.CourseJsonConverter
import io.dushu.room.database.converters.DateConverter
import io.dushu.room.entity.CourseEntity
import io.dushu.room.entity.StudentEntity
import io.dushu.room.entity.relation.StudentCourseJoin
import io.dushu.room.entity.view.StudentEntityView
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

/**
 * author: zhangshuai 6/27/21 9:03 PM
 * email: zhangshuai@dushu365.com
 * mark:
 */
@Database(
    entities = [
        StudentEntity::class,
        CourseEntity::class,
        StudentCourseJoin::class
    ],
    views = [
        StudentEntityView::class
    ],
    version = 1
)
@TypeConverters(
    value = [
        DateConverter::class,
        CourseJsonConverter::class
    ]
)
abstract class StudentDataBase : RoomDatabase() {

    companion object {
        private var mInstance: StudentDataBase? = null

        @Synchronized
        fun getInstance(context: Context): StudentDataBase {
            if (mInstance == null) {
                mInstance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        StudentDataBase::class.java,
                        "student_db.db"
                    )
                        .fallbackToDestructiveMigration() //在升级异常时，重建数据表，同时数据也会丢失
                        .addCallback(CALLBACK)
//                        .addMigrations(*DbHelper.createMigration())
                        .apply {
                            if (!BuildConfig.DEBUG) {  //加密
                                val secret = "attr_20230418".toCharArray()
                                val passphrase = SQLiteDatabase.getBytes(secret)
                                val factory = SupportFactory(passphrase)
                                openHelperFactory(factory)
                            }
                        }
                        .build()
            }
            return mInstance as StudentDataBase
        }

        /**
         * 监听通知
         */
        private object CALLBACK : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                //在新装app时会调用，调用时机为数据库build()之后，数据库升级时不调用此函数
//                Log.i("print_logs", "CALLBACK::onCreate: ")
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
//                Log.i("print_logs", "CALLBACK::onOpen: ")
            }

            override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                super.onDestructiveMigration(db)
//                Log.i("print_logs", "CALLBACK::onDestructiveMigration: ")

            }
        }
    }

    /**
     *------------------------------------------------------------------
     * 用例区
     *------------------------------------------------------------------
     */

    abstract fun getStudentDao(): StudentDao

    abstract fun getCourseDao(): CourseDao

}