package zs.android.contentpager

import android.content.ContentResolver
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.contentpager.content.ContentPager
import androidx.contentpager.content.LoaderQueryRunner

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pager= ContentPager(MyContentResolver(this), LoaderQueryRunner(this,loaderManager))
    }

    private inner class MyContentResolver(context: Context):ContentResolver(context){

    }


}