package io.dushu.viewmodel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.activityViewModels

class ChildFragment : Fragment() {

    private val mMain2ViewModel: Main2ViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_child, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val acTvInfo = view.findViewById<AppCompatTextView>(R.id.acTv_text)
        mMain2ViewModel.getLivedata.observe(viewLifecycleOwner) {
            acTvInfo.text = it
        }
    }
}