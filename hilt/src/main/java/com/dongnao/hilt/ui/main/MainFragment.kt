package com.dongnao.hilt.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dongnao.hilt.R
import com.dongnao.hilt.databinding.FragmentMainBinding
import com.dongnao.hilt.reciver.MyHiltReceiver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var mDataBinding: FragmentMainBinding

    private val mViewModel: MainViewModel by viewModels()

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mDataBinding.unbind()
    }
}