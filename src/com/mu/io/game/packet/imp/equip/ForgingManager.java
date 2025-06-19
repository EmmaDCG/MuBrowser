package com.mu.io.game.packet.imp.equip;

import java.util.HashMap;

public class ForgingManager {
   private static HashMap roleStarRecordMap = new HashMap();

   public static boolean hasProp(long roleID, long itemID, int starLevel) {
      if (!roleStarRecordMap.containsKey(roleID)) {
         return false;
      } else {
         HashMap starRecordMap = (HashMap)roleStarRecordMap.get(roleID);
         if (!starRecordMap.containsKey(starLevel)) {
            return false;
         } else {
            return ((Long)starRecordMap.get(starLevel)).longValue() == itemID;
         }
      }
   }

   public static void addStarRecord(long roleID, long itemID, int starLevel) {
      HashMap starRecordMap = (HashMap)roleStarRecordMap.get(roleID);
      if (starRecordMap == null) {
         starRecordMap = new HashMap();
         roleStarRecordMap.put(roleID, starRecordMap);
      }

      starRecordMap.put(starLevel, itemID);
   }
}
