package com.mu.game.model.unit.unitevent.imp;

import com.mu.game.model.fo.FunctionOpenManager;
import com.mu.game.model.item.action.imp.AddHp;
import com.mu.game.model.item.action.imp.AddMp;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.unitevent.Event;
import com.mu.game.model.unit.unitevent.OperationEnum;
import com.mu.game.model.unit.unitevent.Status;

public class RecoveryEvent extends Event {
   public RecoveryEvent(Player owner) {
      super(owner);
      this.lastCheckTime = System.currentTimeMillis();
      this.checkrate = 5000;
   }

   public void work(long now) throws Exception {
      if (!((Player)this.getOwner()).isDie() && !((Player)this.getOwner()).isDestroy()) {
         if (((Player)this.getOwner()).getHp() < ((Player)this.getOwner()).getMaxHp()) {
            this.addHp();
         }

         if (((Player)this.getOwner()).getMp() < ((Player)this.getOwner()).getMaxMp()) {
            this.addMp();
         }

         if (((Player)this.getOwner()).getSd() < ((Player)this.getOwner()).getMaxSD()) {
            this.addSd();
         }

         if (((Player)this.getOwner()).getAg() < ((Player)this.getOwner()).getMaxAG()) {
            this.addAG();
         }

      }
   }

   private void addHp() {
      int hpRecover = ((Player)this.getOwner()).getStatValue(StatEnum.HP_RECOVER);
      if (hpRecover > 0 && ((Player)this.getOwner()).getHp() < ((Player)this.getOwner()).getMaxHp()) {
         hpRecover = (int)(1.0F * (float)hpRecover / 100000.0F * (float)((Player)this.getOwner()).getMaxHp());
         hpRecover = Math.min(hpRecover, ((Player)this.getOwner()).getMaxHp() - ((Player)this.getOwner()).getHp());
         if (hpRecover > 0) {
            AddHp.addHPAndSend((Creature)this.getOwner(), hpRecover);
         }
      }

   }

   private void addMp() {
      int mpRecover = ((Player)this.getOwner()).getStatValue(StatEnum.MP_RECOVER);
      if (mpRecover > 0 && ((Player)this.getOwner()).getMp() < ((Player)this.getOwner()).getMaxMp()) {
         mpRecover = (int)(1.0F * (float)mpRecover / 100000.0F * (float)((Player)this.getOwner()).getMaxMp());
         mpRecover = Math.min(mpRecover, ((Player)this.getOwner()).getMaxMp() - ((Player)this.getOwner()).getMp());
         if (mpRecover > 0) {
            AddMp.addMPAndSend((Creature)this.getOwner(), mpRecover);
         }
      }

   }

   private void addSd() {
      if (FunctionOpenManager.isOpen((Player)this.getOwner(), 19)) {
         int sdRecover = ((Player)this.getOwner()).getStatValue(StatEnum.SD_RECOVER);
         if (sdRecover > 0) {
            sdRecover = Math.min(sdRecover, ((Player)this.getOwner()).getMaxSD() - ((Player)this.getOwner()).getSd());
            if (sdRecover > 0) {
               ((Player)this.getOwner()).addSd(sdRecover);
            }
         }

      }
   }

   private void addAG() {
      int agRecover = ((Player)this.getOwner()).getStatValue(StatEnum.AG_RECOVER);
      agRecover = Math.min(agRecover, ((Player)this.getOwner()).getMaxAG() - ((Player)this.getOwner()).getAg());
      if (agRecover > 0) {
         ((Player)this.getOwner()).addAg(agRecover);
      }

   }

   public Status getStatus() {
      return Status.Recovery;
   }

   public OperationEnum getOperationEnum() {
      return OperationEnum.NONE;
   }
}
