package com.jetpack.sharing

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class PickActivity : AppCompatActivity() {

    companion object {

        const val KEY_RESULT = "result"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pick)

        findViewById<AppCompatButton>(R.id.acBtn_return).setOnClickListener {
            loadResult()
        }



        findViewById<AppCompatButton>(R.id.acBtn_send).setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/jpg"
            }
            startActivityForResult(intent, 0)
        }
    }

    private fun loadResult(){
        val intent = Intent().apply {
            putExtra(KEY_RESULT, "I am from PickActivity!")
        }
        setResult(RESULT_OK, intent)
        finish()
    }
}