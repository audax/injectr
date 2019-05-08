package net.daxbau.injectr.data

import net.daxbau.injectr.shouldEq
import org.junit.Test
import java.util.*

class ModelsTest {

    @Test
    fun testInjectionPosition() {
        val injectionInfo = InjectionInfo(0, Date(), 1, 7, 0, "comment")
        injectionInfo.position() shouldEq "7A"
    }
}