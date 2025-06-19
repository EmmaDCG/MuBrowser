package com.mu.game.model.map.enter.req;

public class EnterMapRequiremenetFactory {
   public static final int Req_Level = 1;
   public static final int Req_Wing = 2;

   public static EnterMapRequirement createRequirement(String s) {
      String[] tmp = s.split(",");
      int type = Integer.parseInt(tmp[0]);
      switch(type) {
      case 1:
         return createLevelEquirement(tmp[1]);
      case 2:
         return createEquipWingEquirement();
      default:
         return null;
      }
   }

   public static EnterMapLevelRequirement createLevelEquirement(String s) {
      int level = Integer.parseInt(s);
      return new EnterMapLevelRequirement(level);
   }

   public static EnterMapEquipWingRequirement createEquipWingEquirement() {
      return new EnterMapEquipWingRequirement();
   }
}
