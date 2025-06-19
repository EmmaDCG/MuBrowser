package com.mu.game.model.stats;

import com.mu.config.StatNames;
import com.mu.config.VariableConstant;
import java.util.ArrayList;
import java.util.HashMap;

public enum StatEnum {
   None(10000, 100, false),
   STR(1, 1, false),
   DEX(2, 2, false),
   INT(4, 3, false),
   CON(3, 4, false),
   ALL_BASIS(5, 5, false),
   HP(21, 11, false),
   MAX_HP(22, 11, false),
   HP_RECOVER(23, 11, true),
   HP_REC_KILL_MONSTER(24, 11, true),
   MP(26, 12, false),
   MAX_MP(27, 12, false),
   MP_RECOVER(28, 12, true),
   MP_REC_KILL_MONSTER(29, 12, true),
   SD(31, 13, false),
   MAX_SD(32, 13, false),
   SD_RECOVER(33, 13, false),
   SD_REC_KILL_MONSTER(34, 13, false),
   AG(36, 14, false),
   MAX_AG(37, 14, false),
   AG_RECOVER(38, 14, false),
   AG_REC_KILL_MONSTER(39, 14, false),
   AP(41, 15, false),
   MAX_AP(42, 15, false),
   AP_RECOVER(43, 15, false),
   ATK(51, 15, false),
   ATK_MIN(52, 15, false),
   ATK_MAX(53, 15, false),
   DEF(56, 16, false),
   DEF_STRENGTH(57, 1, true),
   HIT(61, 17, false),
   HIT_ABSOLUTE(62, 17, true),
   AVD(66, 18, false),
   AVD_ABSOLUTE(67, 18, true),
   WEAPON_MIN_ATK(72, 15, false),
   WEAPON_MAX_ATK(73, 15, false),
   LEVEL(101, 1, false),
   EXP(102, 1, false),
   MAX_EXP(103, 1, false),
   EXP_BONUS(104, 1, true),
   MONEY(105, 1, false),
   BIND_MONEY(106, 1, false),
   INGOT(107, 1, false),
   BIND_INGOT(108, 1, false),
   SPEED(109, 1, false),
   LUCKY(110, 1, false),
   POTENTIAL(111, 1, false),
   EVIL(112, 1, false),
   PROBABILITY(113, 1, false),
   STRENGTH_LUCKY(114, 1, true),
   STRENGTH_NOBACK(115, 1, false),
   STRENGTH_NODESTROY(116, 1, false),
   REDEEM_POINTS(117, 1, false),
   ITEM_USERLEVEL_DOWN(118, 1, false),
   PKMODE(119, 1, false),
   All_Points(120, 1, false),
   EVALUATION(121, 1, false),
   MASTER_SKILL(122, 1, false),
   ZHUIJIA(123, 1, false),
   WORLDLEVEL(124, 1, false),
   LEVELGAP(125, 1, false),
   DROPRATE(126, 1, true),
   Contribution(127, 1, false),
   HisContribution(128, 1, false),
   DOMINEERING(129, 1, false),
   RNG(201, 1, false),
   ATK_SPEED(202, 1, false),
   DOUBLE_ATK(203, 1, false),
   DAM_ABSORB(207, 1, true),
   DAM_REDUCE(208, 1, true),
   DAM_STRENGTHEN(209, 1, true),
   SD_REDUCTION(212, 1, true),
   IGNORE_DEF_PRO(204, 1, true),
   IGNORE_SD_PRO(205, 1, true),
   DAM_REFLECTION_PRO(250, 1, true),
   DAM_REFLECTION(206, 1, true),
   DAM_PVP(214, 1, false),
   DAM_IGNORE(211, 1, false),
   IGNORE_DEF(213, 1, false),
   DAM_FORCE(210, 1, false),
   ATK_AG_REC_RATE(215, 1, true),
   PERCENT_ATK_AG_REC(216, 1, true),
   ATK_MP_REC_RATE(217, 1, true),
   PERCENT_ATK_MP_REC(218, 1, true),
   BEINJURED_HP_REC_RATE(219, 1, true),
   PERCENT_BEINJURED_HP_REC(220, 1, true),
   BEINJURED_SD_REC_RATE(221, 1, true),
   PERCENT_BEINJURED_SD_REC(222, 1, true),
   ATK_EXCELLENT_RATE(241, 1, true),
   ATK_EXCELLENT_DAM(242, 1, true),
   ATK_EXCELLENT_RES(243, 1, true),
   ATK_LUCKY_RATE(244, 1, true),
   ATK_LUCKY_DAM(245, 1, true),
   ATK_LUCKY_RES(246, 1, true),
   ATK_FATAL_RATE(247, 1, true),
   ATK_FATAL_RES(248, 1, true),
   ATK_FATAL_DAM(249, 1, true),
   SKILL_ATK(401, 1, true),
   SKILL_MP_REDUCE(402, 1, true),
   SKILL_AG_REDUCE(403, 1, true),
   SKILL_CD(404, 1, false),
   CD_BONUS(405, 1, false),
   SKILL_EFFECT_NUMBER(406, 1, false),
   SKILL_RANGE(407, 1, false),
   SKILL_CASTTIME(408, 1, false),
   TIME(409, 1, false),
   SKILL_COEFFICIENT(410, 1, false),
   SKILL_DISTANCE(414, 1, false),
   SKILL_DEGREE(415, 1, false),
   SKILL_WEAPON_HURT(416, 1, true),
   POISONING_ATK_HURT(417, 1, true),
   SKILL_DATA_1(421, 1, false),
   SKILL_DATA_2(422, 1, false),
   SKILL_DATA3(423, 1, false),
   SKILL_DATA4(424, 1, false),
   SKILL_DATA5(425, 1, false),
   ATTACK_CAPABILITY(426, 1, false),
   DEFENCE_CAPABILITY(427, 1, false),
   TRIGGER(300, 1, false),
   POISONING(301, 1, false),
   RES_POISONING(302, 1, true),
   FROST(303, 1, false),
   RES_FROST(304, 1, true),
   PETRIFICATION(305, 1, false),
   RES_PETRIFICATION(306, 1, false),
   PARALYSIS(307, 1, false),
   RES_PARALYSIS(308, 1, true),
   RESURRENTION(309, 1, false),
   RES_RESURRENTION(310, 1, true),
   DISARM(311, 1, false),
   RES_DISARM(312, 1, false),
   SACKED(313, 1, false),
   RES_SACKED(314, 1, false),
   ABSORB_MP(315, 1, false),
   RES_ABSORB_MP(316, 1, false),
   ABSORB_HP(317, 1, false),
   RES_ABSORB_HP(318, 1, false),
   ABSORB_AG(319, 1, false),
   RES_ABSORB_AG(320, 1, false),
   ABSORB_SD(321, 1, false),
   RES_ABSORB_SD(322, 1, false),
   INVINCIBLE(323, 1, false),
   WIND(325, 1, false),
   RES_WIND(326, 1, false),
   MONEY_ADD_WKM(501, 1, true),
   SKILL_PASSIVE_STAT(502, 1, false);

   private static HashMap allStatEnums = new HashMap();
   private static HashMap excellentEnums = new HashMap();
   private int statId;
   private String name;
   private int order;
   private boolean isPercent;
   private String des;
   private boolean changePrompt;

   public static StatEnum find(int statID) {
      StatEnum se = (StatEnum)allStatEnums.get(statID);
      return se == null ? None : se;
   }

   public static void init() {
      StatEnum[] var3;
      int var2 = (var3 = values()).length;

      for(int var1 = 0; var1 < var2; ++var1) {
         StatEnum stat = var3[var1];
         String[] message = StatNames.getMessage(stat.getStatId());
         stat.setName(message[0]);
         stat.setDes(message[1]);
         stat.setChangePrompt(Integer.parseInt(message[2]) == 1);
         if (Integer.parseInt(message[3]) == 1) {
            excellentEnums.put(stat, true);
            if (stat.getName().length() > VariableConstant.maxStatNameLength) {
               VariableConstant.maxStatNameLength = stat.getName().length();
            }
         }

         allStatEnums.put(stat.getStatId(), stat);
      }

   }

   public static ArrayList getStatAndNames() {
      ArrayList stats = new ArrayList();
      StatEnum[] var4;
      int var3 = (var4 = values()).length;

      for(int var2 = 0; var2 < var3; ++var2) {
         StatEnum stat = var4[var2];
         if (stat.getName() != null && stat.getName().length() > 0) {
            stats.add(stat);
         }
      }

      return stats;
   }

   public static boolean isExcellent(StatEnum stat) {
      return excellentEnums.containsKey(stat) ? ((Boolean)excellentEnums.get(stat)).booleanValue() : false;
   }

   private StatEnum(int statId, int order, boolean isPercent) {
      this.statId = statId;
      this.order = order;
      this.isPercent = isPercent;
   }

   public int getStatId() {
      return this.statId;
   }

   public void setStatId(int statId) {
      this.statId = statId;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getOrder() {
      return this.order;
   }

   public void setOrder(int order) {
      this.order = order;
   }

   public boolean isPercent() {
      return this.isPercent;
   }

   public void setPercent(boolean isPercent) {
      this.isPercent = isPercent;
   }

   public String getDes() {
      return this.des;
   }

   public void setDes(String des) {
      this.des = des;
   }

   public boolean isChangePrompt() {
      return this.changePrompt;
   }

   public void setChangePrompt(boolean changePrompt) {
      this.changePrompt = changePrompt;
   }
}
