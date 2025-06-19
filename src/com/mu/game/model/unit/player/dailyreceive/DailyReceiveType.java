package com.mu.game.model.unit.player.dailyreceive;

import com.mu.game.model.unit.player.Player;
import java.util.HashMap;

public enum DailyReceiveType {
   Daily_BigDevil(1, 8, 1);

   private static HashMap typeMap = new HashMap();
   private int type;
   private int hour;
   private int maxReceiveTime = 1;

   static {
      DailyReceiveType[] var3;
      int var2 = (var3 = values()).length;

      for(int var1 = 0; var1 < var2; ++var1) {
         DailyReceiveType type = var3[var1];
         typeMap.put(type.getType(), type);
      }

   }

   private DailyReceiveType(int type, int hour, int maxTimes) {
      this.type = type;
      this.hour = hour;
      this.maxReceiveTime = maxTimes;
   }

   public int getType() {
      return this.type;
   }

   public int getHour() {
      return this.hour;
   }

   public int getMaxReceiveTime(Player player) {
      return this.maxReceiveTime;
   }

   public static DailyReceiveType getReceiveType(int type) {
      return (DailyReceiveType)typeMap.get(type);
   }
}
