package net.daxbau.injectr.inject

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.present
import com.nhaarman.mockitokotlin2.*
import com.schibsted.spain.barista.assertion.BaristaProgressBarAssertions.assertProgress
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertContains
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotContains
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaDialogInteractions.clickDialogNegativeButton
import com.schibsted.spain.barista.interaction.BaristaDialogInteractions.clickDialogPositiveButton
import com.schibsted.spain.barista.interaction.BaristaEditTextInteractions.writeTo
import com.schibsted.spain.barista.interaction.BaristaSeekBarInteractions.setProgressTo
import kotlinx.coroutines.delay
import net.daxbau.injectr.BaseFragmentTest
import net.daxbau.injectr.R
import net.daxbau.injectr.common.StubPhotoManager
import net.daxbau.injectr.data.InjectionInfo
import net.daxbau.injectr.runTest
import net.daxbau.injectr.shouldEq
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.mock.declare
import java.util.*


@RunWith(AndroidJUnit4::class)
class InjectFragmentTest : BaseFragmentTest() {
    override val fragmentId = R.id.inject

    private val vm = spy(StubInjectionListFragmentViewModel())
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
    fun takesPhoto() = runTest {
        launch()
        showBottomSheet()
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

    @Test
    fun asksForConfirmation() {
        launch()
        vm.confirmationRequiredProxy.postValue(true)
        assertContains("Save without photo?")
        vm.confirmationRequiredProxy.postValue(false)
        assertNotContains("Save without photo?")
    }

    @Test
    fun canConfirm() = runTest {
        launch()
        vm.confirmationRequiredProxy.postValue(true)
        assertContains("Save without photo?")
        clickDialogPositiveButton()
        verify(vm).confirmSave()
    }

    @Test
    fun canCancel() {
        launch()
        vm.confirmationRequiredProxy.postValue(true)
        assertContains("Save without photo?")
        reset(vm)
        clickDialogNegativeButton()
        verifyNoMoreInteractions(vm)
    }

    @Test
    fun switchesCamera() = runTest {
        launch()
        showBottomSheet()
        clickOn(R.id.switchCameraButton)
        verify(vm).switchCamera()
    }

    @Test
    fun togglesTorch() = runTest {
        launch()
        showBottomSheet()
        clickOn(R.id.toggleFlashButton)
        delay(100)
        verify(vm).toggleTorch()
    }

    private suspend fun showBottomSheet() {
        onView(withId(R.id.bottom_sheet_inject)).check(matches(allOf(isEnabled(), isClickable()))).perform(
            object : ViewAction {
                override fun getConstraints(): Matcher<View> {
                    return isEnabled() // no constraints, they are checked above
                }

                override fun getDescription(): String {
                    return "click plus button"
                }

                override fun perform(uiController: UiController, view: View) {
                    view.performClick()
                }
            }
        )
        delay(100)
    }

    private open class StubInjectionListFragmentViewModel : InjectViewModel() {
        override var depth: Int = 0
        override var date: Date? = null
        override var limb: Int = 0
        override var position: Int = 0
        override var comment: String = ""
        val confirmationRequiredProxy = MutableLiveData<Boolean>()
        override val confirmationRequired: LiveData<Boolean> = confirmationRequiredProxy

        override suspend fun save() { }
        override suspend fun confirmSave() { }
        override fun switchCamera() {}
        override fun toggleTorch() {}
        override fun loadInjectionInfo(injectionInfo: InjectionInfo) { }
    }
}