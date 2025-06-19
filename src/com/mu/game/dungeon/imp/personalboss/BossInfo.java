package com.mu.game.dungeon.imp.personalboss;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.unit.monster.worldboss.WorldBossData;
import com.mu.game.model.unit.monster.worldboss.WorldBossManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.vip.effect.VIPEffectType;

public class BossInfo {
   private int timeLimit = 1;
   private int x;
   private int y;
   private int bossId;
   private int levelLimit;
   private int ticket;
   private int ticketNumber;
   private String ticketName;
   private Item ticketItem;
   private long telTime;

   public int getTimeLimit() {
      return this.timeLimit;
   }

   public void setTimeLimit(int timeLimit) {
      this.timeLimit = timeLimit;
   }

   public int getX() {
      return this.x;
   }

   public void setX(int x) {
      this.x = x;
   }

   public int getY() {
      return this.y;
   }

   public void setY(int y) {
      this.y = y;
   }

   public int getBossId() {
      return this.bossId;
   }

   public void setBossId(int bossId) {
      this.bossId = bossId;
   }

   public Item getTicketItem() {
      return this.ticketItem;
   }

   public void setTicketItem(Item ticketItem) {
      this.ticketItem = ticketItem;
   }

   public String getTicketName() {
      if (this.ticketName == null) {
         ItemModel model = ItemModel.getModel(this.ticket);
         this.ticketName = model.getName();
      }

      return this.ticketName;
   }

   public int getLevelLimit() {
      return this.levelLimit;
   }

   public void setLevelLimit(int levelLimit) {
      this.levelLimit = levelLimit;
   }

   public int getTicket() {
      return this.ticket;
   }

   public void setTicket(int ticket) {
      this.ticket = ticket;
   }

   public int getTicketNumber() {
      return this.ticketNumber;
   }

   public void setTicketNumber(int ticketNumber) {
      this.ticketNumber = ticketNumber;
   }

   public WorldBossData getBossData() {
      return WorldBossManager.getBossData(this.bossId);
   }

   public int getPlayerTimeLimit(Player player) {
      return this.timeLimit + player.getVIPManager().getEffectIntegerValue(VIPEffectType.VE_7);
   }

   public long getTelTime() {
      return this.telTime;
   }

   public void setTelTime(long telTime) {
      this.telTime = telTime;
   }
}
