package net.daxbau.injectr

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


fun <T> LiveData<T>.block(): T? {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            removeObserver(this)
        }
    }
    observeForever(observer)
    latch.await(2, TimeUnit.SECONDS)

    return data
}

infix fun <T> T.shouldEq(expected: T) {
    assertThat(this, equalTo(expected))
}

private val ctx = newSingleThreadContext("Test")
fun <T> runTest(timeout: Long = 5, block: suspend CoroutineScope.() -> T) {
    runBlocking(ctx) {
        withTimeout(timeout * 1000) {
            block()
        }
    }
}
