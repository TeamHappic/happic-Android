package happy.kiki.happic.module.home.data.api

import happy.kiki.happic.BuildConfig
import happy.kiki.happic.module.characterselect.data.enumerate.CharacterType.CLOUD
import happy.kiki.happic.module.core.data.api.base.ApiResponse
import happy.kiki.happic.module.core.data.api.base.ApiServiceFactory.createService
import happy.kiki.happic.module.core.data.api.base.successApiResponse
import happy.kiki.happic.module.core.util.Picsum
import happy.kiki.happic.module.home.data.model.HomeDataModel
import happy.kiki.happic.module.home.data.model.RandomCapsuleModel
import kotlinx.coroutines.delay
import retrofit2.http.GET

interface HomeService {
    @GET("home")
    suspend fun homeData(): ApiResponse<HomeDataModel>

    @GET("home/capsule")
    suspend fun randomCapsule(): ApiResponse<RandomCapsuleModel>
}

val homeService: HomeService = createService()

val homeMockService: HomeService = if (!BuildConfig.DEBUG) homeService else object : HomeService {
    override suspend fun homeData(): ApiResponse<HomeDataModel> {
        delay(1500)
        return successApiResponse(
            HomeDataModel(
                CLOUD, "김다희", 5, 2, false
            )
        )
    }

    override suspend fun randomCapsule(): ApiResponse<RandomCapsuleModel> {
        return successApiResponse(
            RandomCapsuleModel(
                Picsum.uri(200), 18, "where", "who", "what"
            )
        )
    }
}