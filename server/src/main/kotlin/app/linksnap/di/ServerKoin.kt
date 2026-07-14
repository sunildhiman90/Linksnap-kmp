package app.linksnap.di

import app.linksnap.features.link.data.LinkDbRepository
import app.linksnap.features.link.data.LinkDbRepositoryImpl
import app.linksnap.features.auth.data.UserDbRepository
import app.linksnap.features.auth.data.UserDbRepositoryImpl
import app.linksnap.features.link.domain.LinkService
import app.linksnap.features.link.domain.LinkServiceImpl
import app.linksnap.features.auth.domain.UserService
import app.linksnap.features.auth.domain.UserServiceImpl
import org.koin.dsl.module

val serverModule = module {
    single<UserDbRepository> { UserDbRepositoryImpl() }
    single<LinkDbRepository> { LinkDbRepositoryImpl() }
    single<UserService> { UserServiceImpl(get()) }
    single<LinkService> { LinkServiceImpl(get()) }
}