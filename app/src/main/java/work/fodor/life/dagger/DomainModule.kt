package work.fodor.life.dagger

import dagger.Module
import dagger.Provides
import work.fodor.life.data.WorldRepositoryImpl
import work.fodor.life.domain.repository.WorldRepository
import work.fodor.life.domain.usecase.CreateWorldFromPreset
import work.fodor.life.domain.usecase.GetWorldEvolutionInfiniteSteps
import work.fodor.life.util.BaseSchedulerProvider
import work.fodor.life.util.SchedulerProvider
import javax.inject.Singleton


@Module
class DomainModule {

    @Provides
    @Singleton
    fun provideRepository(): WorldRepository {
        return WorldRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideSchedulerProvider(): BaseSchedulerProvider {
        return SchedulerProvider.instance
    }
}