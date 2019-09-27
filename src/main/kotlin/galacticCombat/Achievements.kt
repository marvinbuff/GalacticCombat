package galacticCombat

import com.almasb.fxgl.achievement.Achievement
import galacticCombat.configs.AchievementConfig


val achievements = listOf(
  //todo not sure if we want the achievements here like that, especially not sure if we nit a AchievementConfig, if the strings are anyway magic as well.
    Achievement("Money Money Money", "Gather 100 gold", "gold", AchievementConfig.GOLD_ACHIEVEMENT_1)
)