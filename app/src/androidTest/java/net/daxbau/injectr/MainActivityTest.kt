package net.daxbau.injectr

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.adevinta.android.barista.interaction.BaristaMenuClickInteractions.clickMenu
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.present
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.mock.declare

@RunWith(AndroidJUnit4::class)
class MainActivityTest : BaseFragmentTest() {
    override val fragmentId = R.id.injectionList

    private val vm = spy(MainActivityViewModelStub())

    override fun installMocks() {
        declare<MainActivityViewModel> { vm }
    }

    @Test
    fun testLaunch() {
        launchActivity()
        assertThat(activityRule.activity.nav, present())
    }

    @Test
    fun testClickPurge() = runTest {
        launchActivity()
        clickMenu(R.id.action_purge)
        verify(vm).purgeOldPhotos()
    }

    private open class MainActivityViewModelStub : MainActivityViewModel() {
        override suspend fun purgeOldPhotos() {}
    }
}