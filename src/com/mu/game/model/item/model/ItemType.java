package com.mu.game.model.item.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemType {
   private static HashMap typeToWts = new HashMap();
   public static final int OneHand = 1;
   public static final int BothHands_H = 2;
   public static final int BOW = 3;
   public static final int CrossBow = 4;
   public static final int BothHands_L = 5;
   public static final int Staff = 6;
   public static final int Arrows = 7;
   public static final int Shield = 8;
   public static final int MagicBook = 9;
   public static final int NECKLACE = 11;
   public static final int BELT = 12;
   public static final int WRIST = 13;
   public static final int RING = 14;
   public static final int BOOTS = 15;
   public static final int HELMET = 16;
   public static final int HEADDRESS = 17;
   public static final int CLOTHES = 18;
   public static final int CLOAK = 19;
   public static final int Panda = 20;
   public static final int TROUSERS = 21;
   public static final int GUARDIAN = 22;
   public static final int WING = 23;
   public static final int RIDING = 24;
   public static final int JEWELRY = 25;
   public static final int Drug_Hp = 31;
   public static final int Drug_Mp = 32;
   public static final int Forging_Lucky = 172;
   public static final int Forging_Protect = 175;
   public static final int Forging_Rune = 161;
   public static final int MoneyType = 43;
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$com$mu$game$model$item$model$WeaponType;

   public static void addWeaponType(int itemType, int wt) {
      typeToWts.put(itemType, WeaponType.fine(wt));
   }

   public static WeaponType getWeaponType(int itemType) {
      return typeToWts.containsKey(itemType) ? (WeaponType)typeToWts.get(itemType) : WeaponType.None;
   }

   public static List getEquipSlot(int itemType) {
      List slots = new ArrayList();
      switch(itemType) {
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
         slots.add(Integer.valueOf(0));
         WeaponType wt = getWeaponType(itemType);
         switch($SWITCH_TABLE$com$mu$game$model$item$model$WeaponType()[wt.ordinal()]) {
         case 2:
            slots.add(Integer.valueOf(1));
            return slots;
         default:
            return slots;
         }
      case 7:
      case 8:
      case 9:
         slots.add(Integer.valueOf(1));
      case 10:
      case 12:
      case 19:
      default:
         break;
      case 11:
         slots.add(Integer.valueOf(6));
         break;
      case 13:
         slots.add(Integer.valueOf(7));
         break;
      case 14:
         slots.add(Integer.valueOf(8));
         slots.add(Integer.valueOf(9));
         break;
      case 15:
         slots.add(Integer.valueOf(10));
         break;
      case 16:
      case 17:
         slots.add(Integer.valueOf(4));
         break;
      case 18:
         slots.add(Integer.valueOf(2));
         break;
      case 20:
         slots.add(Integer.valueOf(5));
         break;
      case 21:
         slots.add(Integer.valueOf(3));
         break;
      case 22:
         slots.add(Integer.valueOf(11));
         break;
      case 23:
         slots.add(Integer.valueOf(12));
         break;
      case 24:
         slots.add(Integer.valueOf(13));
         break;
      case 25:
         slots.add(Integer.valueOf(14));
      }

      return slots;
   }

   public static List getEquipExternalType(int itemType, int slot) {
      List externalTypes = new ArrayList();
      switch(itemType) {
      case 1:
      case 2:
      case 4:
      case 5:
      case 6:
         WeaponType wt = WeaponType.fine(itemType);
         switch($SWITCH_TABLE$com$mu$game$model$item$model$WeaponType()[wt.ordinal()]) {
         case 2:
            if (slot == 0) {
               externalTypes.add(Integer.valueOf(7));
            } else {
               externalTypes.add(Integer.valueOf(6));
            }

            return externalTypes;
         case 3:
         case 4:
         default:
            externalTypes.add(Integer.valueOf(6));
            externalTypes.add(Integer.valueOf(7));
            return externalTypes;
         case 5:
            externalTypes.add(Integer.valueOf(7));
            return externalTypes;
         }
      case 3:
         externalTypes.add(Integer.valueOf(6));
         break;
      case 7:
         externalTypes.add(Integer.valueOf(10));
         break;
      case 8:
      case 9:
         externalTypes.add(Integer.valueOf(6));
      case 10:
      case 11:
      case 12:
      case 14:
      case 19:
      case 20:
      default:
         break;
      case 13:
         externalTypes.add(Integer.valueOf(3));
         break;
      case 15:
         externalTypes.add(Integer.valueOf(5));
         break;
      case 16:
      case 17:
         externalTypes.add(Integer.valueOf(1));
         break;
      case 18:
         externalTypes.add(Integer.valueOf(2));
         break;
      case 21:
         externalTypes.add(Integer.valueOf(4));
         break;
      case 22:
         externalTypes.add(Integer.valueOf(12));
         break;
      case 23:
         externalTypes.add(Integer.valueOf(8));
         break;
      case 24:
         externalTypes.add(Integer.valueOf(9));
      }

      return externalTypes;
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$com$mu$game$model$item$model$WeaponType() {
      int[] var10000 = $SWITCH_TABLE$com$mu$game$model$item$model$WeaponType;
      if ($SWITCH_TABLE$com$mu$game$model$item$model$WeaponType != null) {
         return var10000;
      } else {
         int[] var0 = new int[WeaponType.values().length];

         try {
            var0[WeaponType.BothHands.ordinal()] = 3;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            var0[WeaponType.MainHand.ordinal()] = 5;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            var0[WeaponType.None.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            var0[WeaponType.OneHand.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[WeaponType.SecondaryHand.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$com$mu$game$model$item$model$WeaponType = var0;
         return var0;
      }
   }
}
