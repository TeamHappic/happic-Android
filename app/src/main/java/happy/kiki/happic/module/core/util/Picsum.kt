package happy.kiki.happic.module.core.util

import kotlin.random.Random

object Picsum {
    fun uri(width: Int, height: Int? = null) =
        "https://picsum.photos/${width}/${height ?: width}?random=${Random.nextInt()}"
}