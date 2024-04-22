package zs.jetpack.biometric

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.databinding.DataBindingUtil
import zs.jetpack.biometric.databinding.ActivityMainBinding

/**
 * 通过生物识别特征或设备凭据进行身份验证，以及执行加密操作。
 *
 * 可参考文档：https://blog.51cto.com/u_16099303/7438638
 */
class MainActivity : AppCompatActivity() {

    private lateinit var mDataBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mDataBinding.acBtnAuth.setOnClickListener {
            if (isSupport()) {
                startAuth()
            } else {
                Toast.makeText(this, "未开启生物识别功能", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startAuth() {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("指纹认证")
            .setSubtitle("请进行指纹认证")
            .setDescription("请将您的指纹放在指纹传感器上")
            .setNegativeButtonText("取消")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_WEAK)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            BiometricPrompt(this, mainExecutor, mAuthenticationCallback).authenticate(promptInfo)
        } else {
            BiometricPrompt(this, mAuthenticationCallback).authenticate(promptInfo)
        }
    }

    /**
     * 检测设备支持生物识别功能
     */
    private fun isSupport(): Boolean {
        return BiometricManager.from(this)
            .canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK) == BiometricManager.BIOMETRIC_SUCCESS
    }

    private val mAuthenticationCallback = object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            // 处理认证错误事件
            val msg = when (errorCode) {
                BiometricPrompt.ERROR_HW_UNAVAILABLE -> {
                    "硬件不支持"
                }

                BiometricPrompt.ERROR_UNABLE_TO_PROCESS -> {
                    "传感器无法处理当前图像"
                }

                BiometricPrompt.ERROR_TIMEOUT -> {
                    "认证超时"
                }

                BiometricPrompt.ERROR_NO_SPACE -> {
                    "操作无法完成，因为剩余的设备存储空间不足。"
                }

                BiometricPrompt.ERROR_CANCELED -> {
                    "由于生物识别传感器不可用，操作被取消。当用户被切换、设备被锁定或其他挂起的操作阻止时，可能会发生这种情况。"
                }

                BiometricPrompt.ERROR_LOCKOUT -> {
                    "由于尝试过多，API被锁定，操作被取消。这发生在5次尝试失败后，并持续30秒。"
                }

                BiometricPrompt.ERROR_VENDOR -> {
                    "由于供应商特定的错误，操作失败。\n" +
                            "硬件供应商可以使用此错误代码来扩展此列表，以涵盖不属于其他预定义类别的错误。供应商负责提供这些错误的字符串。\n" +
                            "这些消息通常是为内部操作（如注册）保留的，但也可用于表示未涵盖的任何错误。在这种情况下，应用程序应该显示错误消息，但建议他们不要依赖消息ID，因为这可能因供应商和设备而异。"
                }

                BiometricPrompt.ERROR_LOCKOUT_PERMANENT -> {
                    "由于ERROR_LOCKOUT发生次数过多，操作被取消。在用户使用其设备凭据（即PIN、模式或密码）解锁之前，生物识别身份验证将被禁用。"
                }

                BiometricPrompt.ERROR_USER_CANCELED -> {
                    "用户取消了操作。收到此消息后，应用程序应使用备用身份验证，例如密码。该应用程序还应为用户提供一种返回生物特征验证的方式，例如按钮。"
                }

                BiometricPrompt.ERROR_NO_BIOMETRICS -> {
                    "用户没有注册任何生物特征。"
                }

                BiometricPrompt.ERROR_HW_NOT_PRESENT -> {
                    "设备没有所需的身份验证硬件。"
                }

                BiometricPrompt.ERROR_NEGATIVE_BUTTON -> {
                    "用户按了取消按钮"
                }

                BiometricPrompt.ERROR_NO_DEVICE_CREDENTIAL -> {
                    "设备没有设置pin、模式或密码。"
                }

                else -> {
                    "未知错误！"
                }
            }
            Toast.makeText(this@MainActivity, "认证状态：$errorCode, $errString", Toast.LENGTH_SHORT)
                .show()

            Log.i("print_logs", "onAuthenticationError: $msg")
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            Toast.makeText(this@MainActivity, "认证成功", Toast.LENGTH_SHORT).show()
            // 处理认证成功事件
            // 可以从 result 对象中获取生物识别数据

            val typeHint = when (result.authenticationType) {
                BiometricPrompt.AUTHENTICATION_RESULT_TYPE_UNKNOWN -> {
                    "当用户通过未知方法进行身份验证时，BiometricPrompt.AuthenticationResult报告的身份验证类型。\n" +
                            "由于与较新的API部分不兼容，此值可能会在较旧的Android版本上返回。这并不一定意味着用户使用除AUTHENTICATION_RESULT_TYPE_DEVICE_REDENTAL和AUTHENTIMATION_RESULT_TYPE_BIOMETIC表示的方法之外的方法进行身份验证。"
                }

                BiometricPrompt.AUTHENTICATION_RESULT_TYPE_DEVICE_CREDENTIAL -> {
                    "身份验证类型：设备PIN、模式或密码进行身份验证。"
                }

                BiometricPrompt.AUTHENTICATION_RESULT_TYPE_BIOMETRIC -> {
                    "身份验证类型：生物特征（例如指纹或面部）进行身份验证。"
                }

                else -> {
                    ""
                }
            }


            Log.i(
                "print_logs", "onSucceeded: \n " +
                        "authenticationType= $typeHint，\n" +
                        "cipher= ${result.cryptoObject}, \n" +
                        "mac= ${result.cryptoObject?.mac}, \n" +
                        "signature= ${result.cryptoObject?.signature}"
            )
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            // 处理认证失败事件
            Toast.makeText(this@MainActivity, "认证失败!", Toast.LENGTH_SHORT).show()
        }
    }
}