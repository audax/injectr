package net.daxbau.injectr.inject

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.present
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import com.schibsted.spain.barista.assertion.BaristaProgressBarAssertions.assertProgress
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaSeekBarInteractions.setProgressTo
import io.fotoapparat.result.PhotoResult
import net.daxbau.injectr.BaseFragmentTest
import net.daxbau.injectr.R
import net.daxbau.injectr.shouldEq
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.mock.declare
import java.util.*

@RunWith(AndroidJUnit4::class)
class InjectFragmentTest : BaseFragmentTest() {
    override val fragmentId = R.id.inject

    private val vm = spy<StubInjectionListFragmentViewModel>()

    override fun installMocks() {
        declare {
            single<InjectViewModel>(override = true) { vm }
        }
    }

    @Test
    fun testLaunch() {
        launch()
        assertThat(activityRule.activity.nav, present())
        verify(vm, atLeastOnce()).setNavController(activityRule.activity.nav)
        activityRule.finishActivity()
        verify(vm, atLeastOnce()).onDestroy()
    }

    @Test
    fun setDepth() {
        launch()
        setProgressTo(R.id.depthSeekBar, 6)
        vm.depth shouldEq 6
    }

    @Test
    fun getsDepthFromVM() {
        vm.depth = 8
        launch()
        assertProgress(R.id.depthSeekBar, 8)
    }

    @Test
    fun saves() {
        launch()
        clickOn(R.id.saveInjectionButton)
        verify(vm).save()
    }

    private open class StubInjectionListFragmentViewModel : InjectViewModel() {
        override var depth: Int = 0
        override var date: Date? = null
        override var photo: PhotoResult? = null

        override fun save() { }
    }
}