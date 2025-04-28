package renard.remi.ping.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import renard.remi.ping.domain.repository.AuthRepository
import renard.remi.ping.domain.repository.DatastoreRepository
import renard.remi.ping.domain.repository.UserRepository
import renard.remi.ping.domain.use_case.CreateAccountUseCase
import renard.remi.ping.domain.use_case.GetAccessTokenUseCase
import renard.remi.ping.domain.use_case.GetDynamicsColorsUseCase
import renard.remi.ping.domain.use_case.GetIsInDarkModeUseCase
import renard.remi.ping.domain.use_case.GetMeUseCase
import renard.remi.ping.domain.use_case.GetPaletteColorsUseCase
import renard.remi.ping.domain.use_case.LoginUseCase
import renard.remi.ping.domain.use_case.LogoutUseCase
import renard.remi.ping.domain.use_case.ObserveAppSettingsUseCase
import renard.remi.ping.domain.use_case.UpdateAppColorsUseCase
import renard.remi.ping.domain.use_case.UpdateDarkModeUseCase
import renard.remi.ping.domain.use_case.UpdateFcmTokenUseCase
import renard.remi.ping.domain.use_case.UpdateLanguageUseCase
import renard.remi.ping.domain.use_case.UseDynamicsColorsUseCase
import renard.remi.ping.domain.use_case.ValidatePasswordUseCase
import renard.remi.ping.domain.use_case.ValidateUsernameUseCase
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideValidateUsernameUseCase() = ValidateUsernameUseCase()

    @Provides
    @Singleton
    fun provideValidatePasswordUseCase() = ValidatePasswordUseCase()

    @Provides
    @Singleton
    fun provideLoginUseCase(
        authRepository: AuthRepository,
        datastoreRepository: DatastoreRepository
    ): LoginUseCase {
        return LoginUseCase(
            authRepository = authRepository,
            datastoreRepository = datastoreRepository
        )
    }

    @Provides
    @Singleton
    fun provideLogoutUseCase(
        datastoreRepository: DatastoreRepository
    ): LogoutUseCase {
        return LogoutUseCase(
            datastoreRepository = datastoreRepository
        )
    }

    @Provides
    @Singleton
    fun provideCreateAccountUseCase(
        authRepository: AuthRepository,
        datastoreRepository: DatastoreRepository
    ): CreateAccountUseCase {
        return CreateAccountUseCase(
            authRepository = authRepository,
            datastoreRepository = datastoreRepository
        )
    }

    @Provides
    @Singleton
    fun provideGetMeUseCase(
        userRepository: UserRepository
    ): GetMeUseCase {
        return GetMeUseCase(
            userRepository = userRepository
        )
    }

    @Provides
    @Singleton
    fun provideUpdateFcmTokenUseCase(
        userRepository: UserRepository
    ): UpdateFcmTokenUseCase {
        return UpdateFcmTokenUseCase(
            userRepository = userRepository
        )
    }

    @Provides
    @Singleton
    fun provideGetDynamicsColorsUseCase(
        datastoreRepository: DatastoreRepository
    ): GetDynamicsColorsUseCase {
        return GetDynamicsColorsUseCase(
            datastoreRepository = datastoreRepository
        )
    }

    @Provides
    @Singleton
    fun provideGetIsInDarkModeUseCase(
        datastoreRepository: DatastoreRepository
    ): GetIsInDarkModeUseCase {
        return GetIsInDarkModeUseCase(
            datastoreRepository = datastoreRepository
        )
    }

    @Provides
    @Singleton
    fun provideGetPaletteColorUseCase(
        datastoreRepository: DatastoreRepository
    ): GetPaletteColorsUseCase {
        return GetPaletteColorsUseCase(
            datastoreRepository = datastoreRepository
        )
    }

    @Provides
    @Singleton
    fun provideUpdateAppColorsUseCase(
        datastoreRepository: DatastoreRepository
    ): UpdateAppColorsUseCase {
        return UpdateAppColorsUseCase(
            datastoreRepository = datastoreRepository
        )
    }

    @Provides
    @Singleton
    fun provideUpdateDarkModeUseCase(
        datastoreRepository: DatastoreRepository
    ): UpdateDarkModeUseCase {
        return UpdateDarkModeUseCase(
            datastoreRepository = datastoreRepository
        )
    }

    @Provides
    @Singleton
    fun provideUseDynamicsColorsUseCase(
        datastoreRepository: DatastoreRepository
    ): UseDynamicsColorsUseCase {
        return UseDynamicsColorsUseCase(
            datastoreRepository = datastoreRepository
        )
    }

    @Provides
    @Singleton
    fun provideObserveAppSettingsUseCase(
        datastoreRepository: DatastoreRepository
    ): ObserveAppSettingsUseCase {
        return ObserveAppSettingsUseCase(
            datastoreRepository = datastoreRepository
        )
    }

    @Provides
    @Singleton
    fun provideGetAccessTokenUseCase(
        datastoreRepository: DatastoreRepository
    ): GetAccessTokenUseCase {
        return GetAccessTokenUseCase(
            datastoreRepository = datastoreRepository
        )
    }

    @Provides
    @Singleton
    fun provideUpdateLanguageUseCase(
        datastoreRepository: DatastoreRepository
    ): UpdateLanguageUseCase {
        return UpdateLanguageUseCase(
            datastoreRepository = datastoreRepository
        )
    }
}