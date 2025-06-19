package com.mu.game.model.unit.trigger.action.imp;

import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.monster.Monster;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.trigger.action.AbsKillMonsterAction;
import com.mu.io.game.packet.imp.player.PlayerAttributes;

public class KillMonsterRecoverAgAction extends AbsKillMonsterAction {
   private int value;

   public KillMonsterRecoverAgAction(int id, int value) {
      super(id);
      this.value = value;
   }

   public void killMonster(Monster monster) {
      int tmpValue = Math.min(this.getValue(), this.getOwner().getMaxAG() - this.getOwner().getAg());
      if (tmpValue > 0) {
         this.getOwner().setAg(this.getOwner().getAg() + tmpValue);
         if (this.getOwner().getType() == 1) {
            PlayerAttributes.sendToClient((Player)this.getOwner(), StatEnum.AG);
         }
      }

   }

   public boolean privyCondition() {
      return this.getOwner().getAg() < this.getOwner().getMaxAG();
   }

   public void setValue(int value) {
      this.value = value;
   }

   public int getValue() {
      return this.value;
   }
}
