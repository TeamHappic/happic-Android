package happy.kiki.happic.module.characterselect.data.api

import happy.kiki.happic.module.characterselect.data.enumerate.CharacterType
import happy.kiki.happic.module.core.data.api.base.ApiServiceFactory.createService
import happy.kiki.happic.module.core.data.api.base.NoDataApiResponse
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.PATCH

interface CharacterService {
    @Serializable
    data class UpdateCharacterReq(
        val characterId: CharacterType,
        val characterName: String,
    )

    @PATCH("setting")
    suspend fun updateCharacter(@Body req: UpdateCharacterReq): NoDataApiResponse
}

val characterService: CharacterService = createService()