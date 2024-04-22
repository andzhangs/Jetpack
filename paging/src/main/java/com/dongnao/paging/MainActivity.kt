package com.dongnao.paging

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dongnao.paging.bean.DataX
import com.dongnao.paging.databinding.ActivityMainBinding
import com.dongnao.paging.databinding.LayoutItemMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var mDataBinding: ActivityMainBinding
    private val mViewModel: MainViewModel by viewModels()

    private val mWindowInsetsControllerCompat: WindowInsetsControllerCompat by lazy {
        WindowCompat.getInsetsController(
            window,
            window.decorView
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        //方式一：代码设置
        //方式,设置一个带有深色内容的浅色状态栏
//        mWindowInsetsControllerCompat.isAppearanceLightStatusBars = true
//        mWindowInsetsControllerCompat.isAppearanceLightNavigationBars = true

        mWindowInsetsControllerCompat.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mDataBinding.lifecycleOwner = this
        lifecycle.addObserver(mViewModel)
        initRecyclerView()

//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                mAdapter.loadStateFlow.collect {
//                    mDataBinding.prependProgress.isVisible = it.source.prepend is LoadState.Loading
//                    mDataBinding.appendProgress.isVisible = it.source.prepend is LoadState.Loading
//                }
//            }
//        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                mViewModel.pagingDataFlow.collectLatest {
                    mAdapter.submitData(it)
                }
            }
        }

        //通过监听切换
        window.decorView.setOnApplyWindowInsetsListener { v, insets ->
            //方式一：代码设置状态栏透明度
//            window.statusBarColor = Color.TRANSPARENT
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (insets.isVisible(WindowInsetsCompat.Type.navigationBars())) {
                    mDataBinding.acBtnToggle.setOnClickListener {
                        mWindowInsetsControllerCompat.hide(WindowInsetsCompat.Type.navigationBars())
                        mDataBinding.showBottomView = true

                    }
                } else {
                    mDataBinding.acBtnToggle.setOnClickListener {
                        mWindowInsetsControllerCompat.show(WindowInsetsCompat.Type.navigationBars())
                        mDataBinding.showBottomView = false
                    }
                }
            }
            v.onApplyWindowInsets(insets)
        }

    }

    private fun initRecyclerView() {
        with(mAdapter) {
            addLoadStateListener {
                mDataBinding.appendProgress.isVisible = it.source.prepend is LoadState.Loading
            }

            addOnPagesUpdatedListener {
                Log.i("print_logs", "mAdapter.addOnPagesUpdatedListener")
            }
        }

        with(mDataBinding.recyclerView) {
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            adapter = mAdapter
        }

    }

    private val mAdapter = object :
        PagingDataAdapter<DataX, ImageItemViewHolder>(object : DiffUtil.ItemCallback<DataX>() {
            override fun areItemsTheSame(oldItem: DataX, newItem: DataX): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DataX, newItem: DataX): Boolean {
                return oldItem.id == newItem.id
            }
        }) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageItemViewHolder {
            val binding = DataBindingUtil.inflate<LayoutItemMainBinding>(
                LayoutInflater.from(parent.context),
                R.layout.layout_item_main,
                parent,
                false
            )
            return ImageItemViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ImageItemViewHolder, position: Int) {
            holder.load(getItem(position))

            holder.itemView.setOnClickListener {

                //通过手动设置切换
                val isNavBarVisible = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val windowInsets = window.decorView.rootWindowInsets
                    windowInsets.isVisible(WindowInsetsCompat.Type.navigationBars())
                } else {
                    false
                }
                mWindowInsetsControllerCompat.apply {
                    if (isNavBarVisible) {
//                        hide(WindowInsetsCompat.Type.statusBars())
                        hide(WindowInsetsCompat.Type.navigationBars())
                        mDataBinding.showBottomView = true
                    } else {
//                        show(WindowInsetsCompat.Type.statusBars())
                        show(WindowInsetsCompat.Type.navigationBars())
                        mDataBinding.showBottomView = false
                    }
                }
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "MainActivity::onWindowFocusChanged: hasFocus= $hasFocus")
        }
    }


    private inner class ImageItemViewHolder(private val itemBinding: LayoutItemMainBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun load(data: DataX?) {
            data?.also {
                itemBinding.data = it
                Glide.with(itemBinding.acIvIcon.context)
                    .load(it.envelopePic)
                    .apply(RequestOptions.bitmapTransform(TransformationUtils.getRandom()))
                    .into(itemBinding.acIvIcon)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(mViewModel)
        mDataBinding.unbind()
    }
}