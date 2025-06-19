package com.mu.game.model.item.action.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.action.ItemAction;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.imp.BasicPropertyPopup;
import com.mu.io.game.packet.imp.player.pop.ShowPopup;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class EquipItem extends ItemAction {
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$com$mu$game$model$stats$StatEnum;

   public int doAction(Player player, Item item, int useNum, boolean definite, int wantedSlot) {
      int result = player.getEquipment().equipItem(item, wantedSlot, 1);
      return result;
   }

   protected int commonCondition(Player player, Item item, int useNum, boolean definite) {
      int result = super.commonCondition(player, item, useNum, definite);
      switch(result) {
      case 3034:
      case 3047:
      case 3048:
      case 3049:
      case 3050:
         this.openPanel(player, item);
      default:
         return result;
      }
   }

   private void openPanel(Player player, Item item) {
      if (player.getLevel() < 280) {
         HashMap statMap = new HashMap();
         int allPotential = 0;
         if (item.getModel().getNeedBasicPro().size() > 0) {
            Iterator it = item.getModel().getNeedBasicPro().entrySet().iterator();

            while(it.hasNext()) {
               Entry entry = (Entry)it.next();
               switch($SWITCH_TABLE$com$mu$game$model$stats$StatEnum()[((StatEnum)entry.getKey()).ordinal()]) {
               case 2:
               case 3:
               case 4:
               case 5:
                  int playerValue = player.getStatValue((StatEnum)entry.getKey());
                  if (playerValue < ((Integer)entry.getValue()).intValue()) {
                     statMap.put((StatEnum)entry.getKey(), new int[]{((Integer)entry.getValue()).intValue(), ((Integer)entry.getValue()).intValue() - playerValue});
                     allPotential += ((Integer)entry.getValue()).intValue() - playerValue;
                  }
               }
            }
         }

         if (allPotential > 0 && player.getPotential() >= allPotential) {
            BasicPropertyPopup popup = new BasicPropertyPopup(player.createPopupID(), item.getID(), statMap);
            ShowPopup.open(player, popup);
         }

         statMap.clear();
         statMap = null;
      }
   }

   public int privyCondition(Player player, Item item, int useNum, boolean definite) {
      return canEquipItem(item, player);
   }

   public static int canEquipItem(Item item, Player player) {
      if (item != null && item.getCount() >= 1) {
         if (item.getModel().getUseLevel() > player.getLevel()) {
            return 3004;
         } else if (item.getModel().getProfession().size() > 0 && !item.getModel().getProfession().contains(player.getProfessionID())) {
            return 3006;
         } else {
            return item.getModel().getGender() != -1 && player.getGender() != item.getModel().getGender() ? 3016 : 1;
         }
      } else {
         return 3002;
      }
   }

   public void useWhenObtaining(Player player, ItemModel model, int count, boolean isBind) {
   }

   public void initCheck(String des) throws Exception {
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$com$mu$game$model$stats$StatEnum() {
      int[] var10000 = $SWITCH_TABLE$com$mu$game$model$stats$StatEnum;
      if ($SWITCH_TABLE$com$mu$game$model$stats$StatEnum != null) {
         return var10000;
      } else {
         int[] var0 = new int[StatEnum.values().length];

         try {
            var0[StatEnum.ABSORB_AG.ordinal()] = 138;
         } catch (NoSuchFieldError var146) {
            ;
         }

         try {
            var0[StatEnum.ABSORB_HP.ordinal()] = 136;
         } catch (NoSuchFieldError var145) {
            ;
         }

         try {
            var0[StatEnum.ABSORB_MP.ordinal()] = 134;
         } catch (NoSuchFieldError var144) {
            ;
         }

         try {
            var0[StatEnum.ABSORB_SD.ordinal()] = 140;
         } catch (NoSuchFieldError var143) {
            ;
         }

         try {
            var0[StatEnum.AG.ordinal()] = 19;
         } catch (NoSuchFieldError var142) {
            ;
         }

         try {
            var0[StatEnum.AG_RECOVER.ordinal()] = 21;
         } catch (NoSuchFieldError var141) {
            ;
         }

         try {
            var0[StatEnum.AG_REC_KILL_MONSTER.ordinal()] = 22;
         } catch (NoSuchFieldError var140) {
            ;
         }

         try {
            var0[StatEnum.ALL_BASIS.ordinal()] = 6;
         } catch (NoSuchFieldError var139) {
            ;
         }

         try {
            var0[StatEnum.AP.ordinal()] = 23;
         } catch (NoSuchFieldError var138) {
            ;
         }

         try {
            var0[StatEnum.AP_RECOVER.ordinal()] = 25;
         } catch (NoSuchFieldError var137) {
            ;
         }

         try {
            var0[StatEnum.ATK.ordinal()] = 26;
         } catch (NoSuchFieldError var136) {
            ;
         }

         try {
            var0[StatEnum.ATK_AG_REC_RATE.ordinal()] = 81;
         } catch (NoSuchFieldError var135) {
            ;
         }

         try {
            var0[StatEnum.ATK_EXCELLENT_DAM.ordinal()] = 90;
         } catch (NoSuchFieldError var134) {
            ;
         }

         try {
            var0[StatEnum.ATK_EXCELLENT_RATE.ordinal()] = 89;
         } catch (NoSuchFieldError var133) {
            ;
         }

         try {
            var0[StatEnum.ATK_EXCELLENT_RES.ordinal()] = 91;
         } catch (NoSuchFieldError var132) {
            ;
         }

         try {
            var0[StatEnum.ATK_FATAL_DAM.ordinal()] = 97;
         } catch (NoSuchFieldError var131) {
            ;
         }

         try {
            var0[StatEnum.ATK_FATAL_RATE.ordinal()] = 95;
         } catch (NoSuchFieldError var130) {
            ;
         }

         try {
            var0[StatEnum.ATK_FATAL_RES.ordinal()] = 96;
         } catch (NoSuchFieldError var129) {
            ;
         }

         try {
            var0[StatEnum.ATK_LUCKY_DAM.ordinal()] = 93;
         } catch (NoSuchFieldError var128) {
            ;
         }

         try {
            var0[StatEnum.ATK_LUCKY_RATE.ordinal()] = 92;
         } catch (NoSuchFieldError var127) {
            ;
         }

         try {
            var0[StatEnum.ATK_LUCKY_RES.ordinal()] = 94;
         } catch (NoSuchFieldError var126) {
            ;
         }

         try {
            var0[StatEnum.ATK_MAX.ordinal()] = 28;
         } catch (NoSuchFieldError var125) {
            ;
         }

         try {
            var0[StatEnum.ATK_MIN.ordinal()] = 27;
         } catch (NoSuchFieldError var124) {
            ;
         }

         try {
            var0[StatEnum.ATK_MP_REC_RATE.ordinal()] = 83;
         } catch (NoSuchFieldError var123) {
            ;
         }

         try {
            var0[StatEnum.ATK_SPEED.ordinal()] = 67;
         } catch (NoSuchFieldError var122) {
            ;
         }

         try {
            var0[StatEnum.ATTACK_CAPABILITY.ordinal()] = 117;
         } catch (NoSuchFieldError var121) {
            ;
         }

         try {
            var0[StatEnum.AVD.ordinal()] = 33;
         } catch (NoSuchFieldError var120) {
            ;
         }

         try {
            var0[StatEnum.AVD_ABSOLUTE.ordinal()] = 34;
         } catch (NoSuchFieldError var119) {
            ;
         }

         try {
            var0[StatEnum.All_Points.ordinal()] = 56;
         } catch (NoSuchFieldError var118) {
            ;
         }

         try {
            var0[StatEnum.BEINJURED_HP_REC_RATE.ordinal()] = 85;
         } catch (NoSuchFieldError var117) {
            ;
         }

         try {
            var0[StatEnum.BEINJURED_SD_REC_RATE.ordinal()] = 87;
         } catch (NoSuchFieldError var116) {
            ;
         }

         try {
            var0[StatEnum.BIND_INGOT.ordinal()] = 44;
         } catch (NoSuchFieldError var115) {
            ;
         }

         try {
            var0[StatEnum.BIND_MONEY.ordinal()] = 42;
         } catch (NoSuchFieldError var114) {
            ;
         }

         try {
            var0[StatEnum.CD_BONUS.ordinal()] = 102;
         } catch (NoSuchFieldError var113) {
            ;
         }

         try {
            var0[StatEnum.CON.ordinal()] = 5;
         } catch (NoSuchFieldError var112) {
            ;
         }

         try {
            var0[StatEnum.Contribution.ordinal()] = 63;
         } catch (NoSuchFieldError var111) {
            ;
         }

         try {
            var0[StatEnum.DAM_ABSORB.ordinal()] = 69;
         } catch (NoSuchFieldError var110) {
            ;
         }

         try {
            var0[StatEnum.DAM_FORCE.ordinal()] = 80;
         } catch (NoSuchFieldError var109) {
            ;
         }

         try {
            var0[StatEnum.DAM_IGNORE.ordinal()] = 78;
         } catch (NoSuchFieldError var108) {
            ;
         }

         try {
            var0[StatEnum.DAM_PVP.ordinal()] = 77;
         } catch (NoSuchFieldError var107) {
            ;
         }

         try {
            var0[StatEnum.DAM_REDUCE.ordinal()] = 70;
         } catch (NoSuchFieldError var106) {
            ;
         }

         try {
            var0[StatEnum.DAM_REFLECTION.ordinal()] = 76;
         } catch (NoSuchFieldError var105) {
            ;
         }

         try {
            var0[StatEnum.DAM_REFLECTION_PRO.ordinal()] = 75;
         } catch (NoSuchFieldError var104) {
            ;
         }

         try {
            var0[StatEnum.DAM_STRENGTHEN.ordinal()] = 71;
         } catch (NoSuchFieldError var103) {
            ;
         }

         try {
            var0[StatEnum.DEF.ordinal()] = 29;
         } catch (NoSuchFieldError var102) {
            ;
         }

         try {
            var0[StatEnum.DEFENCE_CAPABILITY.ordinal()] = 118;
         } catch (NoSuchFieldError var101) {
            ;
         }

         try {
            var0[StatEnum.DEF_STRENGTH.ordinal()] = 30;
         } catch (NoSuchFieldError var100) {
            ;
         }

         try {
            var0[StatEnum.DEX.ordinal()] = 3;
         } catch (NoSuchFieldError var99) {
            ;
         }

         try {
            var0[StatEnum.DISARM.ordinal()] = 130;
         } catch (NoSuchFieldError var98) {
            ;
         }

         try {
            var0[StatEnum.DOMINEERING.ordinal()] = 65;
         } catch (NoSuchFieldError var97) {
            ;
         }

         try {
            var0[StatEnum.DOUBLE_ATK.ordinal()] = 68;
         } catch (NoSuchFieldError var96) {
            ;
         }

         try {
            var0[StatEnum.DROPRATE.ordinal()] = 62;
         } catch (NoSuchFieldError var95) {
            ;
         }

         try {
            var0[StatEnum.EVALUATION.ordinal()] = 57;
         } catch (NoSuchFieldError var94) {
            ;
         }

         try {
            var0[StatEnum.EVIL.ordinal()] = 48;
         } catch (NoSuchFieldError var93) {
            ;
         }

         try {
            var0[StatEnum.EXP.ordinal()] = 38;
         } catch (NoSuchFieldError var92) {
            ;
         }

         try {
            var0[StatEnum.EXP_BONUS.ordinal()] = 40;
         } catch (NoSuchFieldError var91) {
            ;
         }

         try {
            var0[StatEnum.FROST.ordinal()] = 122;
         } catch (NoSuchFieldError var90) {
            ;
         }

         try {
            var0[StatEnum.HIT.ordinal()] = 31;
         } catch (NoSuchFieldError var89) {
            ;
         }

         try {
            var0[StatEnum.HIT_ABSOLUTE.ordinal()] = 32;
         } catch (NoSuchFieldError var88) {
            ;
         }

         try {
            var0[StatEnum.HP.ordinal()] = 7;
         } catch (NoSuchFieldError var87) {
            ;
         }

         try {
            var0[StatEnum.HP_RECOVER.ordinal()] = 9;
         } catch (NoSuchFieldError var86) {
            ;
         }

         try {
            var0[StatEnum.HP_REC_KILL_MONSTER.ordinal()] = 10;
         } catch (NoSuchFieldError var85) {
            ;
         }

         try {
            var0[StatEnum.HisContribution.ordinal()] = 64;
         } catch (NoSuchFieldError var84) {
            ;
         }

         try {
            var0[StatEnum.IGNORE_DEF.ordinal()] = 79;
         } catch (NoSuchFieldError var83) {
            ;
         }

         try {
            var0[StatEnum.IGNORE_DEF_PRO.ordinal()] = 73;
         } catch (NoSuchFieldError var82) {
            ;
         }

         try {
            var0[StatEnum.IGNORE_SD_PRO.ordinal()] = 74;
         } catch (NoSuchFieldError var81) {
            ;
         }

         try {
            var0[StatEnum.INGOT.ordinal()] = 43;
         } catch (NoSuchFieldError var80) {
            ;
         }

         try {
            var0[StatEnum.INT.ordinal()] = 4;
         } catch (NoSuchFieldError var79) {
            ;
         }

         try {
            var0[StatEnum.INVINCIBLE.ordinal()] = 142;
         } catch (NoSuchFieldError var78) {
            ;
         }

         try {
            var0[StatEnum.ITEM_USERLEVEL_DOWN.ordinal()] = 54;
         } catch (NoSuchFieldError var77) {
            ;
         }

         try {
            var0[StatEnum.LEVEL.ordinal()] = 37;
         } catch (NoSuchFieldError var76) {
            ;
         }

         try {
            var0[StatEnum.LEVELGAP.ordinal()] = 61;
         } catch (NoSuchFieldError var75) {
            ;
         }

         try {
            var0[StatEnum.LUCKY.ordinal()] = 46;
         } catch (NoSuchFieldError var74) {
            ;
         }

         try {
            var0[StatEnum.MASTER_SKILL.ordinal()] = 58;
         } catch (NoSuchFieldError var73) {
            ;
         }

         try {
            var0[StatEnum.MAX_AG.ordinal()] = 20;
         } catch (NoSuchFieldError var72) {
            ;
         }

         try {
            var0[StatEnum.MAX_AP.ordinal()] = 24;
         } catch (NoSuchFieldError var71) {
            ;
         }

         try {
            var0[StatEnum.MAX_EXP.ordinal()] = 39;
         } catch (NoSuchFieldError var70) {
            ;
         }

         try {
            var0[StatEnum.MAX_HP.ordinal()] = 8;
         } catch (NoSuchFieldError var69) {
            ;
         }

         try {
            var0[StatEnum.MAX_MP.ordinal()] = 12;
         } catch (NoSuchFieldError var68) {
            ;
         }

         try {
            var0[StatEnum.MAX_SD.ordinal()] = 16;
         } catch (NoSuchFieldError var67) {
            ;
         }

         try {
            var0[StatEnum.MONEY.ordinal()] = 41;
         } catch (NoSuchFieldError var66) {
            ;
         }

         try {
            var0[StatEnum.MONEY_ADD_WKM.ordinal()] = 145;
         } catch (NoSuchFieldError var65) {
            ;
         }

         try {
            var0[StatEnum.MP.ordinal()] = 11;
         } catch (NoSuchFieldError var64) {
            ;
         }

         try {
            var0[StatEnum.MP_RECOVER.ordinal()] = 13;
         } catch (NoSuchFieldError var63) {
            ;
         }

         try {
            var0[StatEnum.MP_REC_KILL_MONSTER.ordinal()] = 14;
         } catch (NoSuchFieldError var62) {
            ;
         }

         try {
            var0[StatEnum.None.ordinal()] = 1;
         } catch (NoSuchFieldError var61) {
            ;
         }

         try {
            var0[StatEnum.PARALYSIS.ordinal()] = 126;
         } catch (NoSuchFieldError var60) {
            ;
         }

         try {
            var0[StatEnum.PERCENT_ATK_AG_REC.ordinal()] = 82;
         } catch (NoSuchFieldError var59) {
            ;
         }

         try {
            var0[StatEnum.PERCENT_ATK_MP_REC.ordinal()] = 84;
         } catch (NoSuchFieldError var58) {
            ;
         }

         try {
            var0[StatEnum.PERCENT_BEINJURED_HP_REC.ordinal()] = 86;
         } catch (NoSuchFieldError var57) {
            ;
         }

         try {
            var0[StatEnum.PERCENT_BEINJURED_SD_REC.ordinal()] = 88;
         } catch (NoSuchFieldError var56) {
            ;
         }

         try {
            var0[StatEnum.PETRIFICATION.ordinal()] = 124;
         } catch (NoSuchFieldError var55) {
            ;
         }

         try {
            var0[StatEnum.PKMODE.ordinal()] = 55;
         } catch (NoSuchFieldError var54) {
            ;
         }

         try {
            var0[StatEnum.POISONING.ordinal()] = 120;
         } catch (NoSuchFieldError var53) {
            ;
         }

         try {
            var0[StatEnum.POISONING_ATK_HURT.ordinal()] = 111;
         } catch (NoSuchFieldError var52) {
            ;
         }

         try {
            var0[StatEnum.POTENTIAL.ordinal()] = 47;
         } catch (NoSuchFieldError var51) {
            ;
         }

         try {
            var0[StatEnum.PROBABILITY.ordinal()] = 49;
         } catch (NoSuchFieldError var50) {
            ;
         }

         try {
            var0[StatEnum.REDEEM_POINTS.ordinal()] = 53;
         } catch (NoSuchFieldError var49) {
            ;
         }

         try {
            var0[StatEnum.RESURRENTION.ordinal()] = 128;
         } catch (NoSuchFieldError var48) {
            ;
         }

         try {
            var0[StatEnum.RES_ABSORB_AG.ordinal()] = 139;
         } catch (NoSuchFieldError var47) {
            ;
         }

         try {
            var0[StatEnum.RES_ABSORB_HP.ordinal()] = 137;
         } catch (NoSuchFieldError var46) {
            ;
         }

         try {
            var0[StatEnum.RES_ABSORB_MP.ordinal()] = 135;
         } catch (NoSuchFieldError var45) {
            ;
         }

         try {
            var0[StatEnum.RES_ABSORB_SD.ordinal()] = 141;
         } catch (NoSuchFieldError var44) {
            ;
         }

         try {
            var0[StatEnum.RES_DISARM.ordinal()] = 131;
         } catch (NoSuchFieldError var43) {
            ;
         }

         try {
            var0[StatEnum.RES_FROST.ordinal()] = 123;
         } catch (NoSuchFieldError var42) {
            ;
         }

         try {
            var0[StatEnum.RES_PARALYSIS.ordinal()] = 127;
         } catch (NoSuchFieldError var41) {
            ;
         }

         try {
            var0[StatEnum.RES_PETRIFICATION.ordinal()] = 125;
         } catch (NoSuchFieldError var40) {
            ;
         }

         try {
            var0[StatEnum.RES_POISONING.ordinal()] = 121;
         } catch (NoSuchFieldError var39) {
            ;
         }

         try {
            var0[StatEnum.RES_RESURRENTION.ordinal()] = 129;
         } catch (NoSuchFieldError var38) {
            ;
         }

         try {
            var0[StatEnum.RES_SACKED.ordinal()] = 133;
         } catch (NoSuchFieldError var37) {
            ;
         }

         try {
            var0[StatEnum.RES_WIND.ordinal()] = 144;
         } catch (NoSuchFieldError var36) {
            ;
         }

         try {
            var0[StatEnum.RNG.ordinal()] = 66;
         } catch (NoSuchFieldError var35) {
            ;
         }

         try {
            var0[StatEnum.SACKED.ordinal()] = 132;
         } catch (NoSuchFieldError var34) {
            ;
         }

         try {
            var0[StatEnum.SD.ordinal()] = 15;
         } catch (NoSuchFieldError var33) {
            ;
         }

         try {
            var0[StatEnum.SD_RECOVER.ordinal()] = 17;
         } catch (NoSuchFieldError var32) {
            ;
         }

         try {
            var0[StatEnum.SD_REC_KILL_MONSTER.ordinal()] = 18;
         } catch (NoSuchFieldError var31) {
            ;
         }

         try {
            var0[StatEnum.SD_REDUCTION.ordinal()] = 72;
         } catch (NoSuchFieldError var30) {
            ;
         }

         try {
            var0[StatEnum.SKILL_AG_REDUCE.ordinal()] = 100;
         } catch (NoSuchFieldError var29) {
            ;
         }

         try {
            var0[StatEnum.SKILL_ATK.ordinal()] = 98;
         } catch (NoSuchFieldError var28) {
            ;
         }

         try {
            var0[StatEnum.SKILL_CASTTIME.ordinal()] = 105;
         } catch (NoSuchFieldError var27) {
            ;
         }

         try {
            var0[StatEnum.SKILL_CD.ordinal()] = 101;
         } catch (NoSuchFieldError var26) {
            ;
         }

         try {
            var0[StatEnum.SKILL_COEFFICIENT.ordinal()] = 107;
         } catch (NoSuchFieldError var25) {
            ;
         }

         try {
            var0[StatEnum.SKILL_DATA3.ordinal()] = 114;
         } catch (NoSuchFieldError var24) {
            ;
         }

         try {
            var0[StatEnum.SKILL_DATA4.ordinal()] = 115;
         } catch (NoSuchFieldError var23) {
            ;
         }

         try {
            var0[StatEnum.SKILL_DATA5.ordinal()] = 116;
         } catch (NoSuchFieldError var22) {
            ;
         }

         try {
            var0[StatEnum.SKILL_DATA_1.ordinal()] = 112;
         } catch (NoSuchFieldError var21) {
            ;
         }

         try {
            var0[StatEnum.SKILL_DATA_2.ordinal()] = 113;
         } catch (NoSuchFieldError var20) {
            ;
         }

         try {
            var0[StatEnum.SKILL_DEGREE.ordinal()] = 109;
         } catch (NoSuchFieldError var19) {
            ;
         }

         try {
            var0[StatEnum.SKILL_DISTANCE.ordinal()] = 108;
         } catch (NoSuchFieldError var18) {
            ;
         }

         try {
            var0[StatEnum.SKILL_EFFECT_NUMBER.ordinal()] = 103;
         } catch (NoSuchFieldError var17) {
            ;
         }

         try {
            var0[StatEnum.SKILL_MP_REDUCE.ordinal()] = 99;
         } catch (NoSuchFieldError var16) {
            ;
         }

         try {
            var0[StatEnum.SKILL_PASSIVE_STAT.ordinal()] = 146;
         } catch (NoSuchFieldError var15) {
            ;
         }

         try {
            var0[StatEnum.SKILL_RANGE.ordinal()] = 104;
         } catch (NoSuchFieldError var14) {
            ;
         }

         try {
            var0[StatEnum.SKILL_WEAPON_HURT.ordinal()] = 110;
         } catch (NoSuchFieldError var13) {
            ;
         }

         try {
            var0[StatEnum.SPEED.ordinal()] = 45;
         } catch (NoSuchFieldError var12) {
            ;
         }

         try {
            var0[StatEnum.STR.ordinal()] = 2;
         } catch (NoSuchFieldError var11) {
            ;
         }

         try {
            var0[StatEnum.STRENGTH_LUCKY.ordinal()] = 50;
         } catch (NoSuchFieldError var10) {
            ;
         }

         try {
            var0[StatEnum.STRENGTH_NOBACK.ordinal()] = 51;
         } catch (NoSuchFieldError var9) {
            ;
         }

         try {
            var0[StatEnum.STRENGTH_NODESTROY.ordinal()] = 52;
         } catch (NoSuchFieldError var8) {
            ;
         }

         try {
            var0[StatEnum.TIME.ordinal()] = 106;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            var0[StatEnum.TRIGGER.ordinal()] = 119;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            var0[StatEnum.WEAPON_MAX_ATK.ordinal()] = 36;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            var0[StatEnum.WEAPON_MIN_ATK.ordinal()] = 35;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            var0[StatEnum.WIND.ordinal()] = 143;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            var0[StatEnum.WORLDLEVEL.ordinal()] = 60;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[StatEnum.ZHUIJIA.ordinal()] = 59;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$com$mu$game$model$stats$StatEnum = var0;
         return var0;
      }
   }
}
