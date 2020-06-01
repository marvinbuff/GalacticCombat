package galacticCombat.entities.path

import com.almasb.fxgl.dsl.components.DraggableComponent
import com.almasb.fxgl.dsl.entityBuilder
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import com.almasb.fxgl.entity.components.CollidableComponent
import galacticCombat.configs.LevelDataVar
import galacticCombat.configs.UIConfig
import galacticCombat.entities.EntityType
import galacticCombat.entities.PATH_EDGE_SPAWN_ID
import galacticCombat.entities.PATH_VERTEX_SPAWN_ID
import galacticCombat.entities.path.PathEdgeArgs.Companion.ID_PATH_EDGE_ARGS
import galacticCombat.entities.path.PathVertexArgs.Companion.ID_PATH_VERTEX_ARGS
import galacticCombat.entities.setTypeAdvanced
import galacticCombat.utils.conditionalWith
import galacticCombat.utils.toPoint
import javafx.geometry.Point2D
import javafx.scene.shape.Circle

@Suppress("unused")
class PathFactory : EntityFactory {

  @Spawns(PATH_VERTEX_SPAWN_ID)
  fun spawnPathVertex(data: SpawnData): Entity {
    require(data.hasKey(ID_PATH_VERTEX_ARGS))

    val pathVertexArgs = data.get<PathVertexArgs>(ID_PATH_VERTEX_ARGS)

    return entityBuilder(data).setTypeAdvanced(EntityType.PATH)
      .conditionalWith(DraggableComponent(), LevelDataVar.get().isEditable)
      .conditionalWith(PathVertexComponent(pathVertexArgs), LevelDataVar.get().isEditable) //todo check if we should not just pass the args object
      .viewWithBBox(Circle(halfPathWidth, UIConfig.PATH_COLOR))
//      .with(CollidableComponent(true)) //fixme view and bbox seem not to be in sync
      .build()
  }

  @Spawns(PATH_EDGE_SPAWN_ID)
  fun spawnPathEdge(data: SpawnData): Entity {
    require(data.hasKey(ID_PATH_EDGE_ARGS))

    val (startVertex, endVertex) = data.get<PathEdgeArgs>(ID_PATH_EDGE_ARGS)

    return entityBuilder().setTypeAdvanced(EntityType.PATH)
      .with(PathEdgeComponent(startVertex, endVertex)) //todo check if we should not just pass the args object
      .with(CollidableComponent(true))
      .build()
  }

  companion object {
    const val pathWidth = 30.0
    const val halfPathWidth = pathWidth / 2

    fun createPath(): List<Entity> {
      val paths = LevelDataVar.get().paths
      return paths.map { path ->
        val entities: MutableList<Entity> = mutableListOf()
        var previousEdge: Entity?
        var nextEdge: Entity? = null
        for (i in 0 until path.size) {
          previousEdge = nextEdge
          val current = path[i].toPoint()
          val next = if (i + 1 != path.size) path[i + 1].toPoint() else null

          // Create Edge
          nextEdge = if (next != null) {
            getGameWorld().create(PATH_EDGE_SPAWN_ID, SpawnData(current).put(ID_PATH_EDGE_ARGS, PathEdgeArgs(current, next))).apply { entities.add(this) }
          } else { // at last vertex
            null
          }

          // Create Vertex
          getGameWorld().create(PATH_VERTEX_SPAWN_ID, SpawnData(current).put(ID_PATH_VERTEX_ARGS, PathVertexArgs(path.id, i, previousEdge, nextEdge))).apply { entities.add(this) }
        }
        entities
      }.flatten()
    }

    /**
     * Removes all path entities and then restores them from level data.
     */
    fun reload() {
      val world = getGameWorld()
      world.removeEntities(world.getEntitiesByType(EntityType.PATH))
      world.addEntities(*createPath().toTypedArray())
    }
  }
}

private data class PathEdgeArgs(val startVertex: Point2D, val endVertex: Point2D) {
  companion object {
    const val ID_PATH_EDGE_ARGS = "Path Edge Args"
  }
}

data class PathVertexArgs(val pathID: String, val wayPointIndex: Int, val previousEdge: Entity?, val nextEdge: Entity?) {
  companion object {
    const val ID_PATH_VERTEX_ARGS = "Path Vertex Args"
  }
}