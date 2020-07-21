/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.component.advice

import com.fasterxml.jackson.core.JsonProcessingException
import com.github.fj.board.AppProfile
import com.github.fj.board.Application
import com.github.fj.board.endpoint.AbstractResponseDto
import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.ErrorResponseDto
import com.github.fj.board.exception.GeneralHttpException
import com.github.fj.board.exception.client.IllegalRequestException
import org.slf4j.LoggerFactory
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.stereotype.Controller
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException
import javax.servlet.http.HttpServletRequest
import javax.validation.ValidationException

/**
 * This only works if following settings are applied:
 * ```
 * spring:
 *   mvc:
 *     throw-exception-if-no-handler-found: true
 *   resources:
 *     add-mappings: false
 * ```
 * Read [this post](https://stackoverflow.com/questions/28902374/spring-boot-rest-service-exception-handling/30193013)
 * for more information.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jan - 2018
 */
@Controller
@RestControllerAdvice
class CustomErrorHandler : ErrorController {
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleNoController(req: HttpServletRequest, ex: Exception): ResponseEntity<ErrorResponseDto> {
        return handleError(
            req, GeneralHttpException.createPlain(
                HttpStatus.NOT_FOUND,
                "${req.method} ${req.requestURI} is unsupported or not found on server.",
                ex
            )
        )
    }

    @ExceptionHandler(ValidationException::class)
    fun handleValidationFailure(req: HttpServletRequest, ex: ValidationException): ResponseEntity<ErrorResponseDto> {
        val message = ex.message

        return handleError(
            req, if (message.isNullOrEmpty()) {
                IllegalRequestException(cause = ex)
            } else {
                IllegalRequestException(message, ex)
            }
        )
    }

    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleSpring404(req: HttpServletRequest): ResponseEntity<ErrorResponseDto> {
        return handleError(
            req, GeneralHttpException.create(HttpStatus.NOT_FOUND, req.requestURI ?: "")
        )
    }

    // Somebody please give us a redemption from this sin. No idea how to improve this.
    @Suppress("LongMethod", "ReturnCount")
    @ExceptionHandler(Exception::class)
    fun handleError(req: HttpServletRequest, ex: Exception): ResponseEntity<ErrorResponseDto> {
        val status: HttpStatus
        val response = when (ex) {
            is GeneralHttpException -> {
                logError(req, "Handled exception:", ex)
                status = ex.httpStatus
                AbstractResponseDto.error(ex.message, ex::class.simpleName ?: "")
            }
            is HttpMediaTypeNotSupportedException -> {
                if (ex.cause is Exception) {
                    return handleError(req, ex.cause as Exception)
                }

                logError(req, "Spring handled exception:", ex)
                status = HttpStatus.BAD_REQUEST
                AbstractResponseDto.error(
                    "Cannot process given request.",
                    "HttpMediaTypeNotSupportedException"
                )
            }
            is HttpMessageNotReadableException -> {
                if (ex.cause is Exception) {
                    return handleError(req, ex.cause as Exception)
                }

                logError(req, "Spring handled exception:", ex)
                status = HttpStatus.BAD_REQUEST
                AbstractResponseDto.error(
                    "Cannot process given request.",
                    "HttpMessageNotReadableException"
                )
            }
            is JsonProcessingException -> {
                if (ex.cause is Exception) {
                    return handleError(req, ex.cause as Exception)
                }

                logError(req, "JSON parsing exception:", ex)
                status = HttpStatus.BAD_REQUEST
                AbstractResponseDto.error(
                    "Cannot process given request.",
                    "JsonProcessingException"
                )
            }
            is MethodArgumentNotValidException -> {
                LOG.error("{} {}: Illegal request from client. Constraint violations are:", req.method, req.requestURI)
                val msgs = ex.bindingResult.allErrors.map {
                    LOG.error("  {}", it.defaultMessage)
                    return@map it.defaultMessage
                }
                val msg = msgs.joinToString()
                return handleError(
                    req,
                    if (msg.isEmpty()) {
                        IllegalRequestException(cause = ex)
                    } else {
                        IllegalRequestException(msg, ex)
                    }
                )
            }
            else -> {
                if (ex.cause is Exception) {
                    return handleError(req, ex.cause as Exception)
                } else {
                    // is Exception is wrapped?
                    logError(req, "Unhandled exception:", ex)
                    status = getStatus(req)
                    AbstractResponseDto.error("Unhandled internal server error")
                }
            }
        }

        return ResponseEntity(response, status)
    }

    /**
     * Any errors, that happen in the outside of Spring Context - exceptions in Servlet filters
     * for example, are redirected to this method to decorate error output as our own favour,
     * rather than Spring's [org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController].
     */
    @RequestMapping(ApiPaths.ERROR)
    fun handleError(request: HttpServletRequest): ResponseEntity<ErrorResponseDto> {
        (request.getAttribute("javax.servlet.error.exception") as? Exception)?.let {
            return handleError(request, it)
        }

        return handleError(request, GeneralHttpException.create(getStatus(request)))
    }

    override fun getErrorPath(): String {
        return ApiPaths.ERROR
    }

    private fun getStatus(request: HttpServletRequest): HttpStatus {
        (request.getAttribute("javax.servlet.error.status_code") as? Int)?.let {
            return HttpStatus.valueOf(it)
        }

        return HttpStatus.SERVICE_UNAVAILABLE
    }

    private fun logError(req: HttpServletRequest, message: String, ex: Exception) {
        if (Application.profile == AppProfile.RELEASE) {
            // Minimise log outputs in RELEASE binary
            LOG.error("{} {}: {}", req.method, req.requestURI, message)
            logCauses(req, ex)
        } else {
            LOG.error("{} {}: {}", req.method, req.requestURI, message, ex)
        }
    }

    private fun logCauses(req: HttpServletRequest, cause: Throwable?) {
        if (cause == null) {
            return
        } else {
            val superCause = cause.cause
            if (superCause == null) {
                LOG.error("""{} {}: {} ("{}")""", req.method, req.requestURI, cause::class, cause.message)
            } else {
                LOG.error("by {}", cause::class)
                logCauses(req, superCause)
            }
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(CustomErrorHandler::class.java)
    }
}
