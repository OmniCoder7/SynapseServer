package com.proton.demo.coroutine

import org.springframework.beans.factory.DisposableBean
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextClosedEvent
import org.springframework.stereotype.Component

@Component
class ApplicationLifecycleManager(
    private val appScope: ApplicationCoroutineScope
) : ApplicationListener<ContextClosedEvent>, DisposableBean {

    override fun onApplicationEvent(event: ContextClosedEvent) {
        appScope.close()
    }

    override fun destroy() {
        appScope.close()
    }
}