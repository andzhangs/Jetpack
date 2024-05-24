package zs.android.jetpack.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * @author zhangshuai@attrsense.com
 * @date 2023/5/25 18:35
 * @description
 */
abstract class BaseActivity<DB : ViewDataBinding> : AppCompatActivity() {

    lateinit var mDataBinding: DB

    open fun onCreateBefore(savedInstanceState: Bundle?) {

    }

    @LayoutRes
    protected abstract fun setLayoutResId(): Int

    protected abstract fun initView(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onCreateBefore(savedInstanceState)
        mDataBinding = DataBindingUtil.setContentView(this, setLayoutResId())
        mDataBinding.lifecycleOwner = this
        initView(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mDataBinding.unbind()
    }

}