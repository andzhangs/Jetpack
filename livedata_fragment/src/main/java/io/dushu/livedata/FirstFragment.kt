package io.dushu.livedata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

/**
 * author: zhangshuai 6/26/21 9:57 PM
 * email: zhangshuai@dushu365.com
 * mark:
 */
class FirstFragment : Fragment() {

    private lateinit var rootView: View
    private lateinit var seekBar: SeekBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_first, container, false)
        seekBar = rootView.findViewById(R.id.seekBar1)

        activity?.let {
            val viewModel = ViewModelProvider(
                it,
                ViewModelProvider.AndroidViewModelFactory(it.application)
            )[MyViewModel::class.java]

            viewModel.getProgress().observe(requireActivity()) { num ->
                seekBar.progress = num
            }

            //方式一
            rootView.findViewById<AppCompatButton>(R.id.mBtnSend).setOnClickListener {
                parentFragmentManager.setFragmentResult(SecondFragment.KEY, Bundle().apply {
                    this.putString(SecondFragment.KEY, "Successes")
                })
            }

            //方式二
            seekBar.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    viewModel.setProgress(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            })
        }

        return rootView
    }
}