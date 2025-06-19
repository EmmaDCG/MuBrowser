package com.mu.game.model.equip.newStone;

import com.mu.game.model.item.model.ItemSort;
import com.mu.game.model.service.StringTools;
import com.mu.game.model.stats.FinalModify;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import jxl.Sheet;

public class StoneSet {
   private static List setList = new ArrayList();
   private static List stoneTypeList = new ArrayList();
   private static final int typeCount = 7;
   private int id;
   private String title;
   private HashMap typeMap = null;
   private ArrayList statList = null;
   private String statStr = "";
   private int domineering = 0;

   public static void init(Sheet sheet) throws Exception {
      String stoneTypeStr = Tools.getCellValue(sheet.getCell("B1"));
      stoneTypeList = StringTools.analyzeIntegerList(stoneTypeStr, ",");
      if (stoneTypeList.size() != 7) {
         throw new Exception(sheet.getName() + " - 宝石类型顺序出错");
      } else {
         int rows = sheet.getRows();
         int i = 3;

         while(i <= rows) {
            int id = Tools.getCellIntValue(sheet.getCell("A" + i));
            String title = Tools.getCellValue(sheet.getCell("B" + i));
            String typeStr = Tools.getCellValue(sheet.getCell("C" + i));
            String statStr = Tools.getCellValue(sheet.getCell("D" + i));
            int domi = Tools.getCellIntValue(sheet.getCell("E" + i));
            String des = sheet.getName() + "第 " + i + " 行";
            HashMap typeMap = StringTools.analyzeIntegerMap(typeStr, ",");
            if (typeMap != null && typeMap.size() >= 1) {
               Iterator it = typeMap.entrySet().iterator();

               while(it.hasNext()) {
                  Entry entry = (Entry)it.next();
                  if (!ItemSort.hasItemType(((Integer)entry.getKey()).intValue())) {
                     throw new Exception(des + " - 宝石类型不存在");
                  }

                  if (((Integer)entry.getKey()).intValue() < 1) {
                     throw new Exception(des + "- 类型个数不正确");
                  }
               }

               ArrayList modifyList = StringTools.analyzeArrayAttris(statStr, ",");
               if (modifyList != null && modifyList.size() >= 1) {
                  if (domi < 0) {
                     throw new Exception(des + "--战斗力数据错误");
                  }

                  StoneSet ss = new StoneSet();
                  ss.setId(id);
                  ss.setTitle(title);
                  ss.setStatList(modifyList);
                  ss.setTypeMap(typeMap);
                  ss.setDomineering(domi);
                  setList.add(ss);
                  ++i;
                  continue;
               }

               throw new Exception(des + " - 奖励属性不正确");
            }

            throw new Exception(des + " - 宝石类型填写错误");
         }

      }
   }

   public int getDomineering() {
      return this.domineering;
   }

   public void setDomineering(int domineering) {
      this.domineering = domineering;
   }

   public static List getSetList() {
      return setList;
   }

   public boolean isEffect(HashMap actualMap) {
      Iterator it = this.typeMap.entrySet().iterator();

      Entry entry;
      int actualCount;
      do {
         if (!it.hasNext()) {
            return true;
         }

         entry = (Entry)it.next();
         actualCount = actualMap.containsKey(entry.getKey()) ? ((Integer)actualMap.get(entry.getKey())).intValue() : 0;
      } while(((Integer)entry.getValue()).intValue() <= actualCount);

      return false;
   }

   public static List getStoneTypeList() {
      return stoneTypeList;
   }

   public void setStatList(ArrayList statList) {
      this.statList = statList;
      StringBuffer sb = new StringBuffer();

      for(int i = 0; i < statList.size(); ++i) {
         FinalModify fm = (FinalModify)statList.get(i);
         sb.append(fm.getStat().getName() + " +" + fm.getShowValue() + fm.getSuffix());
         if (i != statList.size() - 1) {
            sb.append("#n:{4}");
         }
      }

      this.setStatStr(sb.toString());
   }

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getTitle() {
      return this.title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public HashMap getTypeMap() {
      return this.typeMap;
   }

   public void setTypeMap(HashMap typeMap) {
      this.typeMap = typeMap;
   }

   public ArrayList getStatList() {
      return this.statList;
   }

   public String getStatStr() {
      return this.statStr;
   }

   public void setStatStr(String statStr) {
      this.statStr = statStr;
   }
}
