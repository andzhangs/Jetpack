package io.dushu.room.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import io.dushu.room.dao.StudentDao
import io.dushu.room.entity.Student

/**
 * author: zhangshuai 6/27/21 9:03 PM
 * email: zhangshuai@dushu365.com
 * mark:
 */
@Database(entities = [Student::class], version = 1, exportSchema = true)
abstract class StudentDataBase : RoomDatabase() {


    companion object {
        private var mInstance: StudentDataBase? = null
        private val migrationList = arrayOf(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)

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
//                        .addMigrations(*migrationList)
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
                Log.i("print_logs", "CALLBACK::onCreate: ")
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                Log.i("print_logs", "CALLBACK::onOpen: ")

            }

            override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                super.onDestructiveMigration(db)
                Log.i("print_logs", "CALLBACK::onDestructiveMigration: ")

            }
        }


        /**
         *------------------------------------------------------------------
         * 升级区
         *------------------------------------------------------------------
         */
        private object MIGRATION_1_2 : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                Log.i("print_logs", "MIGRATION_1_2::migrate: ")
                database.execSQL("ALTER TABLE student ADD COLUMN sex INTEGER NOT NULL DEFAULT 1")
            }
        }

        private object MIGRATION_2_3 : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                Log.i("print_logs", "MIGRATION_2_3::migrate: ")
                database.execSQL("ALTER TABLE student ADD COLUMN mark INTEGER NOT NULL DEFAULT 0")
            }
        }

        /**
         * 数据库销毁和重建策略
         * 将表字段类型："Integer"修改成"Text"
         * 1、创建一张符合表结构要求的临时表temp_student
         * 2、将数据从student复制到临时表temp_student
         * 3、删除旧表student
         * 4、将临时表temp_student重命名为student
         */
        private object MIGRATION_3_4 : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS temp_student (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `age` INTEGER NOT NULL, `sex` INTEGER NOT NULL, `mark` TEXT DEFAULT 'NULL')")
                database.execSQL("INSERT INTO temp_student(name,age,sex,mark) SELECT name,age,sex,mark FROM student")
                database.execSQL("DROP TABLE student")
                database.execSQL("ALTER TABLE temp_student RENAME TO student")
            }
        }

    }

    /**
     *------------------------------------------------------------------
     * 用例区
     *------------------------------------------------------------------
     */

    abstract fun getStudentDao(): StudentDao


}