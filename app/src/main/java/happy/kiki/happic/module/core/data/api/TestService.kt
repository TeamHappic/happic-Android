package happy.kiki.happic.module.core.data.api

import happy.kiki.happic.module.core.data.api.base.ApiResponse
import happy.kiki.happic.module.core.data.api.base.ApiServiceFactory.createService
import happy.kiki.happic.module.core.data.api.base.NoDataApiResponse
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import retrofit2.http.GET

interface TestService {
    @GET("query")
    suspend fun query(): ApiResponse<List<Int>>

    @GET("query_no_data")
    suspend fun queryNoData(): NoDataApiResponse

    @GET("query_enum")
    suspend fun queryEnum(): ApiResponse<TestEnum>

    @GET("query_enum_str")
    suspend fun queryEnumStr(): ApiResponse<TestEnumStr>
}

@Serializable
enum class TestEnumStr(val id: String) {
    @SerialName("1")
    ONE("1"),

    @SerialName("2")
    TWO("2"),

    @SerialName("3")
    THREE("3")
}

@Serializable(with = TestEnumSerializer::class)
enum class TestEnum(val id: Int) {
    ONE(1), TWO(2), THREE(3)
}

object TestEnumSerializer : KSerializer<TestEnum> {
    override val descriptor = serialDescriptor<Int>()

    override fun deserialize(decoder: Decoder) =
        enumValues<TestEnum>().find { decoder.decodeInt() == it.id } ?: TestEnum.ONE

    override fun serialize(encoder: Encoder, value: TestEnum) = encoder.encodeInt(value.id)
}

val testService: TestService = createService()