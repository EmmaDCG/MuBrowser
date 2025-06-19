package com.mu.game.model.service;

import com.mu.game.model.equip.equipStat.EquipStat;
import com.mu.game.model.item.ItemRune;
import com.mu.game.model.item.ItemStone;
import com.mu.game.model.stats.FinalModify;
import com.mu.game.model.stats.ItemModify;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.stats.StatModifyPriority;
import com.mu.game.model.unit.buff.model.BuffProps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringTools {
   private static Logger logger = LoggerFactory.getLogger(StringTools.class);

   public static HashSet analyzeIntegerHashset(String s, String splitStr) {
      HashSet sets = new HashSet();
      if (s != null && s.trim().length() > 0 && splitStr != null && splitStr.trim().length() >= 1) {
         String[] splists = s.split(splitStr);

         for(int i = 0; i < splists.length; ++i) {
            int inte = Integer.parseInt(splists[i]);
            sets.add(inte);
         }

         return sets;
      } else {
         return sets;
      }
   }

   public static List analyzeIntegerList(String s, String splitStr) {
      List list = new ArrayList();
      if (s != null && s.trim().length() > 0 && splitStr != null && splitStr.trim().length() >= 1) {
         String[] splists = s.split(splitStr);

         for(int i = 0; i < splists.length; ++i) {
            try {
               int inte = Integer.parseInt(splists[i]);
               list.add(inte);
            } catch (Exception var6) {
               var6.printStackTrace();
            }
         }

         return list;
      } else {
         return list;
      }
   }

   public static ArrayList analyzeArrayAttris(String s, String splitStr) {
      ArrayList statList = new ArrayList();
      if (s != null && s.trim().length() >= 1) {
         String[] splists = s.split(";");

         for(int i = 0; i < splists.length; ++i) {
            try {
               String[] secondSplit = splists[i].split(splitStr);
               if (secondSplit.length < 2) {
                  logger.error("解析字符串{}出错", splists[i]);
               } else {
                  StatEnum name = StatEnum.find(Integer.parseInt(secondSplit[0]));
                  int value = Integer.parseInt(secondSplit[1]);
                  StatModifyPriority priority = StatModifyPriority.ADD;
                  if (secondSplit.length > 2) {
                     priority = StatModifyPriority.fine(Integer.parseInt(secondSplit[2]));
                  }

                  FinalModify modify = new FinalModify(name, value, priority);
                  statList.add(modify);
               }
            } catch (Exception var10) {
               var10.printStackTrace();
            }
         }

         return statList;
      } else {
         return statList;
      }
   }

   public static ArrayList analyzeItemBasicModifies(String s) {
      ArrayList statList = new ArrayList();
      if (s != null && s.trim().length() >= 1) {
         String[] splists = s.split(";");

         for(int i = 0; i < splists.length; ++i) {
            try {
               String[] secondSplit = splists[i].split(",");
               if (secondSplit.length < 3) {
                  logger.error("基础数据出错--" + s);
               } else {
                  StatEnum name = StatEnum.find(Integer.parseInt(secondSplit[0]));
                  int value = Integer.parseInt(secondSplit[1]);
                  StatModifyPriority priority = StatModifyPriority.fine(Integer.parseInt(secondSplit[2]));
                  int bonusType = 0;
                  if (secondSplit.length > 3) {
                     bonusType = Integer.parseInt(secondSplit[3]);
                  }

                  ItemModify modify = new ItemModify(name, value, priority, bonusType);
                  statList.add(modify);
               }
            } catch (Exception var10) {
               var10.printStackTrace();
            }
         }

         return statList;
      } else {
         return statList;
      }
   }

   public static SortedMap analyzeItemEquipStat(String s) {
      SortedMap statMap = new TreeMap();
      if (s != null && s.trim().length() >= 1) {
         String[] splits = s.trim().split(",");
         String[] var6 = splits;
         int var5 = splits.length;

         for(int var4 = 0; var4 < var5; ++var4) {
            String split = var6[var4];

            try {
               int id = Integer.parseInt(split);
               EquipStat stat = EquipStat.getEquipStat(id);
               if (stat == null) {
                  logger.error("装备属性ID不存在 " + s);
               } else {
                  statMap.put(stat.getId(), stat.cloneStat());
               }
            } catch (Exception var9) {
               var9.printStackTrace();
            }
         }

         return statMap;
      } else {
         return statMap;
      }
   }

   public static String getModifyString(List slist) {
      StringBuffer sb = new StringBuffer();
      if (slist != null && slist.size() != 0) {
         Iterator var3 = slist.iterator();

         while(var3.hasNext()) {
            ItemModify sm = (ItemModify)var3.next();
            sb.append(sm.getStat().getStatId());
            sb.append(",");
            sb.append(sm.getValue());
            sb.append(",");
            sb.append(sm.getPriority().getPriority());
            sb.append(",");
            sb.append(sm.getBonusType());
            sb.append(";");
         }

         sb.delete(sb.length() - 1, sb.length());
         return sb.toString();
      } else {
         return "";
      }
   }

   public static String getShowModifyString(List fmList) {
      StringBuffer sb = new StringBuffer();
      if (fmList != null && fmList.size() != 0) {
         Iterator var3 = fmList.iterator();

         while(var3.hasNext()) {
            FinalModify sm = (FinalModify)var3.next();
            sb.append(sm.getStat().getName());
            sb.append(" + ");
            sb.append(sm.getShowValue());
            sb.append(sm.getSuffix());
            sb.append(";");
         }

         sb.delete(sb.length() - 1, sb.length());
         return sb.toString();
      } else {
         return "";
      }
   }

   public static String getEquipStatString(SortedMap equipStats) {
      StringBuffer sb = new StringBuffer();
      if (equipStats != null && equipStats.size() != 0) {
         Iterator var3 = equipStats.values().iterator();

         while(var3.hasNext()) {
            EquipStat es = (EquipStat)var3.next();
            sb.append(es.getId());
            sb.append(",");
         }

         sb.delete(sb.length() - 1, sb.length());
         return sb.toString();
      } else {
         return "";
      }
   }

   public static String getStoneStr(List stones) {
      StringBuffer sb = new StringBuffer();

      try {
         if (stones == null || stones.size() < 1) {
            return sb.toString();
         }

         Iterator var3 = stones.iterator();

         while(var3.hasNext()) {
            ItemStone stone = (ItemStone)var3.next();
            sb.append(stone.getModelID());
            sb.append(",");
            sb.append(stone.getEquipStatID());
            sb.append(";");
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      if (sb.length() > 0) {
         sb.delete(sb.length() - 1, sb.length());
      }

      return sb.toString();
   }

   public static String getRuneStr(List runes) {
      StringBuffer sb = new StringBuffer();

      try {
         if (runes == null || runes.size() < 1) {
            return sb.toString();
         }

         Iterator var3 = runes.iterator();

         while(var3.hasNext()) {
            ItemRune rune = (ItemRune)var3.next();
            sb.append(rune.getModelID());
            sb.append(";");
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      if (sb.length() > 0) {
         sb.delete(sb.length() - 1, sb.length());
      }

      return sb.toString();
   }

   public static List analyzeIntegerStatenum(String s, String splitStr) {
      List statList = new ArrayList();
      if (s != null && s.trim().length() > 0 && splitStr != null && splitStr.trim().length() >= 1) {
         String[] splists = s.split(splitStr);

         for(int i = 0; i < splists.length; ++i) {
            try {
               int inte = Integer.parseInt(splists[i]);
               statList.add(StatEnum.find(inte));
            } catch (Exception var6) {
               var6.printStackTrace();
            }
         }

         return statList;
      } else {
         return statList;
      }
   }

   public static HashMap analyzeIntegerMap(String s, String splitStr) {
      HashMap map = new HashMap();
      if (s != null && s.trim().length() > 0 && splitStr != null && splitStr.trim().length() >= 1) {
         String[] splists = s.split(";");

         for(int i = 0; i < splists.length; ++i) {
            try {
               String[] secondSplit = splists[i].split(splitStr);
               if (secondSplit.length < 2) {
                  logger.error("数据出错");
               } else {
                  int key = Integer.parseInt(secondSplit[0]);
                  int value = Integer.parseInt(secondSplit[1]);
                  map.put(key, value);
               }
            } catch (Exception var8) {
               var8.printStackTrace();
            }
         }

         return map;
      } else {
         return map;
      }
   }

   public static List analyzeBuffProps(String s) {
      List propList = new ArrayList();
      if (s != null && s.trim().length() >= 1) {
         String[] splists = s.split(";");

         for(int i = 0; i < splists.length; ++i) {
            try {
               String[] secondSplit = splists[i].split(",");
               if (secondSplit.length < 5) {
                  logger.error("Buff数据出错");
               } else {
                  StatEnum stat = StatEnum.find(Integer.parseInt(secondSplit[0]));
                  int value = Integer.parseInt(secondSplit[1]);
                  int maxValue = Integer.parseInt(secondSplit[2]);
                  StatModifyPriority priority = StatModifyPriority.fine(Integer.parseInt(secondSplit[3]));
                  int strCoeff;
                  if (value > 0 && value > maxValue || value < 0 && value < maxValue) {
                     strCoeff = value;
                     value = maxValue;
                     maxValue = strCoeff;
                  }

                  strCoeff = Integer.parseInt(secondSplit[4]);
                  BuffProps prop = new BuffProps(stat, value, maxValue, priority, strCoeff);
                  propList.add(prop);
               }
            } catch (Exception var11) {
               var11.printStackTrace();
            }
         }

         return propList;
      } else {
         return propList;
      }
   }

   public static String getPropStr(List ps) {
      StringBuffer sb = new StringBuffer();
      if (ps != null && ps.size() > 0) {
         Iterator var3 = ps.iterator();

         while(var3.hasNext()) {
            BuffProps prop = (BuffProps)var3.next();
            sb.append(prop.getStat().getStatId());
            sb.append(",");
            sb.append(prop.getValue());
            sb.append(",");
            sb.append(prop.getMaxValue());
            sb.append(",");
            sb.append(prop.getPriority().getPriority());
            sb.append(",");
            sb.append(prop.getStrCoeff());
            sb.append(";");
         }

         sb.delete(sb.length() - 1, sb.length());
      }

      return sb.toString();
   }
}
