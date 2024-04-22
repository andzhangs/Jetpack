package zs.jetpack.loader

/**
 *
 * @author zhangshuai
 * @date 2023/10/13 11:34
 * @mark 自定义类描述
 */
data class UserBean(val name: String){
    override fun toString(): String {
        return "name= $name"
    }
}
