package happy.kiki.happic.module.home.data.api

import happy.kiki.happic.module.core.data.api.base.ApiResponse
import happy.kiki.happic.module.core.data.api.base.ApiServiceFactory.createService
import happy.kiki.happic.module.home.data.model.HomeDataModel
import happy.kiki.happic.module.home.data.model.RandomCapsuleModel
import retrofit2.http.GET

interface HomeService {
    @GET("home")
    suspend fun homeData(): ApiResponse<HomeDataModel>

    @GET("home/capsule")
    suspend fun randomCapsule(): ApiResponse<RandomCapsuleModel>
}

val homeService: HomeService = createService()