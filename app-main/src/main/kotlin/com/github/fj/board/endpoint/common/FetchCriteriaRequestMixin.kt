/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.common

import com.github.fj.board.vo.ContentsFetchCriteria
import com.github.fj.board.vo.SortDirection
import org.springframework.util.MultiValueMap
import kotlin.math.min

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Aug - 2020
 */
interface FetchCriteriaRequestMixin {
    /*
     * Original function signature:
     *
     * ```
     * fun <SORT> MultiValueMap<String, String>.toFetchCriteria(
     *     sortByProvider: (sortBy: String?) -> SORT,
     *     defaultSortDirection: SortDirection,
     *     defaultPage: Int,
     *     defaultFetchSizeRange: IntRange
     * ): ContentsFetchCriteria<SORT>
     * ```
     *
     * It seems that something weird is happening if:
     *
     *   1. declare an Kotlin extension function that receiver type is using Java/Kotlin generics -
     *      (MultiValueMap<String, String> is in this case),
     *   2. let this method is exposed to `org.springframework.validation.annotation.Validated` annotated class
     *      which is to be a proxy target,
     *   3. declare a spring-data dependency with Hibernate
     *
     * exception occurs like below, although there are no direct dependencies to spring-data classes, in proxied class.
     *
     * ```
     * java.lang.ArrayIndexOutOfBoundsException: Index 0 out of bounds for length 0
     * at org.hibernate.validator.internal.metadata.provider.AnnotationMetaDataProvider.getTypeParametersCascadingMetaDataForParameterizedType(AnnotationMetaDataProvider.java:678)
     * at org.hibernate.validator.internal.metadata.provider.AnnotationMetaDataProvider.getTypeParametersCascadingMetadata(AnnotationMetaDataProvider.java:660)
     * at org.hibernate.validator.internal.metadata.provider.AnnotationMetaDataProvider.findCascadingMetaData(AnnotationMetaDataProvider.java:627)
     * at org.hibernate.validator.internal.metadata.provider.AnnotationMetaDataProvider.getParameterMetaData(AnnotationMetaDataProvider.java:432)
     * at org.hibernate.validator.internal.metadata.provider.AnnotationMetaDataProvider.findExecutableMetaData(AnnotationMetaDataProvider.java:308)
     * at org.hibernate.validator.internal.metadata.provider.AnnotationMetaDataProvider.getMetaData(AnnotationMetaDataProvider.java:292)
     * at org.hibernate.validator.internal.metadata.provider.AnnotationMetaDataProvider.getMethodMetaData(AnnotationMetaDataProvider.java:279)
     * at org.hibernate.validator.internal.metadata.provider.AnnotationMetaDataProvider.retrieveBeanConfiguration(AnnotationMetaDataProvider.java:130)
     * at org.hibernate.validator.internal.metadata.provider.AnnotationMetaDataProvider.getBeanConfiguration(AnnotationMetaDataProvider.java:120)
     * at org.hibernate.validator.internal.metadata.BeanMetaDataManagerImpl.getBeanConfigurationForHierarchy(BeanMetaDataManagerImpl.java:234)
     * at org.hibernate.validator.internal.metadata.BeanMetaDataManagerImpl.createBeanMetaData(BeanMetaDataManagerImpl.java:201)
     * at org.hibernate.validator.internal.metadata.BeanMetaDataManagerImpl.getBeanMetaData(BeanMetaDataManagerImpl.java:165)
     * at org.hibernate.validator.internal.engine.ValidatorImpl.validateParameters(ValidatorImpl.java:267)
     * at org.hibernate.validator.internal.engine.ValidatorImpl.validateParameters(ValidatorImpl.java:235)
     * at org.springframework.validation.beanvalidation.MethodValidationInterceptor.invoke(MethodValidationInterceptor.java:104)
     * at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)
     * at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:749)
     * at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:691)
     * at com.github.fj.board.endpoint.v1.reply.GetRepliesControllerImpl$$EnhancerBySpringCGLIB$$8e36496f.getLatest(<generated>)
     * ```
     *
     * with following configuration:
     *   org.springframework.boot:spring-boot-starter:2.3.1.RELEASE
     *   org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.72
     *   javax.validation:validation-api:2.0.1.Final
     *   jakarta.servlet:jakarta.servlet-api:4.0.3
     */
    fun <SORT> extractFetchCriteria(
        requestParams: MultiValueMap<String, String>,
        sortByProvider: (sortBy: String?) -> SORT,
        defaultSortDirection: SortDirection,
        defaultPage: Int,
        defaultFetchSizeRange: IntRange
    ): ContentsFetchCriteria<SORT> = with(requestParams) {
        val sortDirection = (getFirst(GET_LIST_PARAM_ORDER_BY)?.let {
            SortDirection.fromString(it)
        } ?: defaultSortDirection).direction
        val page = getFirst(GET_LIST_PARAM_PAGE)?.toIntOrNull()?.takeIf {
            it >= 0
        } ?: defaultPage
        val count = getFirst(GET_LIST_PARAM_COUNT)?.toIntOrNull()?.takeIf {
            it >= defaultFetchSizeRange.first
        } ?: defaultFetchSizeRange.first
        val fetchSize = min(count, defaultFetchSizeRange.last)

        return ContentsFetchCriteria.create(
            sortBy = sortByProvider.invoke(getFirst(GET_LIST_PARAM_SORT_BY)),
            sortDirection = sortDirection,
            page = page,
            fetchSize = fetchSize
        )
    }

    companion object {
        const val GET_LIST_PARAM_SORT_BY = "sortBy"
        const val GET_LIST_PARAM_ORDER_BY = "orderBy"
        const val GET_LIST_PARAM_PAGE = "page"
        const val GET_LIST_PARAM_COUNT = "count"
    }
}
