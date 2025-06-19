package com.mu.game.model.equip.equipStat;

import com.mu.config.VariableConstant;
import com.mu.game.model.stats.ItemModify;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.stats.StatModifyPriority;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import jxl.Sheet;

public class EquipStat extends ItemModify {
   private static HashMap deportMap = new HashMap();
   private static HashMap luckyStats = new HashMap();
   private static String luckyString = "";
   private static HashMap bonusTypeStats = new HashMap();
   private int Id;
   private int domineering = 0;

   private EquipStat(int id, StatEnum stat, int value, StatModifyPriority priority, int bonusType) {
      super(stat, value, priority, bonusType);
      this.Id = id;
   }

   public static void init(Sheet sheet) throws Exception {
      List statList = new ArrayList();
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int id = Tools.getCellIntValue(sheet.getCell("A" + i));
         StatEnum stat = StatEnum.find(Tools.getCellIntValue(sheet.getCell("B" + i)));
         int value = Tools.getCellIntValue(sheet.getCell("C" + i));
         StatModifyPriority priority = StatModifyPriority.fine(Tools.getCellIntValue(sheet.getCell("D" + i)));
         int bonusType = Tools.getCellIntValue(sheet.getCell("E" + i));
         int domineering = Tools.getCellIntValue(sheet.getCell("F" + i));
         if (stat == StatEnum.None) {
            throw new Exception("属性生成规则 - " + sheet.getName() + "-属性ID不存在，第" + i + "行");
         }

         if (value < 1) {
            throw new Exception("属性生成规则 -" + sheet.getName() + "-数值不正确，第" + i + "行");
         }

         if (priority == StatModifyPriority.NONE) {
            throw new Exception("属性生成规则-" + sheet.getName() + "-属性添加方式不正确，第" + i + "行");
         }

         EquipStat equipStat = new EquipStat(id, stat, value, priority, bonusType);
         equipStat.setDomineering(domineering);
         addEquipStat(equipStat);
         statList.add(equipStat);
      }

      Iterator var13 = deportMap.values().iterator();

      while(var13.hasNext()) {
         EquipStat stat = (EquipStat)var13.next();
         Iterator var15 = statList.iterator();

         while(var15.hasNext()) {
            EquipStat cStat = (EquipStat)var15.next();
            if (stat.getId() != cStat.getId() && stat.getBonusType() == cStat.getBonusType() && stat.getStat() == cStat.getStat() && stat.getValue() == cStat.getValue() && stat.getPriority() == cStat.getPriority()) {
               throw new Exception("装备属性相同 属性ID= {" + stat.getId() + "," + cStat.getId() + "}");
            }
         }
      }

      statList.clear();
      statList = null;
      if (luckyStats.size() < 1) {
         throw new Exception("装备生成规则 - 没有填写幸运属性");
      } else {
         Integer id;
         for(var13 = luckyStats.keySet().iterator(); var13.hasNext(); luckyString = id + (luckyString.trim().length() < 1 ? "" : ",") + luckyString) {
            id = (Integer)var13.next();
         }

      }
   }

   private static void addEquipStat(EquipStat stat) {
      deportMap.put(stat.getId(), stat);
      switch(stat.getBonusType()) {
      case 2:
         luckyStats.put(stat.getId(), stat);
         if (stat.getStat() == StatEnum.STRENGTH_LUCKY) {
            VariableConstant.Strength_Lucky = stat.getValue();
         } else if (stat.getStat() == StatEnum.ATK_LUCKY_RATE) {
            VariableConstant.Lucky_Attack = stat.getValue();
         }
      default:
         SortedMap typeStatMap = (SortedMap)bonusTypeStats.get(stat.getBonusType());
         if (typeStatMap == null) {
            typeStatMap = new TreeMap();
            bonusTypeStats.put(stat.getBonusType(), typeStatMap);
         }

         ((SortedMap)typeStatMap).put(stat.getId(), stat);
      }
   }

   public static EquipStat getEquipStat(int id) {
      return (EquipStat)deportMap.get(id);
   }

   public static boolean hasEquipStat(int id) {
      return deportMap.containsKey(id);
   }

   public static HashMap getLuckyStats() {
      return luckyStats;
   }

   public static boolean isLucky(int id) {
      return luckyStats.containsKey(id);
   }

   public static String getLuckyString() {
      return luckyString;
   }

   public static String getGMShowStr(int id) {
      EquipStat stat = getEquipStat(id);
      return stat == null ? "" : stat.getStat().getName() + "+" + stat.getShowValue() + stat.getSuffix();
   }

   public EquipStat cloneStat() {
      EquipStat stat = new EquipStat(this.getId(), this.getStat(), this.getValue(), this.getPriority(), this.getBonusType());
      stat.setDomineering(this.getDomineering());
      return stat;
   }

   public int getId() {
      return this.Id;
   }

   public void setId(int id) {
      this.Id = id;
   }

   public int getDomineering() {
      return this.domineering;
   }

   public void setDomineering(int domineering) {
      this.domineering = domineering;
   }
}
