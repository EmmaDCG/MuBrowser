package com.mu.game.model.ui.dm.imp.dungeon;

import com.mu.config.Global;
import com.mu.game.dungeon.DungeonTemplateFactory;
import com.mu.game.dungeon.imp.redfort.RedFort;
import com.mu.game.dungeon.imp.redfort.RedFortTemplate;
import com.mu.game.model.unit.player.Player;

public class RedFortMenu extends DungeonMenu {
   public RedFortMenu() {
      super(12, 5);
   }

   public boolean hasEffect(Player player, int showNumber) {
      RedFortTemplate template = (RedFortTemplate)DungeonTemplateFactory.getTemplate(5);
      RedFort redFort = template.getRedFortManager().getRedFort();
      return redFort != null && !redFort.isBegin();
   }

   public boolean isShow(Player player) {
      if (Global.isInterServiceServer()) {
         return false;
      } else {
         RedFortTemplate template = (RedFortTemplate)DungeonTemplateFactory.getTemplate(5);
         RedFort redFort = template.getRedFortManager().getRedFort();
         if (redFort != null && !redFort.isComplete()) {
            return template.getLevelLimit() <= player.getLevel();
         } else {
            return false;
         }
      }
   }
}
