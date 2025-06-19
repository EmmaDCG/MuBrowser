package com.mu.game.model.unit.skill.learnConsume;

import com.mu.game.model.unit.player.Player;
import java.util.List;

public class ItemLearnConsume implements LearnConsume {
   private int itemID;
   private int count;

   public ItemLearnConsume(int itemID, int count) {
      this.itemID = itemID;
      this.count = count;
   }

   public void consumed(Player player) {
      player.getItemManager().deleteAndAddModel(this.itemID, this.count, 100, (List)null);
   }
}
