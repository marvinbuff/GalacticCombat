package galacticCombat.level.json

import galacticCombat.configs.GameConfig
import kotlinx.serialization.Serializable

@Serializable
data class Path(
    val id: String,
    private val wayPoints: MutableList<Pair<Int, Int>>
) : MutableList<Pair<Int, Int>> by wayPoints {


    //region -------------- Overwrite list elements to accord for world vs global coordinates -----------
    override fun add(element: Pair<Int, Int>): Boolean {
        val elementInWorldCoordinates = element.first - GameConfig.WORLD_POSITION.x.toInt() to element.second - GameConfig.WORLD_POSITION.y.toInt()
        return wayPoints.add(elementInWorldCoordinates)
    }

    override fun add(index: Int, element: Pair<Int, Int>) {
        val elementInWorldCoordinates = element.first - GameConfig.WORLD_POSITION.x.toInt() to element.second - GameConfig.WORLD_POSITION.y.toInt()
        wayPoints.add(index, elementInWorldCoordinates)
    }

    override fun get(index: Int): Pair<Int, Int> {
        val elementInWorldCoordinates = wayPoints[index]
        return elementInWorldCoordinates.first + GameConfig.WORLD_POSITION.x.toInt() to elementInWorldCoordinates.second + GameConfig.WORLD_POSITION.y.toInt()
    }

    override fun set(index: Int, element: Pair<Int, Int>): Pair<Int, Int> {
        val elementInWorldCoordinates = element.first - GameConfig.WORLD_POSITION.x.toInt() to element.second - GameConfig.WORLD_POSITION.y.toInt()
        return wayPoints.set(index, elementInWorldCoordinates)
    }
    //endregion
}
