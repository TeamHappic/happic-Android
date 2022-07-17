package happy.kiki.happic.module.core.util

object Picsum {
    fun uri(width: Int, height: Int? = null) = "https://picsum.photos/${width}/${height ?: width}"
}