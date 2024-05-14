package io.dushu.room.database

import android.util.Log
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 *
 * @author zhangshuai
 * @date 2024/5/14 15:00
 * @description 自定义类描述
 */
object DbHelper {

    @JvmStatic
    fun createMigration() = arrayOf(
        MIGRATION_1_2
    )

    /**
     *------------------------------------------------------------------
     * 升级区
     *------------------------------------------------------------------
     */
    //测试：表Student新增字段level_url非空，升级语句未设置默认值时
    //修复：升级语句设置默认值
    @JvmStatic
    val MIGRATION_1_2= object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            Log.i("print_logs", "MIGRATION_1_2::migrate: ")

//            database.execSQL("ALTER TABLE student ADD COLUMN level_url TEXT NOT NULL")

            //修复
            database.execSQL("ALTER TABLE student ADD COLUMN level_url TEXT NOT NULL DEFAULT ''")
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * 数据库销毁和重建策略
     * 将表字段类型："Integer"修改成"Text"
     * 1、创建一张符合表结构要求的临时表temp_student
     * 2、将数据从student复制到临时表temp_student
     * 3、删除旧表student
     * 4、将临时表temp_student重命名为student
     */
//        private object MIGRATION_3_4 : Migration(3, 4) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("CREATE TABLE IF NOT EXISTS temp_student (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `age` INTEGER NOT NULL, `sex` INTEGER NOT NULL, `mark` TEXT DEFAULT 'NULL')")
//                database.execSQL("INSERT INTO temp_student(name,age,sex,mark) SELECT name,age,sex,mark FROM student")
//                database.execSQL("DROP TABLE student")
//                database.execSQL("ALTER TABLE temp_student RENAME TO student")
//            }
//        }
}