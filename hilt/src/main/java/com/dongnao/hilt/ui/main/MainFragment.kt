package com.dongnao.hilt.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dongnao.hilt.R
import com.dongnao.hilt.TestHelper
import com.dongnao.hilt.component.MyComponent
import com.dongnao.hilt.databinding.FragmentMainBinding
import com.dongnao.hilt.intoset.Plugin
import com.dongnao.hilt.intoset.PluginA
import com.dongnao.hilt.intoset.PluginB
import com.dongnao.hilt.reciver.MyHiltReceiver
import dagger.hilt.android.AndroidEntryPoint
import dagger.internal.DaggerGenerated
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var mDataBinding: FragmentMainBinding

    private val mViewModel: MainViewModel by viewModels()

    @Inject
    lateinit var mTestHelper: TestHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        mDataBinding.lifecycleOwner = this
        mDataBinding.vm = mViewModel
        return mDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDataBinding.acTvTime.setOnClickListener {
            requireActivity().sendBroadcast(Intent().apply {
                action = MyHiltReceiver.ACTION_SEND
            })
        }

        mTestHelper.loadPrint()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        mDataBinding.unbind()
    }
}