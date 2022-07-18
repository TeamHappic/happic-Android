package happy.kiki.happic.module.core.util

import java.time.LocalDateTime

val now get() = LocalDateTime.now()

fun yearMonthText(year: Int, month: Int) = "${year.toString().padStart(4, '0')}.${month.toString().padStart(2, '0')}"