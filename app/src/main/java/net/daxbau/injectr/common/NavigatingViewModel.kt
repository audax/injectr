package net.daxbau.injectr.common

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController

abstract class NavigatingViewModel : ViewModel(), JustLog {

    protected var nav: NavController? = null

    fun setNavController(nav: NavController) {
        info("Setting NavController")
        this.nav = nav
    }

    fun onDestroy() {
        info("Removing reference to NavController")
        this.nav = null
    }
}