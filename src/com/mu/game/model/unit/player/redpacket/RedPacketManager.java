package com.mu.game.model.unit.player.redpacket;

import com.mu.game.model.gang.RedPacketInfo;
import com.mu.game.model.unit.player.Player;
import java.util.HashMap;

public class RedPacketManager {
   private Player owner;
   private int todayBindRedReceive = 0;
   private HashMap bindSendMap = new HashMap();

   public RedPacketManager(Player owner) {
      this.owner = owner;
   }

   public int getBindSendTimes(int id) {
      Integer in = (Integer)this.bindSendMap.get(id);
      return in == null ? 0 : in.intValue();
   }

   public void addBindSendTimes(int id, int times) {
      int value = this.getBindSendTimes(id);
      this.bindSendMap.put(id, value + times);
   }

   public int getBindPacketLeftTimes(RedPacketInfo info) {
      if (info == null) {
         return 0;
      } else {
         int times = this.owner.getUser().getTotleRed(info.getId()) - this.getBindSendTimes(info.getId());
         return times < 0 ? 0 : times;
      }
   }

   public void dayChange() {
      this.todayBindRedReceive = 0;
   }

   public int getTodayBindRedReceive() {
      return this.todayBindRedReceive;
   }

   public void setTodayBindRedReceive(int todayBindRedReceive) {
      this.todayBindRedReceive = todayBindRedReceive;
   }

   public void addTodayBindRedReceive(int times) {
      this.todayBindRedReceive += times;
   }

   public void destroy() {
      if (this.bindSendMap != null) {
         this.bindSendMap.clear();
         this.bindSendMap = null;
      }

      this.owner = null;
   }
}
