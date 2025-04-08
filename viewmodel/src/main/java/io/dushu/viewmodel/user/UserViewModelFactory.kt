package io.dushu.viewmodel.user

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.savedstate.SavedStateRegistryOwner
import io.dushu.viewmodel.ChildViewModel

/**
 *
 * @author zhangshuai
 * @date 2025/4/8 10:17
 * @description 自定义类描述
 */
class UserViewModelFactory(
    owner: SavedStateRegistryOwner,
    private val bundle: Bundle?,
    private val mUserRepository: UserRepository
) :AbstractSavedStateViewModelFactory(owner,bundle){
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return ChildViewModel(
            handle,
            userRepository = mUserRepository
        ) as T
    }
}