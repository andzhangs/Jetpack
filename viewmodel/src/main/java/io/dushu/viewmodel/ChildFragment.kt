package io.dushu.viewmodel

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import io.dushu.viewmodel.user.UserCreationExtras
import io.dushu.viewmodel.user.UserRepository
import io.dushu.viewmodel.user.UserViewModelFactory

class ChildFragment : Fragment() {

    private val mMain2ViewModel: Main2ViewModel by activityViewModels()

    private val mMainViewModel by createViewModelLazy(
        viewModelClass = MainViewModel::class,
        storeProducer = {viewModelStore},
        factoryProducer = { ViewModelProvider.AndroidViewModelFactory() }
    )

    private lateinit var mChildViewModel: ChildViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_child, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle= savedInstanceState ?: bundleOf(ChildViewModel.KEY_DATA to "哈哈哈哈")

        mChildViewModel = ViewModelProvider(
            this,
            UserViewModelFactory(
                this,
                bundle,
                UserRepository())
        ).get(ChildViewModel::class.java)


        val acTvInfo = view.findViewById<AppCompatTextView>(R.id.acTv_text)
        mMain2ViewModel.getLivedata.observe(viewLifecycleOwner) {
            acTvInfo.text = it

            mMainViewModel.showToast("I‘m from ChildFragment.")
        }

        savedInstanceState?.also {
            if (BuildConfig.DEBUG) {
                Log.d("print_logs", "ChildFragment::onViewCreated: ${it.getString(ChildViewModel.KEY_INFO)}")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "ChildFragment::onSaveInstanceState: ChildFragment")
        }
        outState.putString(ChildViewModel.KEY_INFO, "ChildFragment")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (BuildConfig.DEBUG) {
            Log.d("print_logs", "ChildFragment::onConfigurationChanged: ${newConfig.orientation}")
        }
    }
}