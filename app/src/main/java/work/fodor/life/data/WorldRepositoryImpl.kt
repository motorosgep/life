package work.fodor.life.data

import io.reactivex.Single
import work.fodor.life.domain.model.World
import work.fodor.life.domain.repository.WorldRepository
import javax.inject.Singleton


@Singleton
open class WorldRepositoryImpl : WorldRepository {

    private val dataSource by lazy {
        HashMap<String, World>().apply {
            put("Blinker", getBlinkerPreset())
            put("Toad", getToadPreset())
            put("Pulsar", getPulsarPreset())
            put("Diehard", getDiehardPreset())
        }
    }

    override fun getPresets(): Single<Array<String>> {
        return Single.just(dataSource.keys.toTypedArray())
    }

    override fun getPreset(preset: String): Single<World> {
        dataSource[preset]?.let {
            return Single.just(it)
        }

        return Single.never()
    }

    // Presets

    private fun getBlinkerPreset(): World {
        return World(5, 5).apply {
            cells[1][2] = true
            cells[2][2] = true
            cells[3][2] = true
        }
    }


    private fun getToadPreset(): World {
        return World(6, 6).apply {
            cells[2][2] = true
            cells[3][2] = true
            cells[4][2] = true
            cells[1][3] = true
            cells[2][3] = true
            cells[3][3] = true
        }
    }

    private fun getDiehardPreset(): World {
        return World(40, 40).apply {
            cells[16][14] = true
            cells[17][14] = true
            cells[17][15] = true
            cells[22][13] = true
            cells[21][15] = true
            cells[22][15] = true
            cells[23][15] = true
        }
    }

    private fun getPulsarPreset(): World {
        return World(20, 20).apply {
            cells[5][3] = true
            cells[6][3] = true
            cells[12][3] = true
            cells[13][3] = true
            cells[6][4] = true
            cells[7][4] = true
            cells[11][4] = true
            cells[12][4] = true
            cells[3][5] = true
            cells[6][5] = true
            cells[8][5] = true
            cells[10][5] = true
            cells[12][5] = true
            cells[15][5] = true
            cells[3][6] = true
            cells[4][6] = true
            cells[5][6] = true
            cells[7][6] = true
            cells[8][6] = true
            cells[10][6] = true
            cells[11][6] = true
            cells[13][6] = true
            cells[14][6] = true
            cells[15][6] = true
            cells[4][7] = true
            cells[6][7] = true
            cells[8][7] = true
            cells[10][7] = true
            cells[12][7] = true
            cells[14][7] = true
            cells[5][8] = true
            cells[6][8] = true
            cells[7][8] = true
            cells[11][8] = true
            cells[12][8] = true
            cells[13][8] = true
            cells[5][10] = true
            cells[6][10] = true
            cells[7][10] = true
            cells[11][10] = true
            cells[12][10] = true
            cells[13][10] = true
            cells[4][11] = true
            cells[6][11] = true
            cells[8][11] = true
            cells[10][11] = true
            cells[12][11] = true
            cells[14][11] = true
            cells[3][12] = true
            cells[4][12] = true
            cells[5][12] = true
            cells[7][12] = true
            cells[8][12] = true
            cells[10][12] = true
            cells[11][12] = true
            cells[13][12] = true
            cells[14][12] = true
            cells[15][12] = true
            cells[3][13] = true
            cells[6][13] = true
            cells[8][13] = true
            cells[10][13] = true
            cells[12][13] = true
            cells[15][13] = true
            cells[6][14] = true
            cells[7][14] = true
            cells[11][14] = true
            cells[12][14] = true
            cells[5][15] = true
            cells[6][15] = true
            cells[12][15] = true
            cells[13][15] = true
        }
    }
}