package com.proton.demo.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.springframework.stereotype.Component
import java.io.Closeable
import kotlin.coroutines.CoroutineContext

@Component
class ApplicationCoroutineScope : CoroutineScope, Closeable {
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = job + Dispatchers.Default

    override fun close() {
        job.cancel()
    }
}