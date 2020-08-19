/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.appconfig

import org.slf4j.LoggerFactory
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import java.lang.reflect.Method
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.abs

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Aug - 2020
 */
@Configuration
@EnableAsync
@EnableScheduling
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "app")
class AsyncConfig : AsyncConfigurer, AsyncUncaughtExceptionHandler {
    /**
     * Defaults to [DEFAULT_REPLY_DELETE_EXECUTOR_POOL_SIZE], but may adjusted by configuration for thread pool
     * utilisation rate of your application.
     *
     * Unlike deleting a post, deleting all replies does not require client to wait until job is complete,
     * since there could be enormous number of replies in a single post. A post owner and API frontend will
     * suffer a huge wait for such a job in this scenario, thus deleting a bulk of replies should be run under
     * separate threads to keep performance.
     */
    // @Value("\${app.delete-reply-executor-pool-size}")
    var deleteReplyExecutorPoolSize: Int = DEFAULT_REPLY_DELETE_EXECUTOR_POOL_SIZE

    @Bean(name = [EXECUTOR_NAME_DELETE_REPLIES])
    fun getPurchaseExecutor(): Executor {
        return Executors.newFixedThreadPool(
            deleteReplyExecutorPoolSize,
            NamedThreadFactory(EXECUTOR_NAME_DELETE_REPLIES)
        )
    }

    override fun handleUncaughtException(ex: Throwable, method: Method, vararg params: Any?) {
        LOG.error("Unhandled exception on async method: {}", method, ex)
    }

    /**
     * Produces a non-daemon, normal priority thread.
     */
    private class NamedThreadFactory(poolName: String) : ThreadFactory {
        private val group = System.getSecurityManager()?.threadGroup ?: Thread.currentThread().threadGroup
        private val threadNumber = AtomicInteger(1)
        private val namePrefix = "$poolName-thread-"

        override fun newThread(r: Runnable) =
            Thread(group, r, namePrefix + abs(threadNumber.getAndIncrement()), 0).apply {
                isDaemon = false
                priority = Thread.NORM_PRIORITY
            }
    }

    companion object {
        const val DEFAULT_REPLY_DELETE_EXECUTOR_POOL_SIZE = 10

        private const val EXECUTOR_NAME_DELETE_REPLIES = "deleteRepliesExecutor"

        private val LOG = LoggerFactory.getLogger(AsyncConfig::class.java)
    }
}
