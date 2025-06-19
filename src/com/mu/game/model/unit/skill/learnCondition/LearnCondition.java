package com.mu.game.model.unit.skill.learnCondition;

import com.mu.game.model.unit.player.Player;

public interface LearnCondition {
   int Type_Money = 1;
   int Type_Item = 2;
   int Type_FrontSkill = 3;
   int Type_UserLevel = 4;
   int Type_Proficiency = 5;

   int verify(Player var1);

   int getType();
}
