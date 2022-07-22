package happy.kiki.happic.module.home.ui.fragment

import androidx.lifecycle.ViewModel
import happy.kiki.happic.module.core.data.api.base.useApiNoParams
import happy.kiki.happic.module.core.util.EventFlow
import happy.kiki.happic.module.home.data.api.homeService
import happy.kiki.happic.module.home.data.model.RandomCapsuleModel

class HomeViewModel : ViewModel() {
    val homeApi = useApiNoParams {
        homeService.homeData()
    }

    val onRandomCapsuleScreenOpened = EventFlow<RandomCapsuleModel>()
    val randomCapsuleApi = useApiNoParams(onSuccess = {
        onRandomCapsuleScreenOpened.emit(it)
    }) {
        homeService.randomCapsule()
    }
}