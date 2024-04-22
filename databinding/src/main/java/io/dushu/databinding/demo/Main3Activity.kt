package io.dushu.databinding.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.dushu.databinding.R
import io.dushu.databinding.databinding.ActivityMain3Binding

class Main3Activity : AppCompatActivity() {

    lateinit var dataBinding: ActivityMain3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main3)
        val viewModel =
            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application))
                .get(
                    TeamScoreViewModel::class.java
                )
        dataBinding.modelScore = viewModel

        dataBinding.lifecycleOwner = this

    }
}
