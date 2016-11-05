package space.limerainne.i_bainil_u.view.main

/**
 * Created by Limerainne on 2016-11-05.
 */
interface UpdatingToolbar {
    fun updateTitle(callback: (title: String, subtitle: String) -> Unit)
}