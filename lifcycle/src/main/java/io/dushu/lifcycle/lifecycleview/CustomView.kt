package io.dushu.lifcycle.lifecycleview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.doOnAttach
import androidx.core.view.doOnDetach
import androidx.core.view.doOnLayout
import androidx.core.view.doOnNextLayout
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope

/**
 *
 * @author zhangshuai
 * @date 2023/12/21 15:01
 * @description 自定义类描述
 */
class CustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {
    
    init {
        //当视图被附加到窗口时调用。
        doOnAttach {
            Log.w("print_logs", "CustomView::: doOnAttach 在视图附加到窗口时执行的代码")
            findViewTreeLifecycleOwner()?.lifecycleScope?.launchWhenCreated {
                Log.w("print_logs", "CustomView::: launchWhenCreated")
            }
        }
        //当视图的布局发生变化时调用，包括大小、位置等
        doOnLayout {
            Log.w("print_logs", "CustomView::: doOnLayout 在视图布局发生变化时执行的代码")
        }

        //当下一次布局过程中发生时调用。与 doOnLayout 不同，它在下一次布局中执行，而不是当前布局过程中
        doOnNextLayout {
            Log.w("print_logs", "CustomView::: doOnNextLayout 在下一次布局过程中执行的代码")
        }
        //在视图绘制之前调用。通常用于执行一些需要在绘制之前完成的操作，如布局计算
        doOnPreDraw {
            Log.w("print_logs", "CustomView::: doOnPreDraw 在视图绘制之前执行的代码")
        }
        //当视图从窗口中分离时调用。
        doOnDetach {
            Log.w("print_logs", "CustomView::: doOnDetach 在视图从窗口中分离时执行的代码")
        }
    }
}