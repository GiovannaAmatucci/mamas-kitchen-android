package com.giovanna.amatucci.foodbook.di.util

import timber.log.Timber

interface LogWriter {
    fun d(tag: String, message: String)
    fun w(tag: String, message: String, t: Throwable? = null)
    fun e(tag: String, message: String, t: Throwable? = null)
}

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