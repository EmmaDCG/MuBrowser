package com.mu.game.model.unit.buff.imp.role;

import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.buff.imp.BuffCommon;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.pkMode.EvilEnum;
import com.mu.game.model.unit.service.EvilManager;
import com.mu.io.game.packet.imp.buff.BuffUpdateDydata;
import com.mu.io.game.packet.imp.pkModel.ChangeEvilColor;

public class BuffPkColor extends BuffCommon {
   private int showEvil = 0;

   public BuffPkColor(int modelID, int level, Player owner, Player caster) {
      super(modelID, level, owner, caster);
      this.showEvil = EvilManager.getShowEvil(owner.getEvil());
      this.setBuffCheckTime(60000);
   }

   public void startDetail() {
      super.startDetail();
      ChangeEvilColor.sendWhenSelfChange((Player)this.getOwner(), this.getNextEvilEnum());
   }

   public void doWork(long time) {
      super.doWork(time);
      int curShowEvil = EvilManager.getShowEvil(((Player)this.getOwner()).getEvil());
      if (curShowEvil != this.showEvil) {
         this.showEvil = curShowEvil;
         BuffUpdateDydata.sendToClient(this.getOwner(), this);
      }

   }

   public void endSpecial(boolean notice) {
      ChangeEvilColor.sendWhenSelfChange((Player)this.getOwner(), ((Player)this.getOwner()).getSelfEvilEnum());
   }

   protected double getSpecialDyData(StatEnum stat) {
      if (stat == StatEnum.EVIL) {
         int curShowEvil = EvilManager.getShowEvil(((Player)this.getOwner()).getEvil());
         return (double)curShowEvil;
      } else {
         return 0.0D;
      }
   }

   private EvilEnum getNextEvilEnum() {
      return this.getModelID() == 80001 ? EvilEnum.Evil_Gray : EvilEnum.Evil_Red;
   }

   protected void doOverlapping() {
      super.doOverlapTime();
   }
}
