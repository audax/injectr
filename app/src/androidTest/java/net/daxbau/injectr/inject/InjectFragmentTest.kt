package net.daxbau.injectr.inject

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.present
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import com.schibsted.spain.barista.assertion.BaristaProgressBarAssertions.assertProgress
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertContains
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaEditTextInteractions.writeTo
import com.schibsted.spain.barista.interaction.BaristaSeekBarInteractions.setProgressTo
import io.fotoapparat.result.PhotoResult
import net.daxbau.injectr.BaseFragmentTest
import net.daxbau.injectr.R
import net.daxbau.injectr.common.StubPhotoManager
import net.daxbau.injectr.runTest
import net.daxbau.injectr.shouldEq
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.mock.declare
import java.util.*

@RunWith(AndroidJUnit4::class)
class InjectFragmentTest : BaseFragmentTest() {
    override val fragmentId = R.id.inject

    private val vm = spy<StubInjectionListFragmentViewModel>()
    private val photoManager = spy<StubPhotoManager>()

    override fun installMocks() {
        declare {
            single<InjectViewModel>(override = true) { vm }
            single<PhotoManager>(override = true) { photoManager }
        }
    }

    @Test
    fun testLaunch() {
        launch()
        assertThat(activityRule.activity.nav, present())
        verify(vm, atLeastOnce()).setNavController(activityRule.activity.nav)
    }

    @Test
    fun setDepth() {
        launch()
        setProgressTo(R.id.depthSeekBar, 6)
        vm.depth shouldEq 6
        assertContains(R.id.injection_slider_label, "6mm")
    }

    @Test
    fun getsDepthFromVM() {
        vm.depth = 8
        launch()
        assertProgress(R.id.depthSeekBar, 8)
        assertContains(R.id.injection_slider_label, "8mm")
    }

    @Test
    fun saves() = runTest {
        launch()
        clickOn(R.id.saveInjectionButton)
        verify(vm).save()
    }

    @Test
    fun takesPhoto() {
        launch()
        clickOn(R.id.takePhotoButton)
        verify(photoManager).takePhoto()
    }

    @Test
    fun setsComment() {
        launch()
        writeTo(R.id.injection_comment, "comment")
        vm.comment shouldEq "comment"
    }

    @Test
    fun getsCommentFromVM() {
        vm.comment = "comment"
        launch()
        assertContains(R.id.injection_comment, "comment")
    }

    private open class StubInjectionListFragmentViewModel : InjectViewModel() {
        override var depth: Int = 0
        override var date: Date? = null
        override var limb: Int = 0
        override var position: Int = 0
        override var comment: String = ""
        override var photo: PhotoResult? = null

        override suspend fun save() { }
    }
}