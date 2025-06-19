package com.mu.game.model.equip.newStone;

import com.mu.game.model.equip.equipStat.EquipStat;
import com.mu.game.model.weight.WeightAtom;
import java.util.List;

public class StoneStatAtom extends WeightAtom {
   private EquipStat equipStat;

   public StoneStatAtom(EquipStat equipStat, int weight) {
      super(weight);
      this.equipStat = equipStat;
   }

   public static StoneStatAtom createStoneStarAtom(List statList, String keyStr, String str) throws Exception {
      String[] secSplits = str.split(",");
      if (secSplits.length < 2) {
         throw new Exception(keyStr + " - 宝石属性填写错误 ");
      } else {
         Integer statID = Integer.parseInt(secSplits[0]);
         EquipStat stat = EquipStat.getEquipStat(statID.intValue());
         if (stat == null) {
            throw new Exception(keyStr + " - 宝石属性不存在,statID = " + statID);
         } else if (statList.contains(stat)) {
            throw new Exception(keyStr + " - 宝石属性重复行");
         } else {
            int weight = Integer.parseInt(secSplits[1]);
            if (weight < 1) {
               throw new Exception(keyStr + " - 权重填写不正确");
            } else {
               statList.add(stat);
               StoneStatAtom atom = new StoneStatAtom(stat, weight);
               return atom;
            }
         }
      }
   }

   public EquipStat getEquipStat() {
      return this.equipStat;
   }

   public void setEquipStat(EquipStat equipStat) {
      this.equipStat = equipStat;
   }

   public int getStatID() {
      return this.equipStat.getId();
   }
}
