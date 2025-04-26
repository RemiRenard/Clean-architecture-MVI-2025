package renard.remi.ping.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import renard.remi.ping.domain.use_case.GetAccessTokenUseCase
import renard.remi.ping.domain.use_case.ObserveAppSettingsUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val observeAppSettingsUseCase: ObserveAppSettingsUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<MainState> = MutableStateFlow(
        MainState(
            isConnected = runBlocking {
                getAccessTokenUseCase.execute()?.isNotBlank() == true
            }
        )
    )

    val state: StateFlow<MainState>
        get() = _state.asStateFlow()

    init {
        viewModelScope.launch {
            observeAppSettingsUseCase.execute().collect {
                _state.update { state ->
                    state.copy(isConnected = it.accessToken?.isNotBlank() == true)
                }
            }
        }
    }
}
