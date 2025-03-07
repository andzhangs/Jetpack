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
    fun createMigration() :Array<Migration> = arrayOf(
//        MIGRATION_1_2
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

//            database.execSQL("ALTER TABLE table_student ADD COLUMN level_url TEXT NOT NULL")

            //修复
//            database.execSQL("ALTER TABLE table_student ADD COLUMN level_url TEXT NOT NULL DEFAULT ''")

            database.execSQL("DELETE FROM table_project WHERE id NOT IN (SELECT MAX(ID) FROM table_project GROUP BY project_name)")
            database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_table_project_project_name ON table_project (project_name)")
        }
    }
}