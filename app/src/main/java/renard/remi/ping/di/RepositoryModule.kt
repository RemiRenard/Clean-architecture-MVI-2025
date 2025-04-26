package renard.remi.ping.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import renard.remi.ping.data.repository.AuthRepositoryImpl
import renard.remi.ping.data.repository.DatastoreRepositoryImpl
import renard.remi.ping.data.repository.UserRepositoryImpl
import renard.remi.ping.domain.repository.AuthRepository
import renard.remi.ping.domain.repository.DatastoreRepository
import renard.remi.ping.domain.repository.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(authRepository: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepository: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindDatastoreRepository(datastoreRepository: DatastoreRepositoryImpl): DatastoreRepository
}