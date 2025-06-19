package com.mu.game.model.unit.skill.learnCondition;

import com.mu.game.model.unit.player.Player;

public class UserLevelCondition implements LearnCondition {
   private int level;

   public UserLevelCondition(int level) {
      this.level = level;
   }

   public int verify(Player player) {
      return player.getLevel() < this.level ? 8005 : 1;
   }

   public int getType() {
      return 4;
   }
}
