package net.daxbau.injectr

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.present
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import com.schibsted.spain.barista.interaction.BaristaMenuClickInteractions.clickMenu
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.mock.declareModule

@RunWith(AndroidJUnit4::class)
class MainActivityTest : BaseFragmentTest() {
    override val fragmentId = R.id.injectionList

    private val vm = spy(MainActivityViewModelStub())

    override fun installMocks() {
        declareModule {
            single<MainActivityViewModel>(override = true) { vm }
        }
    }

    @Test
    fun testLaunch() {
        launch()
        assertThat(activityRule.activity.nav, present())
    }

    @Test
    fun testClickPurge() = runTest {
        launch()
        clickMenu(R.id.action_purge)
        verify(vm).purgeOldPhotos()
    }

    private open class MainActivityViewModelStub : MainActivityViewModel() {
        override suspend fun purgeOldPhotos() {}
    }
}