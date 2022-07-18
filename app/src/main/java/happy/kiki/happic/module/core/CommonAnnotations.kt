package happy.kiki.happic.module.core

import kotlin.annotation.AnnotationRetention.SOURCE
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.PROPERTY
import kotlin.annotation.AnnotationTarget.VALUE_PARAMETER

@Target(FUNCTION, PROPERTY, VALUE_PARAMETER)
@Retention(SOURCE)
annotation class Dp