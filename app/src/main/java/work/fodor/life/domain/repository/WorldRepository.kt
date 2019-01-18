package work.fodor.life.domain.repository

import io.reactivex.Single
import work.fodor.life.domain.model.World


interface WorldRepository {

    fun getPresets(): Single<Array<String>>
    fun getPreset(preset: String): Single<World>

}