package io.jetpack.workmanager.demo.jobs

import android.os.Bundle
import io.jetpack.workmanager.base.BaseActivity
import io.jetpack.workmanager.databinding.ActivityMainJobBinding
import io.jetpack.workmanager.utils.viewBinding

class MainJobActivity : BaseActivity() {

    private val mBinding by viewBinding(ActivityMainJobBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding.acBtnStartJob.setOnClickListener {
            DemoJobCreator.scheduleHob(this)
        }

        mBinding.acBtnCancelJob.setOnClickListener {
            DemoJobCreator.cancelByJobId(1)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        DemoJobCreator.destroy()
    }
}