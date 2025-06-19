package com.mu.game.model.unit.skill.condition;

import com.mu.game.model.unit.skill.Skill;

public interface Condition {
   int Type_HP = 1;
   int Type_MP = 2;
   int Type_AG = 3;
   int Type_Scene = 4;
   int Type_Status = 5;
   int Type_Weapon = 6;
   int Type_Siege = 7;

   int verify(Skill var1);

   int getType();

   int getConsumeValue();
}
