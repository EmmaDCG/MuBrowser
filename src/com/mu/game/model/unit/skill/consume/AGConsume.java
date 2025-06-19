package com.mu.game.model.unit.skill.consume;

import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.skill.Skill;
import com.mu.io.game.packet.imp.player.PlayerAttributes;

public class AGConsume implements Consume {
   private int value;

   public AGConsume(int value) {
      this.value = value;
   }

   public void consumed(Skill skill) {
      if (skill.getOwner().getType() == 1) {
         int tmpValue = skill.getAgConsume(this.value);
         Player player = (Player)skill.getOwner();
         player.setAg(player.getAg() - tmpValue);
         PlayerAttributes.sendToClient(player, StatEnum.AG);
      }
   }

   public int getValue() {
      return this.value;
   }

   public void setValue(int value) {
      this.value = value;
   }
}
