package com.mu.game.model.vip;

import com.mu.executor.Executor;
import com.mu.game.model.task.target.TargetType;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.unitevent.imp.VIPEvent;
import com.mu.game.model.vip.effect.VIPEffect;
import com.mu.game.model.vip.effect.VIPEffectType;
import com.mu.game.model.vip.effect.VIPEffectValue;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.dm.UpdateMenu;
import com.mu.io.game.packet.imp.monster.RefreshSingleBoss;
import com.mu.io.game.packet.imp.vip.InitVIP;
import com.mu.io.game.packet.imp.vip.VIPInform;
import com.mu.utils.Time;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerVIPManager {
   private static Logger logger = LoggerFactory.getLogger(PlayerVIPManager.class);
   private Player owner;
   private VIPData data;
   private long activeTime;
   private int buyDays;
   private int expDays;

   public PlayerVIPManager(Player player) {
      this.owner = player;
   }

   public void init(InitVIP iv) {
      if (iv.remaining() > 0) {
         try {
            int vipId = iv.readByte();
            long activeTime = iv.readLong();
            int buyDays = iv.readShort();
            int expDays = iv.readShort();
            this.data = VIPConfigManager.getVIP(vipId);
            this.activeTime = activeTime;
            this.buyDays = buyDays;
            this.expDays = expDays;
         } catch (Exception var7) {
            logger.error("role vip init error");
            var7.printStackTrace();
         }
      }

   }

   public void onEnterGame() {
      VIPInform.sendMsgVIPLevel(this.owner);
      if (!this.isTimeOut()) {
         this.owner.addMomentEvent(new VIPEvent(this.owner));
         this.effect();
         if (this.owner.isNeedZeroClear()) {
            this.owner.getVitalityManager().addVitality(this.getEffectIntegerValue(VIPEffectType.VE_17));
         }
      }

      this.autoAddExp();
   }

   private void autoAddExp() {
      if (this.data != null) {
         if (this.owner.isNeedZeroClear()) {
            if (this.expDays < this.buyDays) {
               int d = Math.min(this.buyDays, (int)((System.currentTimeMillis() - Time.getTodayBegin(this.activeTime)) / 86400000L));
               if (d > this.expDays) {
                  this.increaseExp(this.data.getExp() * (d - this.expDays) * 10, true);
                  this.expDays = d;
                  this.dbReplaceVIP();
               }

            }
         }
      }
   }

   public void activationVIP(int vipId) {
      VIPData data = VIPConfigManager.getVIP(vipId);
      if (data != null) {
         boolean active = this.isTimeOut();
         if (active) {
            this.data = data;
            this.activeTime = System.currentTimeMillis();
            this.buyDays = data.getTimeDay();
            this.owner.addMomentEvent(new VIPEvent(this.owner));
         } else {
            this.data = data.getExp() > this.data.getExp() ? data : this.data;
            this.buyDays += data.getTimeDay();
         }

         if (data.getBaseLv() > this.owner.getVIPLevel()) {
            this.owner.setVIPLevel(data.getBaseLv());
            this.dbUpdateVIPLevel();
            active = true;
         }

         if (active) {
            this.effect();
            this.owner.getVitalityManager().addVitality(this.getEffectIntegerValue(VIPEffectType.VE_17));
            VIPInform.sendMsgVIPLevel(this.owner);
         }

         this.dbReplaceVIP();
         VIPInform.sendMsgVIPLevel(this.owner);
         this.owner.getTaskManager().onEventCheckValue(TargetType.ValueType.Vip_Level);
         this.refreshWhenVipChanged();
      }
   }

   public void onVIPTimeOut() {
      VIPInform.sendMsgVIPLevel(this.getOwner());
      VIPLevel vl = this.getLevel();
      Iterator it = VIPConfigManager.getEffectIterator();

      while(it.hasNext()) {
         VIPEffect ve = (VIPEffect)it.next();
         ve.timeOut(this.owner, vl);
      }

      this.refreshWhenVipChanged();
   }

   public int getEffectIntegerValue(VIPEffectType vet) {
      if (this.isTimeOut()) {
         return 0;
      } else {
         VIPEffectValue vev = VIPConfigManager.getEffectValue(vet, this.getLevel());
         return !vev.isAvailable() ? 0 : vev.getIntegerValue();
      }
   }

   public boolean getEffectBooleanValue(VIPEffectType vet) {
      if (this.isTimeOut()) {
         return false;
      } else {
         VIPEffectValue vev = VIPConfigManager.getEffectValue(vet, this.getLevel());
         return !vev.isAvailable() ? false : vev.isBooleanValue();
      }
   }

   public VIPLevel getLevel() {
      return VIPConfigManager.getLevel(this.owner.getVIPLevel());
   }

   public void effect() {
      VIPLevel vl = this.getLevel();
      Iterator it = VIPConfigManager.getEffectIterator();

      while(it.hasNext()) {
         VIPEffect ve = (VIPEffect)it.next();
         ve.effect(this.owner, vl);
      }

   }

   public void onSkipDay() {
      if (!this.isTimeOut()) {
         ++this.expDays;
         this.increaseExp(this.data.getExp() * 10, true);
         this.dbReplaceVIP();
         this.owner.getVitalityManager().addVitality(this.getEffectIntegerValue(VIPEffectType.VE_17));
      }

   }

   public boolean isTimeOut() {
      if (this.data == null) {
         return true;
      } else {
         return System.currentTimeMillis() > this.activeTime + (long)this.buyDays * 86400000L;
      }
   }

   public int getMaxExp() {
      return VIPConfigManager.getLevel(this.owner.getVIPLevel()).getMaxExp();
   }

   private void levelUp(int overExp, boolean sendMsg) {
      this.owner.setVIPLevel(this.owner.getVIPLevel() + 1);
      this.owner.setVIPExp(0);
      this.increaseExp(overExp, false);
      if (sendMsg) {
         this.dbUpdateVIPLevel();
         VIPInform.sendMsgVIPLevel(this.owner);
         this.effect();
         this.owner.getTaskManager().onEventCheckValue(TargetType.ValueType.Vip_Level);
         this.refreshWhenVipChanged();
      }

   }

   public void increaseExp(int exp, boolean sendMsg) {
      int overExp = this.owner.getVIPExp() + exp - this.getMaxExp();
      if (overExp >= 0 && VIPConfigManager.getLevelTail().getLevel() > this.owner.getVIPLevel()) {
         this.levelUp(overExp, sendMsg);
      } else {
         this.owner.setVIPExp(this.owner.getVIPExp() + exp);
         if (sendMsg) {
            VIPInform.sendMsgVIPLevel(this.owner);
         }
      }

   }

   public void destroy() {
      this.owner = null;
      this.data = null;
   }

   public Player getOwner() {
      return this.owner;
   }

   public void dbReplaceVIP() {
      try {
         WriteOnlyPacket packet = Executor.ReplaceVIP.toPacket(this.owner.getID(), this.data.getId(), this.activeTime, this.buyDays, this.expDays);
         this.owner.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public void dbUpdateVIPLevel() {
      try {
         WriteOnlyPacket packet = Executor.UpdateVIPLevel.toPacket(this.owner);
         this.owner.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public void refreshWhenVipChanged() {
      RefreshSingleBoss.refreshAllSingleBoss(this.owner);
      this.owner.getTitleManager().vipLevelChanged();
      UpdateMenu.update(this.owner, 32);
      UpdateMenu.update(this.owner, 1);
      UpdateMenu.update(this.owner, 2);
   }

   public long getOffTime() {
      return this.data == null ? 0L : this.activeTime + (long)this.buyDays * 86400000L;
   }

   public VIPData getData() {
      return this.data;
   }
}
