package com.dongnao.paging

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dongnao.paging.adapter.PagingListAdapter
import com.dongnao.paging.adapter.SheetBottomLoadMoreAdapter
import com.dongnao.paging.adapter.TopRefreshAdapter
import com.dongnao.paging.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var mDataBinding: ActivityMainBinding
    private val mViewModel: MainViewModel by viewModels()

    private val mWindowInsetsControllerCompat:WindowInsetsControllerCompat by lazy{
        WindowCompat.getInsetsController(window, window.decorView)
    }

    private val mAdapter = PagingListAdapter{
        //通过手动设置切换
        val isNavBarVisible = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowInsets = window.decorView.rootWindowInsets
            windowInsets.isVisible(WindowInsetsCompat.Type.navigationBars())
        } else {
            false
        }

        with(mWindowInsetsControllerCompat) {
            if (isNavBarVisible) {
                hide(WindowInsetsCompat.Type.statusBars())
                hide(WindowInsetsCompat.Type.navigationBars())
                mDataBinding.showBottomView = true
            } else {
                show(WindowInsetsCompat.Type.statusBars())
                show(WindowInsetsCompat.Type.navigationBars())
                mDataBinding.showBottomView = false
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
//        Log.i("print_logs", "MainActivity::onWindowFocusChanged: hasFocus= $hasFocus")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        //方式一：代码设置
        //方式,设置一个带有深色内容的浅色状态栏
//        mWindowInsetsControllerCompat.isAppearanceLightStatusBars = true
//        mWindowInsetsControllerCompat.isAppearanceLightNavigationBars = true

        mWindowInsetsControllerCompat.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mDataBinding.lifecycleOwner = this
        lifecycle.addObserver(mViewModel)

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

        val params=mDataBinding.toolbar.layoutParams as ViewGroup.MarginLayoutParams
        params.setMargins(0,getStatusBarHeight(this),0,0)

        initRecyclerView()

//        mDataBinding.swipeRefreshLayout.setOnRefreshListener {
//            mAdapter.refresh()
//        }

        lifecycleScope.launch {
            mViewModel.pagingDataFlow
                .flowWithLifecycle(lifecycle,Lifecycle.State.STARTED)
                .collectLatest {
                    mAdapter.submitData(it)
                }
        }

        lifecycleScope.launch {
            mViewModel.getCurrentPageFlow.collectLatest {
                Log.i("print_logs", "prevKey: ${it.first}, currentKey：${it.second}, nextKey：${it.third}")
            }
        }
    }

    private fun initRecyclerView() {
        with(mAdapter) {
            addLoadStateListener {
//                if (BuildConfig.DEBUG) { Log.i("print_logs", "Load-2: $it, ") }
                mDataBinding.appendProgress.isVisible = it.append is LoadState.Loading
//                mDataBinding.swipeRefreshLayout.isRefreshing = it.refresh is LoadState.Loading

                when (it.append) {
                    is LoadState.NotLoading -> {
//                        Log.i("print_logs", "LoadState.NotLoading！")
                    }
                    is LoadState.Loading -> {
//                        Log.i("print_logs", "LoadState.Loading！")
                    }
                    is LoadState.Error -> {
                        Log.e("print_logs", "LoadState.Error: ${(it.append as LoadState.Error).error}")
                    }
                }
            }

            when (stateRestorationPolicy) {
                RecyclerView.Adapter.StateRestorationPolicy.ALLOW -> {
                    Log.i("print_logs", "stateRestorationPolicy: ALLOW")
                }
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY -> {
                    Log.i("print_logs", "stateRestorationPolicy: PREVENT_WHEN_EMPTY")
                }
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT -> {
                    Log.i("print_logs", "stateRestorationPolicy: PREVENT")
                }
            }

            addOnPagesUpdatedListener {
//                Log.i("print_logs", "mAdapter.addOnPagesUpdatedListener")
            }
        }

        with(mDataBinding.recyclerView) {
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            adapter = mAdapter.withLoadStateHeaderAndFooter(TopRefreshAdapter(),SheetBottomLoadMoreAdapter())
//            addItemDecoration(DividerItemDecoration(this@MainActivity,RecyclerView.VERTICAL))
        }
    }

    private fun getStatusBarHeight(context: Context): Int {
        var statusBarHeight = 0
        val resources = context.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        }
        return statusBarHeight
    }

    override fun onDestroy() {
        lifecycle.removeObserver(mViewModel)
        mDataBinding.unbind()
        super.onDestroy()
    }
}