package zs.android.camerax

import android.content.Context
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

/**
 *
 * @author zhangshuai
 * @date 2024/11/27 15:48
 * @description 自定义类描述
 */
class FocusImageView @JvmOverloads constructor(
    private val context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var mRectF: RectF

    init {
        setImageResource(R.drawable.icon_focus)
        mRectF = RectF()
    }

    fun setRectF(rectF: RectF){
        mRectF.set(rectF)
        invalidate()
    }
}