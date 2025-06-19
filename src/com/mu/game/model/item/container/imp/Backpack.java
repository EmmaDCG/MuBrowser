package com.mu.game.model.item.container.imp;

import com.mu.game.model.item.container.BackpackExpandData;
import com.mu.game.model.unit.player.Player;

public class Backpack extends Storage {
   private long lastCheckTime;
   private int curGridTime = 0;
   private int curWaitOpenGrid;
   private int needTime = 1;

   public Backpack(int page) {
      super(1, page);
      this.setCurWaitOpenGrid(page + 1);
      this.setLastCheckTime(System.currentTimeMillis());
   }

   public void reset(int page, int cooledCount, int time) {
      super.reset(page);
      this.setCurWaitOpenGrid(page + cooledCount + 1);
      this.setCurGridTime(time);
      this.checkTime(System.currentTimeMillis());
   }

   public boolean checkTime(long now) {
      if (this.getLimit() >= 98) {
         return false;
      } else {
         long time = (now - this.getLastCheckTime()) / 1000L + (long)this.curGridTime;
         if (time >= 2147483647L) {
            time = 2147483646L;
         }

         time = Math.max(0L, time);
         int tmpWaitOpen = this.getCurWaitOpenGrid();

         for(int i = this.getCurWaitOpenGrid(); i <= this.getMaxCount(); ++i) {
            BackpackExpandData expandData = BackpackExpandData.getExpandData(i);
            tmpWaitOpen = i;
            this.needTime = expandData.getNeedTime();
            if ((long)expandData.getNeedTime() > time) {
               break;
            }

            time -= (long)expandData.getNeedTime();
         }

         boolean change = tmpWaitOpen != this.getCurWaitOpenGrid();
         this.setCurWaitOpenGrid(tmpWaitOpen);
         this.setCurGridTime((int)time);
         this.setLastCheckTime(now);
         return change;
      }
   }

   public int expansion(Player player, int addedPage) {
      int result = super.expansion(player, addedPage);
      if (result == 1) {
         if (this.getLimit() >= this.getCurWaitOpenGrid()) {
            this.setCurWaitOpenGrid(this.getLimit() + 1);
            this.setCurGridTime(0);
            this.setLastCheckTime(System.currentTimeMillis());
         }

         this.checkTime(System.currentTimeMillis());
      }

      return result;
   }

   public int getRemainCoolTime() {
      int remainTime = -1;
      if (this.getLimit() < this.getMaxCount()) {
         remainTime = this.getNeedTime() - this.getCurGridTime();
      }

      return remainTime;
   }

   public int getCooledCount() {
      return this.curWaitOpenGrid - this.getLimit() - 1;
   }

   public void setCurWaitOpenGrid(int curWaitOpenGrid) {
      curWaitOpenGrid = Math.max(50, curWaitOpenGrid);
      this.curWaitOpenGrid = Math.min(curWaitOpenGrid, 98);
   }

   public long getLastCheckTime() {
      return this.lastCheckTime;
   }

   public void setLastCheckTime(long lastCheckTime) {
      this.lastCheckTime = lastCheckTime;
   }

   public int getCurGridTime() {
      return this.curGridTime;
   }

   public void setCurGridTime(int curGridTime) {
      this.curGridTime = curGridTime;
   }

   public int getNeedTime() {
      return this.needTime;
   }

   public void setNeedTime(int needTime) {
      this.needTime = needTime;
   }

   public int getCurWaitOpenGrid() {
      return this.curWaitOpenGrid;
   }
}
