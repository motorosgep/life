package work.fodor.life.dagger

import dagger.Component
import work.fodor.life.ui.GameActivity
import javax.inject.Singleton


@Singleton
@Component(modules = [GameModule::class, DomainModule::class])
interface GameComponent {
    fun inject(activity: GameActivity)
}