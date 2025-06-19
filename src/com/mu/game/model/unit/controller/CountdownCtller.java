package com.mu.game.model.unit.controller;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.player.StopCountDown;

public class CountdownCtller {
   public static final int Countdown_CatchPet = 0;
   public static final int Countdown_ReadySkill = 1;
   public static final int Countdown_SkillProcess = 2;
   public static final int Countdown_Other = 3;
   public static final int Countdown_Gather = 4;
   public static final int Countdown_GearMan = 5;
   public int type = -1;
   private long timestamp = -1L;
   private long timeLength = -1L;
   private boolean isStart = false;
   public CountdownObject object;

   public CountdownCtller(CountdownObject object) {
      this.object = object;
   }

   public void reset(long countTime) {
      this.timestamp = countTime;
      this.timeLength = (long)this.object.getTimeLength();
   }

   public long getTimeStamp() {
      return this.timestamp;
   }

   public boolean isValid() {
      return this.type != -1;
   }

   public boolean countdownEnd(long endTime) {
      long diff = endTime - this.timestamp;
      boolean flag = diff >= this.timeLength;
      return flag;
   }

   public void endCountDown(Player player) {
      this.object.countdownEnd(player);
      this.noticeStop(player);
   }

   public void stopCountDown(Player player) {
      this.object.stopCountDown(player);
      this.noticeStop(player);
   }

   public void noticeStop(Player player) {
      StopCountDown.stop(player);
   }

   public void destroy() {
      this.object = null;
   }

   public boolean isStart() {
      return this.isStart;
   }

   public CountdownObject getCountdownObject() {
      return this.object;
   }

   public void setStart(boolean isStart) {
      this.isStart = isStart;
   }
}
