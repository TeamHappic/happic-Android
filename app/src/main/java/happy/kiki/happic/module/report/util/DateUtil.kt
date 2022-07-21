package happy.kiki.happic.module.report.util

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.*

/**
 * YYYY-MM-DD
 */
typealias DateString = String
/**
 * YYYY-MM-DDTHH
 */
typealias DateHourString = String
/**
 * YYYY-MM-DD or YYYY-MM-DDTHH
 */
typealias DateOrDateHourString = String

fun String.padZero(length: Int = 2) = padStart(length, '0')
fun Int.padZero() = toString().padZero()

/**
 * 1 -> 월
 * 7 -> 일
 */
val DayOfWeek.koFormat: String
    get() = when (value) {
        1 -> "월"
        2 -> "화"
        3 -> "수"
        4 -> "목"
        5 -> "금"
        6 -> "토"
        else -> "일"
    }

/**
 * 1 -> 오전 1시
 * 13 -> 오후 1시
 * 24 -> 오후 12시
 */
val Int.koFormat: String
    get() = if (this < 13) {
        "오전 ${this}시"
    } else {
        "오후 ${this - 12}시"
    }

/**
 * 1997-04
 */
val LocalDate.yearMonthFormat: String
    get() = "${year}-${monthValue.padZero()}"

/**
 * 1997-03
 */
val LocalDate.lastYearMonthFormat: String
    get() = "${year}-${(monthValue - 1).padZero()}"

/**
 * 0 ~ 6
 */
val LocalDate.weekOfMonth: Int
    get() {
        val weekFields: WeekFields = WeekFields.of(Locale.KOREA)
        return get(weekFields.weekOfMonth())
    }

/**
 * 1(SUN) -> 7(SAT)
 */
val LocalDate.dayOfWeekValue: Int
    get() = when (dayOfWeek.value + 1) {
        8 -> 1
        else -> dayOfWeek.value + 1
    }

/** 0(SUN) -> 6(SAT) */
val LocalDate.dayOfWeekIndex: Int
    get() = dayOfWeekValue - 1

fun calculateRequiredRow(date: LocalDate): Int {
    return (date.lengthOfMonth() + date.withDayOfMonth(1).dayOfWeekIndex - 1) / 7 + 1
}