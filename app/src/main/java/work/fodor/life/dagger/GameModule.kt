package work.fodor.life.dagger

import dagger.Module
import dagger.Provides
import work.fodor.life.ui.GameContract


@Module
class GameModule(private val view: GameContract.View) {

    @Provides
    fun provideView() = view
}