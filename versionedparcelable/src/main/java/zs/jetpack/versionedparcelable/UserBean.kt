package zs.jetpack.versionedparcelable

import androidx.versionedparcelable.NonParcelField
import androidx.versionedparcelable.VersionedParcelable
import androidx.versionedparcelable.VersionedParcelize

/**
 *
 * @author zhangshuai
 * @date 2023/10/11 19:05
 * @mark 自定义类描述
 */
@VersionedParcelize
class UserBean : VersionedParcelable {

    var name: String = ""

    @NonParcelField
    var age: Int = 0
}