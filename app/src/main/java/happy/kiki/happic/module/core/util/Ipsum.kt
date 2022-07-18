package happy.kiki.happic.module.core.util

import com.thedeanda.lorem.LoremIpsum

object Ipsum {
    private val instance = LoremIpsum.getInstance()

    fun text(max: Int = 5) = instance.getTitle(1, max).take(5)
}