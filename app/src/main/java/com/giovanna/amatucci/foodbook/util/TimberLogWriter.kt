package com.giovanna.amatucci.foodbook.util

import org.koin.core.annotation.Single
import timber.log.Timber

interface LogWriter {
    fun d(tag: String, message: String)
    fun w(tag: String, message: String, t: Throwable? = null)
    fun e(tag: String, message: String, t: Throwable? = null)
}

@Single(binds = [LogWriter::class])
class TimberLogWriter : LogWriter {
    override fun d(tag: String, message: String) {
        Timber.tag(tag).d(message)
    }

    override fun w(tag: String, message: String, t: Throwable?) {
        Timber.tag(tag).w(t, message)
    }

    override fun e(tag: String, message: String, t: Throwable?) {
        Timber.tag(tag).e(t, message)
    }
}