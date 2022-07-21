package happy.kiki.happic.module.characterselect.ui.activity

import androidx.lifecycle.ViewModel
import happy.kiki.happic.module.auth.data.api.AuthService.SignInReq
import happy.kiki.happic.module.auth.data.api.AuthService.SignInRes
import happy.kiki.happic.module.auth.data.api.AuthService.SignUpReq
import happy.kiki.happic.module.auth.data.api.authService
import happy.kiki.happic.module.characterselect.data.api.CharacterService.UpdateCharacterReq
import happy.kiki.happic.module.characterselect.data.api.characterService
import happy.kiki.happic.module.core.data.api.base.useApi

class CharacterNameViewModel : ViewModel() {
    val signUpAndSignInApi = useApi<SignUpReq, SignInRes> {
        authService.signUp(it)
        authService.signIn(SignInReq(it.accessToken))
    }
    val updateCharacter = useApi<UpdateCharacterReq> {
        characterService.updateCharacter(it)
    }
}