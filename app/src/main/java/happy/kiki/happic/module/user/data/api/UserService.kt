package happy.kiki.happic.module.user.data.api

import happy.kiki.happic.module.core.data.api.base.ApiServiceFactory.createService
import happy.kiki.happic.module.core.data.api.base.NoDataApiResponse
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.PUT

interface UserService {
    @Serializable
    data class UpdateCharacterReq(
        val characterId: Int,
        val characterName: String,
    )

    @PUT("setting")
    suspend fun updateCharacter(@Body req: UpdateCharacterReq): NoDataApiResponse
}

val userService: UserService = createService()