package com.apk.downloadinstall.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs

class DetailFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.findViewById<AppCompatButton>(R.id.acBtnDetail)?.setOnClickListener {
            Navigation.findNavController(it).apply {
                navigate(R.id.action_detailFragment_to_homeFragment)
            }
        }
    }

    //别忘了添加插件
    // 第一步：classpath 'androidx.navigation:navigation-safe-args-gradle-plugin:2.3.5'
    // 第二步：plugin { id("androidx.navigation.safeargs.kotlin") }
    val argsHome: HomeFragmentArgs by navArgs()
    val argsDetail:DetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //接收值方式一
//        val fromBundle = HomeFragmentArgs.fromBundle(requireArguments())
//        Toast.makeText(activity, "获取：${fromBundle.userName}, ${fromBundle.userAge}", Toast.LENGTH_SHORT).show()
        //安全取值
//        Toast.makeText(activity, "获取：${argsHome.userName}, ${argsHome.userAge}", Toast.LENGTH_SHORT).show()

        //来自deeplink
        if (argsHome.deeplinkParams != "unknown") {
            Toast.makeText(activity, "DeepLink之UrlPendingIntent：${argsHome.deeplinkParams}}", Toast.LENGTH_SHORT)
                .show()
        }

        //Url
        Toast.makeText(activity, "DeepLink之Url: ${arguments?.getString("params")}", Toast.LENGTH_SHORT).show()
    }
}