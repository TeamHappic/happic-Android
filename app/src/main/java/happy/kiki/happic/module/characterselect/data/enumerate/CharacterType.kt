package happy.kiki.happic.module.characterselect.data.enumerate

import kotlinx.serialization.Serializable

@Serializable
enum class CharacterType(val characterId: Int) {
    MOON(0), CLOUD(1)
}