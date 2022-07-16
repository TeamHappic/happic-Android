package happy.kiki.happic.module.characterselect.data.api

import happy.kiki.happic.module.characterselect.data.enumerate.CharacterType
import happy.kiki.happic.module.core.data.api.base.ApiServiceFactory.createService
import happy.kiki.happic.module.core.data.api.base.NoDataApiResponse
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.POST

interface CharacterService {
    @Serializable
    data class RegisterCharacterNameReq(
        val characterId: CharacterType,
        val characterName: String,
    )

    @POST("character")
    suspend fun registerCharacterName(@Body req: RegisterCharacterNameReq): NoDataApiResponse
}

val characterService: CharacterService = createService()