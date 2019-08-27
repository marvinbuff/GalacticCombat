@file:Suppress("ArrayInDataClass", "SpellCheckingInspection")

package entityJson

import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.stringify

@UnstableDefault @ImplicitReflectionSerializer
fun main() {
  val json = Json(configuration)

  // Entity to String
  val dataEntities = setupEvonik()

  println(json.stringify(dataEntities))
}

private val countryToCurrency = mapOf(
  "US" to "USD",
  "CN" to "CNY",
  "KR" to "KRW",
  "SG" to "SGD",
  "AU" to "AUD",
  "DE" to "EUR",
  "CA" to "CAD",
  "ID" to "IDR"
)

// In the real code, this would be EvonikCluster
private enum class ClusterTemplate(val label: String, val managed: Boolean = true) {
  CK("Care Solutions"),
  OU("Comfort & Insulation"),
  OI("Interface & Performance"),
  SU("Baby Care"),
  EA("Health Care"),
  FA("Animal Nutrition"),
  BM("New Business Dev."),
  AO("Active Oxygens"),
  KA("Catalyst"),
  AA("Coating & Adh. Resins"),
  AB("Coating Additives"),
  CX("Crosslinkers"),
  HO("High Perf. Polymers"),
  AL("Oil Additives"),
  I4("Silanes"),
  I2("Silica"),
  NB("New Growth Business"),
  C4("Performance Intermed."),
  EL("Functional Solutions"),
  AV("Methacrylates"),
  AW("Acrylic Products"),
  BI("CyPlus Technologies"),
  J0("Nutrition & Care"),
  J1("Resource Efficiency"),
  SM("Performance Matierials"),
  J2("Services"),
  OT("Others"),
  NOTASSIGNED("Not Assigned", false)
  ;

  private fun getCluster(segments: Array<SegmentJson>): ClusterJson =
    ClusterJson(if (this == NOTASSIGNED) "#" else name, label, managed, segments)

  companion object {
    val specialClusters = listOf(J0, J1, SM)

    fun getClustersWithout(filter: List<ClusterTemplate>, segments: Array<SegmentJson>) =
      values()
        .filter { !specialClusters.contains(it) }
        .filter { !filter.contains(it) }
        .map { it.getCluster(segments) }.toTypedArray()

    fun getClustersWith(clusters: List<ClusterTemplate>, segments: Array<SegmentJson>) =
      clusters.map { it.getCluster(segments) }.toTypedArray()
  }
}

@ImplicitReflectionSerializer
private fun setupEvonik(): EntitiesJson {

  // Segments
  val inScopeSegments = arrayOf(
    createSimpleSegment("SERVICES", "Services IG"),
    createSimpleSegment("SERVICES_3P", "Services 3P"),
    createSimpleSegment("IG_CYCLIC", "Int. Delivery"),
    createSimpleSegment("P_IG", "IG Production"),
    createSimpleSegment("T_IG", "IG Trading"),
    SegmentJson(
      "IG_T_3P", "IG_T_3P_INTEG", "3P Dist (Integrated)", "targetEbitRm",
      arrayOf("4142", "4632", "4638", "8786", "8849", "0028", "8740", "8918", "4063", "8812", "8825", "4075", "4073", "8511")
    ),
    createSimpleSegment("IG_T_3P", "3P Distribution"),
    createSimpleSegment("P_3P", "3P Production"),
    createSimpleSegment("THP_T_3P", "3P Trading"),
    createSimpleSegment("REST", "Remaining")
  )
  val outOfScopeSegments = arrayOf(
    SegmentJson(
      "P_IGT_COMP", "P_IGT_COMP", "IG Prod. & Trad.", "ask",
      arrayOf("4142", "4632", "4638", "8786", "8849")
    ),
    createSimpleSegment("REST", "Remaining")
  )

  // Clusters
  val clusterOilInScope = ClusterTemplate.getClustersWith(listOf(ClusterTemplate.AL, ClusterTemplate.OT), inScopeSegments)
  val clusterOilOutScope = ClusterTemplate.getClustersWith(listOf(ClusterTemplate.AL, ClusterTemplate.OT), outOfScopeSegments)

  // Entitites
  val entitiesInScope = listOf( //have all segments
    Triple(
      "4142", "US", ClusterTemplate.getClustersWithout(
        listOf(
          ClusterTemplate.BM
        ), inScopeSegments
      )
    ),
    Triple(
      "4632", "KR", ClusterTemplate.getClustersWithout(
        listOf(
          ClusterTemplate.BM, ClusterTemplate.SU, ClusterTemplate.BI
        ), inScopeSegments
      )
    ),
    Triple(
      "4638", "SG", ClusterTemplate.getClustersWithout(
        listOf(
          ClusterTemplate.BM, ClusterTemplate.SU, ClusterTemplate.BI, ClusterTemplate.C4
        ), inScopeSegments
      )
    ),
    Triple("8786", "SG", clusterOilInScope),
    Triple(
      "8849", "AU", ClusterTemplate.getClustersWith(
        listOf(
          ClusterTemplate.J0, ClusterTemplate.J1, ClusterTemplate.SM, ClusterTemplate.J2, ClusterTemplate.OT, ClusterTemplate.NOTASSIGNED
        ), inScopeSegments
      )
    )
  )

  val entitiesOutScope = listOf( //have only two segments
    Triple(
      "0028", "DE", ClusterTemplate.getClustersWithout(
        listOf(
          ClusterTemplate.EA, ClusterTemplate.BM, ClusterTemplate.AL, ClusterTemplate.AV, ClusterTemplate.AW, ClusterTemplate.BI
        ), outOfScopeSegments
      )
    ),
    Triple("4063", "DE", clusterOilOutScope),
    Triple("4073", "CA", clusterOilOutScope),
    Triple("4075", "US", clusterOilOutScope),
    Triple(
      "8511", "ID", ClusterTemplate.getClustersWith(
        listOf(
          ClusterTemplate.FA, ClusterTemplate.AO, ClusterTemplate.OT, ClusterTemplate.NOTASSIGNED
        ), outOfScopeSegments
      )
    ),
    Triple(
      "8740", "CN", ClusterTemplate.getClustersWith(
        listOf(
          ClusterTemplate.CK, ClusterTemplate.OU, ClusterTemplate.AA, ClusterTemplate.AB,
          ClusterTemplate.CX, ClusterTemplate.HO, ClusterTemplate.I2, ClusterTemplate.AV,
          ClusterTemplate.AW, ClusterTemplate.J2, ClusterTemplate.OT, ClusterTemplate.NOTASSIGNED
        ), outOfScopeSegments
      )
    ),
    Triple(
      "8812", "KR", ClusterTemplate.getClustersWith(
        listOf(
          ClusterTemplate.AO, ClusterTemplate.OT, ClusterTemplate.NOTASSIGNED
        ), outOfScopeSegments
      )
    ),
    Triple(
      "8825", "CN", ClusterTemplate.getClustersWith(
        listOf(
          ClusterTemplate.I4, ClusterTemplate.OT, ClusterTemplate.NOTASSIGNED
        ), outOfScopeSegments
      )
    ),
    Triple(
      "8918", "US", ClusterTemplate.getClustersWith(
        listOf(
          ClusterTemplate.CK, ClusterTemplate.OU, ClusterTemplate.OI, ClusterTemplate.AB,
          ClusterTemplate.CX, ClusterTemplate.OT, ClusterTemplate.NOTASSIGNED
        ), outOfScopeSegments
      )
    )
  )

  // Entities
  val entityJsons = (entitiesInScope + entitiesOutScope).map { entity ->
    EntityJson(
      name = entity.first,
      country = entity.second,
      lc = countryToCurrency.getValue(entity.second),
      clusters = entity.third
    )
  }.toTypedArray()

  return EntitiesJson(entityJsons)
}

// ------------------------- Settings -----------------------------

@UnstableDefault
private val configuration = JsonConfiguration(
  encodeDefaults = true,
  strictMode = true,
  unquoted = false,
  prettyPrint = true,
  indent = "  ",
  useArrayPolymorphism = false,
  classDiscriminator = "type"
)

// ---------------------------- Data Classes --------------------------

@Serializable
data class EntitiesJson(
  val entities: Array<EntityJson>
)

@Serializable
data class EntityJson(
  val name: String,
  val active: Boolean = true,
  var state: String = "integrated",
  val lc: String,
  var salesorg: Array<String> = emptyArray(),
  var customer: Array<String> = emptyArray(),
//  var sapRegion: String = "",
  var clusters: Array<ClusterJson> = emptyArray(),
//  var fiscmonthOffset: Int = 0,
  var country: String,
  var suppressCalcInbound: Boolean = false,
  var conditionDeclarations: Array<String> = emptyArray(),//Not really an array of strings
  var carveoutConfig: CarveOutConfigJson = CarveOutConfigJson(),
//  val allowPullSelfAccept: Boolean = true,
//  var entityType: String = ""
  val pnlGroups: Array<String> = emptyArray()// Not really an array: PnlGroupDeclaration
)

@Serializable
data class ClusterJson(
  val id: String,
  val label: String,
  val managed: Boolean = true,
//    val state: String,
//    val allowPullSelfAccept: Boolean,
//    val specialSegments: Array<String>,
//    var clusterType: String = "",
  val segments: Array<SegmentJson> = emptyArray()
)

@Serializable
data class SegmentJson(
  val type: String,
  val id: String,
  val label: String,
//  val tooltip: String = "",
  val strategy: String = "",
  val params: Array<String> = emptyArray()
//    val after: String,
//    val filters: Array<FilterDeclaration>
)

@Serializable
data class CarveOutConfigJson(
  val productionStrategy: String = "typeOfGood",
  val sourcingStrategy: String = "heuristic",
  val isProducer: Boolean = true
)

// ----------------------- Creation Utility ----------------------

private fun createSimpleSegment(type: String, label: String): SegmentJson = SegmentJson(type, type, label)