package com.mu.game.model.market;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.vip.effect.VIPEffectType;

public class MarketConstant {
   public static final int Condition_Level = 1;
   public static final int Condition_Lucky = 2;
   public static final int Condition_IngoreDefPro = 3;
   public static final int Condition_EquipSet = 4;
   public static final int Condition_ExcellentCount = 5;
   public static final int Condition_None = 6;
   public static final int ShelveTime = 259200000;
   public static final int Market_Page_Count = 12;
   public static final int Market_Role_ItemCount = 11;
   public static final int Market_TaxRate = 20000;
   public static final int MarketRecordMax = 20;

   public static int getPersonTaxRate(Player player) {
      int rate = 20000;
      if (!player.getVIPManager().isTimeOut()) {
         rate += player.getVIPManager().getEffectIntegerValue(VIPEffectType.VE_16);
      }

      return Math.max(1, rate);
   }
}
