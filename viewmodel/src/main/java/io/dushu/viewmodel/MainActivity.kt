package io.dushu.viewmodel

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private lateinit var mViewModel: MyViewModel
    private lateinit var textView :AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application))[MyViewModel::class.java]

        textView = findViewById(R.id.textView)

        textView.text = mViewModel.number.toString()
    }


    fun plusNumber(view: View) {
        textView.text = (++mViewModel.number).toString()
        mViewModel.showToast(textView.text.toString())
    }
}
