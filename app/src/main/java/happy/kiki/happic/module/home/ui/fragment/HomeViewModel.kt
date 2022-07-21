package happy.kiki.happic.module.home.ui.fragment

import androidx.lifecycle.ViewModel
import happy.kiki.happic.module.characterselect.data.enumerate.CharacterType
import happy.kiki.happic.module.core.data.api.base.useApiNoParams
import happy.kiki.happic.module.home.data.api.homeService
import kotlinx.coroutines.flow.MutableStateFlow

class HomeViewModel : ViewModel() {

    val characterType = MutableStateFlow(CharacterType.MOON)
    val characterName = MutableStateFlow("")
    val growthRate = MutableStateFlow("1")
    val level = MutableStateFlow("1")
    val isPosted = MutableStateFlow(true)

    val homeApi = useApiNoParams {
        homeService.homeData()
    }

    init {
        homeApi.call()
    }
}