package com.mu.game.model.vip.effect;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.vip.VIPLevel;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class VIPEffect {
   protected VIPEffectType type;
   protected String name;
   protected HashMap valueMap;
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$com$mu$game$model$vip$effect$VIPEffectType;

   public VIPEffect(String name, VIPEffectType type) {
      this.type = type;
      this.name = name;
      this.valueMap = new LinkedHashMap();
   }

   public VIPEffectType getType() {
      return this.type;
   }

   public String getName() {
      return this.name;
   }

   public VIPEffectValue addLevelValue(VIPLevel vl) {
      VIPEffectValue vev = new VIPEffectValue(this, vl);
      this.valueMap.put(vl, vev);
      return vev;
   }

   public void effect(Player player, VIPLevel vl) {
      VIPEffectValue vev = this.getValue(vl);
      if (vev != null) {
         if (vev.isAvailable()) {
            switch($SWITCH_TABLE$com$mu$game$model$vip$effect$VIPEffectType()[this.getType().ordinal()]) {
            case 2:
               player.getBuffManager().createAndStartBuff(player, vev.getIntegerValue(), 1, true, 0L, (List)null);
            case 1:
            default:
            }
         }
      }
   }

   public void timeOut(Player player, VIPLevel vl) {
      VIPEffectValue vev = this.getValue(vl);
      if (vev != null) {
         switch($SWITCH_TABLE$com$mu$game$model$vip$effect$VIPEffectType()[this.getType().ordinal()]) {
         case 2:
            player.getBuffManager().endBuff(vev.getIntegerValue(), true);
         default:
         }
      }
   }

   public VIPEffectValue getValue(VIPLevel vl) {
      return (VIPEffectValue)this.valueMap.get(vl);
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$com$mu$game$model$vip$effect$VIPEffectType() {
      int[] var10000 = $SWITCH_TABLE$com$mu$game$model$vip$effect$VIPEffectType;
      if ($SWITCH_TABLE$com$mu$game$model$vip$effect$VIPEffectType != null) {
         return var10000;
      } else {
         int[] var0 = new int[VIPEffectType.values().length];

         try {
            var0[VIPEffectType.VE_1.ordinal()] = 1;
         } catch (NoSuchFieldError var24) {
            ;
         }

         try {
            var0[VIPEffectType.VE_10.ordinal()] = 9;
         } catch (NoSuchFieldError var23) {
            ;
         }

         try {
            var0[VIPEffectType.VE_11.ordinal()] = 10;
         } catch (NoSuchFieldError var22) {
            ;
         }

         try {
            var0[VIPEffectType.VE_12.ordinal()] = 11;
         } catch (NoSuchFieldError var21) {
            ;
         }

         try {
            var0[VIPEffectType.VE_13.ordinal()] = 12;
         } catch (NoSuchFieldError var20) {
            ;
         }

         try {
            var0[VIPEffectType.VE_14.ordinal()] = 13;
         } catch (NoSuchFieldError var19) {
            ;
         }

         try {
            var0[VIPEffectType.VE_15.ordinal()] = 14;
         } catch (NoSuchFieldError var18) {
            ;
         }

         try {
            var0[VIPEffectType.VE_16.ordinal()] = 15;
         } catch (NoSuchFieldError var17) {
            ;
         }

         try {
            var0[VIPEffectType.VE_17.ordinal()] = 16;
         } catch (NoSuchFieldError var16) {
            ;
         }

         try {
            var0[VIPEffectType.VE_18.ordinal()] = 17;
         } catch (NoSuchFieldError var15) {
            ;
         }

         try {
            var0[VIPEffectType.VE_19.ordinal()] = 18;
         } catch (NoSuchFieldError var14) {
            ;
         }

         try {
            var0[VIPEffectType.VE_2.ordinal()] = 2;
         } catch (NoSuchFieldError var13) {
            ;
         }

         try {
            var0[VIPEffectType.VE_20.ordinal()] = 19;
         } catch (NoSuchFieldError var12) {
            ;
         }

         try {
            var0[VIPEffectType.VE_21.ordinal()] = 20;
         } catch (NoSuchFieldError var11) {
            ;
         }

         try {
            var0[VIPEffectType.VE_22.ordinal()] = 21;
         } catch (NoSuchFieldError var10) {
            ;
         }

         try {
            var0[VIPEffectType.VE_23.ordinal()] = 22;
         } catch (NoSuchFieldError var9) {
            ;
         }

         try {
            var0[VIPEffectType.VE_24.ordinal()] = 23;
         } catch (NoSuchFieldError var8) {
            ;
         }

         try {
            var0[VIPEffectType.VE_25.ordinal()] = 24;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            var0[VIPEffectType.VE_3.ordinal()] = 3;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            var0[VIPEffectType.VE_4.ordinal()] = 4;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            var0[VIPEffectType.VE_6.ordinal()] = 5;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            var0[VIPEffectType.VE_7.ordinal()] = 6;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            var0[VIPEffectType.VE_8.ordinal()] = 7;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[VIPEffectType.VE_9.ordinal()] = 8;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$com$mu$game$model$vip$effect$VIPEffectType = var0;
         return var0;
      }
   }
}
