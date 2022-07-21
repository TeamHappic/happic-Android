package happy.kiki.happic.module.characterselect.provider

import happy.kiki.happic.module.characterselect.data.enumerate.CharacterType
import happy.kiki.happic.module.characterselect.data.enumerate.CharacterType.MOON
import happy.kiki.happic.module.characterselect.provider.CharacterSelectFlowProvider.Usage.SIGNUP
import happy.kiki.happic.module.characterselect.provider.CharacterSelectFlowProvider.Usage.UPDATE
import kotlinx.coroutines.flow.MutableStateFlow

object CharacterSelectFlowProvider {
    val character = MutableStateFlow(CharacterType.MOON)
    val name = MutableStateFlow("")
    private var _snsAccessToken: String = ""
    val snsAccessToken get() = _snsAccessToken
    var usage: Usage = SIGNUP

    fun initForSignUp(accessToken: String) {
        reset()
        this._snsAccessToken = accessToken
        usage = SIGNUP
    }

    fun initForUpdate() {
        reset()
        usage = UPDATE
    }

    fun reset() {
        character.value = MOON
        name.value = ""
    }

    enum class Usage {
        SIGNUP, UPDATE
    }
}