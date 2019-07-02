package net.daxbau.injectr.common

import android.util.Log

interface JustLog {

    fun getLogger(): JustLoggerWrapper = JustLoggerManager.factory.createLogger(javaClass)

    fun trace(message: Any, vararg args: Any) = getLogger().trace(message, args)

    fun debug(message: Any, vararg args: Any) = getLogger().debug(message, args)

    fun info(message: Any, vararg args: Any) = getLogger().info(message, args)

    fun warn(message: Any, vararg args: Any) = getLogger().warn(message, args)

    fun error(message: Any?, throwable: Throwable? = null) = getLogger().error(message, throwable)
}

interface JustLoggerWrapper {

    fun trace(msg: Any, vararg args: Any)
    fun debug(msg: Any, vararg args: Any)
    fun info(msg: Any, vararg args: Any)
    fun warn(msg: Any, vararg args: Any)

    fun error(msg: Any?, exception: Throwable?)

}

interface JustLoggerFactory {
    fun <T> createLogger(clazz: Class<T>): JustLoggerWrapper
}

class AndroidLoggerWrapper(clazz: Class<*>) : JustLoggerWrapper {

    private val tag = clazz.simpleName

    override fun trace(msg: Any, vararg args: Any) {
        Log.v(tag, format(msg, args))
    }

    override fun info(msg: Any, vararg args: Any) {
        Log.i(tag, format(msg, args))
    }

    override fun debug(msg: Any, vararg args: Any) {
        Log.d(tag, format(msg, args))
    }

    override fun warn(msg: Any, vararg args: Any) {
        Log.v(tag, format(msg, args))
    }

    override fun error(msg: Any?, exception: Throwable?) {
        Log.e(tag, msg?.toString() ?: "No error msg", exception)
    }

    private fun format(msg: Any?, args: Array<out Any>) = msg.toString().let {
        if (args.isNotEmpty()) {
                return@let it.format(args)
        }
        it
    }
}

class PrintLogger(clazz: Class<*>): JustLoggerWrapper {

    private val tag = clazz.simpleName

    private fun f(msg: Any, vararg args: Any) = "$tag ${msg.toString().format(*args)}"

    override fun trace(msg: Any, vararg args: Any) {
        println("TRACE: " + f(msg, *args))
    }

    override fun debug(msg: Any, vararg args: Any) {
        println("DEBUG: " + f(msg, *args))
    }

    override fun info(msg: Any, vararg args: Any) {
        println("INFO: " + f(msg, *args))
    }

    override fun warn(msg: Any, vararg args: Any) {
        println("WARN: " + f(msg, *args))
    }

    override fun error(msg: Any?, exception: Throwable?) {
        println("ERROR: ${msg.toString()}\n ${exception?.message ?: ""}")
    }
}

object JustLoggerManager {
    @Suppress("TooGenericExceptionCaught")
    var factory: JustLoggerFactory = try {
        Log.d("JustLoggerManager", "Starting logging system")
        object : JustLoggerFactory {
            override fun <T> createLogger(clazz: Class<T>): JustLoggerWrapper =
                    AndroidLoggerWrapper(clazz)
        }
    } catch (e: RuntimeException) {
        object : JustLoggerFactory {
            override fun <T> createLogger(clazz: Class<T>): JustLoggerWrapper =
                    PrintLogger(clazz)
        }
    }

}

