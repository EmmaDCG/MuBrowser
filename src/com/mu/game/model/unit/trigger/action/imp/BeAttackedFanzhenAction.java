package com.mu.game.model.unit.trigger.action.imp;

import com.mu.config.Constant;
import com.mu.game.model.item.action.imp.AddHp;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;
import com.mu.game.model.unit.trigger.action.AbsBeAttackedAction;

public class BeAttackedFanzhenAction extends AbsBeAttackedAction {
   private int value = 10000;

   public BeAttackedFanzhenAction(int id, int value) {
      super(id);
      this.value = value;
   }

   public void beAttacked(Creature attacker, AttackResult result) {
      if (!this.getOwner().isDestroy() && !this.getOwner().isDie()) {
         if (!attacker.isDestroy() && !attacker.isDie()) {
            switch(result.getType()) {
            case 1:
            case 3:
            case 4:
            case 9:
               int damage = Constant.getPercentValue(result.getActualDamage(), this.value);
               if (damage < 1) {
                  return;
               }

               AttackResult ar = new AttackResult(7, damage, -2, -1, this.getOwner());
               AddHp.reduceHp(attacker, ar, this.getOwner());
               return;
            case 2:
            case 5:
            case 6:
            case 7:
            case 8:
            default:
            }
         }
      }
   }

   public boolean privyCondition() {
      return !this.getOwner().isDie();
   }
}
