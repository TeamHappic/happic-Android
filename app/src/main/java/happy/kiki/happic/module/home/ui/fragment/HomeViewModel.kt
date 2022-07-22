package happy.kiki.happic.module.home.ui.fragment

import androidx.lifecycle.ViewModel
import happy.kiki.happic.module.core.data.api.base.useApiNoParams
import happy.kiki.happic.module.home.data.api.homeMockService

class HomeViewModel : ViewModel() {
    val homeApi = useApiNoParams {
        homeMockService.homeData()
    }
}