package net.daxbau.injectr

import androidx.navigation.NavController
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.koin.test.KoinTest

abstract class BaseFragmentTest: KoinTest {
    @Rule
    @JvmField
    val activityRule = object : ActivityTestRule<MainActivity>(MainActivity::class.java, false, false) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            installMocks()
        }
    }

    protected inline fun <reified T> findFragment(): T {
        val navHostFragment = activityRule.activity.supportFragmentManager.fragments.first()
        return navHostFragment.childFragmentManager.fragments.find { it is T } as T
    }

    protected abstract val fragmentId: Int

    private fun navigate(nav: NavController) {
        nav.navigate(fragmentId)
    }

    protected abstract fun installMocks()

    protected fun launch() {
        activityRule.launchActivity(null)
        navigate(activityRule.activity.nav)
    }

    protected fun getString(id: Int): String = activityRule.activity.getString(id)
    protected fun getString(id: Int, vararg args: Any): String =
        activityRule.activity.getString(id, *args)
}
