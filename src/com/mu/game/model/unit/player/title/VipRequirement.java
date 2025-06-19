package com.mu.game.model.unit.player.title;

import com.mu.game.model.unit.player.Player;

public class VipRequirement implements TitleRequirement {
   private int minVipLevel;
   private int maxVipLevel;

   public VipRequirement(int min, int max) {
      this.minVipLevel = min;
      this.maxVipLevel = max;
   }

   public boolean math(Player player) {
      int vipLevel = player.getVipShowLevel();
      return vipLevel >= this.minVipLevel && vipLevel <= this.maxVipLevel;
   }
}
