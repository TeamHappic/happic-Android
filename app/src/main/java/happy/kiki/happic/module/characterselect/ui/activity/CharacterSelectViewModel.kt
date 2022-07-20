package happy.kiki.happic.module.characterselect.ui.activity

import androidx.lifecycle.ViewModel
import happy.kiki.happic.module.characterselect.data.api.CharacterService.RegisterCharacterNameReq
import happy.kiki.happic.module.characterselect.data.api.characterService
import happy.kiki.happic.module.characterselect.data.enumerate.CharacterType
import happy.kiki.happic.module.core.data.api.base.useApi
import kotlinx.coroutines.flow.MutableStateFlow

class CharacterSelectViewModel : ViewModel() {
    val characterName = MutableStateFlow("")
    val characterType = MutableStateFlow(CharacterType.MOON)
    val characterQueryApi = useApi<RegisterCharacterNameReq> {
        characterService.registerCharacterName(it)
    }
<<<<<<< HEAD
=======

    init {
        characterQueryApi.call(RegisterCharacterNameReq(characterType.value, characterName.value))
    }
>>>>>>> create-character
}