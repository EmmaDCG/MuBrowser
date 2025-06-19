package com.mu.game.model.equip.compositenew;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.service.StringTools;
import com.mu.utils.Rnd;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import jxl.Sheet;

public class CompositeModel {
   private static HashMap modelMap = new HashMap();
   private static SortedMap labelMap = new TreeMap();
   private static HashMap broadcastMap = new HashMap();
   private int comID;
   private int sortID;
   private int typeID;
   private String name;
   private List materialList = null;
   private List needMaterial = null;
   private List chooseMaterial = null;
   private List showItemList = null;
   private List targetItemList = null;
   private String title;
   private String materialDes;
   private int basicRate;
   private int maxRate;
   private int ingot;
   private int money;
   private int openLevel;
   private int statRuleID = -1;
   private String broadcast = null;

   public CompositeModel(int comID, int sortID, int typeID) {
      this.comID = comID;
      this.sortID = sortID;
      this.typeID = typeID;
   }

   public static void initBroadcast(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int ID = Tools.getCellIntValue(sheet.getCell("A" + i));
         String str = Tools.getCellValue(sheet.getCell("B" + i));
         broadcastMap.put(ID, str);
      }

   }

   public static void initModel(Sheet sheet) throws Exception {
      int rows = sheet.getRows();
      int i = 2;

      while(i <= rows) {
         int comID = Tools.getCellIntValue(sheet.getCell("A" + i));
         int sortID = Tools.getCellIntValue(sheet.getCell("B" + i));
         int typeID = Tools.getCellIntValue(sheet.getCell("C" + i));
         String name = Tools.getCellValue(sheet.getCell("D" + i));
         String materialStr = Tools.getCellValue(sheet.getCell("E" + i));
         String showStr = Tools.getCellValue(sheet.getCell("F" + i));
         String title = sheet.getCell("G" + i).getContents();
         String materialDes = Tools.getCellValue(sheet.getCell("H" + i));
         int basicRate = Tools.getCellIntValue(sheet.getCell("I" + i));
         int maxRate = Tools.getCellIntValue(sheet.getCell("J" + i));
         int ingot = Tools.getCellIntValue(sheet.getCell("K" + i));
         int money = Tools.getCellIntValue(sheet.getCell("L" + i));
         int openLevel = Tools.getCellIntValue(sheet.getCell("M" + i));
         int statRuleID = Tools.getCellIntValue(sheet.getCell("N" + i));
         String targetStr = Tools.getCellValue(sheet.getCell("O" + i));
         String broadcast = null;
         int broadcastID = Tools.getCellIntValue(sheet.getCell("P" + i));
         broadcast = (String)broadcastMap.get(broadcastID);
         if (CompositLabel.hasLabel(sortID) && CompositLabel.hasLabel(typeID)) {
            List materialSet = StringTools.analyzeIntegerList(materialStr, ",");
            HashSet mSet = new HashSet();
            mSet.addAll(materialSet);
            if (materialSet.size() >= 1 && mSet.size() == materialSet.size()) {
               mSet.clear();
               mSet = null;
               List materialList = new ArrayList();
               List needMaterial = new ArrayList();
               Iterator var25 = materialSet.iterator();

               while(var25.hasNext()) {
                  Integer materialID = (Integer)var25.next();
                  MaterialModel mm = MaterialModel.getMaterialModel(materialID.intValue());
                  if (mm == null) {
                     throw new Exception(sheet.getName() + "--填写的合成材料不存在,第 " + i + "行");
                  }

                  materialList.add(mm);
                  if (mm.isNeed()) {
                     needMaterial.add(mm);
                  }
               }

               if (needMaterial.size() < 1) {
                  throw new Exception(sheet.getName() + "--没有必须材料,第 " + i + "行");
               }

               List showItemList = new ArrayList();
               String[] showSplits = showStr.split(",");
               String[] var29 = showSplits;
               int var28 = showSplits.length;

               String des;
               for(int var27 = 0; var27 < var28; ++var27) {
                  des = var29[var27];
                  Integer itemModelID = Integer.parseInt(des);
                  if (containsShow(showItemList, itemModelID.intValue())) {
                     throw new Exception(sheet.getName() + " -- 显示道具重复填写 ，第 " + i + " 行");
                  }

                  ItemDataUnit unit = new ItemDataUnit(itemModelID.intValue(), 1);
                  unit.setHide(true);
                  unit.setStatRuleID(statRuleID);
                  Item item = ItemTools.createItem(2, unit);
                  if (item == null) {
                     throw new Exception(sheet.getName() + "--显示道具不存在,第 " + i + "行");
                  }

                  showItemList.add(item);
               }

               if (showItemList.size() < 1) {
                  throw new Exception(sheet.getName() + "--没有显示道具,第 " + i + "行");
               }

               if (basicRate >= 0 && maxRate >= basicRate && maxRate >= 1) {
                  if (money >= 0 && ingot >= 0) {
                     des = "第 " + i + " 行";
                     List targetItemList = new ArrayList();
                     String[] targetSplits = targetStr.split(";");
                     String[] var47 = targetSplits;
                     int var46 = targetSplits.length;

                     for(int var44 = 0; var44 < var46; ++var44) {
                        String ts = var47[var44];
                        String[] sSplits = ts.split(",");
                        if (sSplits.length < 2) {
                           throw new Exception(sheet.getName() + "-目标道具格式填写错误 " + des);
                        }

                        Integer itemModelId = Integer.parseInt(sSplits[0]);
                        if (ItemModel.getModel(itemModelId.intValue()) == null) {
                           throw new Exception(sheet.getName() + "-目标道具不存在 ，" + des);
                        }

                        if (containsTarget(targetItemList, itemModelId.intValue())) {
                           throw new Exception(sheet.getName() + "-目标道具填写重复， " + des);
                        }

                        Integer index = Integer.parseInt(sSplits[1]);
                        index = index.intValue() - 1;
                        if (index.intValue() > showItemList.size() - 1 || index.intValue() < 0) {
                           throw new Exception(sheet.getName() + "-目标道具所指向的显示道具 位置不正确，" + des);
                        }

                        int[] targetItem = new int[]{itemModelId.intValue(), index.intValue()};
                        targetItemList.add(targetItem);
                     }

                     if (targetItemList.size() < 1) {
                        throw new Exception(sheet.getName() + " -- 目标道具没有填写，" + des);
                     }

                     CompositeModel model = new CompositeModel(comID, sortID, typeID);
                     model.setName(name);
                     boolean flag = model.setMaterialList(materialList);
                     if (!flag) {
                        throw new Exception(sheet.getName() + "--系统自动放入的道具重复,第 " + i + "行");
                     }

                     model.setNeedMaterial(needMaterial);
                     model.setShowItemList(showItemList);
                     model.setTitle(title);
                     model.setMaterialDes(materialDes);
                     model.setBasicRate(basicRate);
                     model.setMaxRate(maxRate);
                     model.setIngot(ingot);
                     model.setMoney(money);
                     model.setOpenLevel(openLevel);
                     model.setStatRuleID(statRuleID);
                     model.setTargetItemList(targetItemList);
                     model.setBroadcast(broadcast);
                     addCompositeModel(model);
                     ++i;
                     continue;
                  }

                  throw new Exception(sheet.getName() + "--金钱或者钻石填写不正确,第 " + i + "行");
               }

               throw new Exception(sheet.getName() + "--概率填写不正确,第 " + i + "行");
            }

            throw new Exception(sheet.getName() + "--没有填写需要的材料 或 有相同的材料ID,第 " + i + "行");
         }

         throw new Exception(sheet.getName() + "--合成标签不存在,第 " + i + "行");
      }

   }

   private static boolean containsShow(List itemList, int modelID) {
      Iterator var3 = itemList.iterator();

      while(var3.hasNext()) {
         Item item = (Item)var3.next();
         if (item.getModelID() == modelID) {
            return true;
         }
      }

      return false;
   }

   private static boolean containsTarget(List itemList, int modelID) {
      Iterator var3 = itemList.iterator();

      while(var3.hasNext()) {
         int[] target = (int[])var3.next();
         if (target[0] == modelID) {
            return true;
         }
      }

      return false;
   }

   public static void addCompositeModel(CompositeModel model) {
      modelMap.put(model.getComID(), model);
      int fLabelID = model.getSortID();
      int secLabelID = model.getTypeID();
      SortedMap secMap = (SortedMap)labelMap.get(fLabelID);
      if (secMap == null) {
         secMap = new TreeMap();
         labelMap.put(fLabelID, secMap);
      }

      List comIDList = (List)((SortedMap)secMap).get(secLabelID);
      if (comIDList == null) {
         comIDList = new ArrayList();
         ((SortedMap)secMap).put(secLabelID, comIDList);
      }

      ((List)comIDList).add(model.getComID());
   }

   public static CompositeModel getModel(int comID) {
      return (CompositeModel)modelMap.get(comID);
   }

   public static SortedMap getLabelMap() {
      return labelMap;
   }

   public int[] getRndTarget() {
      return (int[])this.targetItemList.get(Rnd.get(0, this.targetItemList.size() - 1));
   }

   public ItemDataUnit getUnit(int count, boolean bind, int itemModelID) {
      ItemDataUnit unit = new ItemDataUnit(itemModelID, count, bind);
      unit.setSource(3);
      unit.setStatRuleID(this.statRuleID);
      return unit;
   }

   public boolean hasChooseMaterial(int materialID) {
      Iterator var3 = this.chooseMaterial.iterator();

      while(var3.hasNext()) {
         MaterialModel mm = (MaterialModel)var3.next();
         if (mm.getMaterialID() == materialID) {
            return true;
         }
      }

      return false;
   }

   public boolean setMaterialList(List materialList) throws Exception {
      this.materialList = materialList;
      this.chooseMaterial = new ArrayList();
      List autoList = new ArrayList();
      Iterator var4 = materialList.iterator();

      while(var4.hasNext()) {
         MaterialModel mm = (MaterialModel)var4.next();
         if (!mm.isAuto()) {
            this.chooseMaterial.add(mm);
         } else {
            autoList.add(mm);
         }
      }

      for(int i = 0; i < autoList.size(); ++i) {
         MaterialModel mm = (MaterialModel)autoList.get(i);

         for(int j = i + 1; j < autoList.size(); ++j) {
            MaterialModel tmpModel = (MaterialModel)autoList.get(j);
            if (mm.getShowItem().getModelID() == tmpModel.getShowItem().getModelID()) {
               return false;
            }
         }
      }

      return true;
   }

   public HashMap getAutoConsume() {
      HashMap consumeMap = new HashMap();
      Iterator var3 = this.getNeedMaterial().iterator();

      while(var3.hasNext()) {
         MaterialModel mm = (MaterialModel)var3.next();
         if (mm.isAuto()) {
            consumeMap.put(mm.getShowItem().getModelID(), mm.getCount());
         }
      }

      return consumeMap;
   }

   public List getTargetItemList() {
      return this.targetItemList;
   }

   public void setTargetItemList(List targetItemList) {
      this.targetItemList = targetItemList;
   }

   public int getComID() {
      return this.comID;
   }

   public void setComID(int comID) {
      this.comID = comID;
   }

   public int getSortID() {
      return this.sortID;
   }

   public void setSortID(int sortID) {
      this.sortID = sortID;
   }

   public int getTypeID() {
      return this.typeID;
   }

   public void setTypeID(int typeID) {
      this.typeID = typeID;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public List getMaterialList() {
      return this.materialList;
   }

   public List getChooseMaterial() {
      return this.chooseMaterial;
   }

   public List getNeedMaterial() {
      return this.needMaterial;
   }

   public void setNeedMaterial(List needMaterial) {
      this.needMaterial = needMaterial;
   }

   public List getShowItemList() {
      return this.showItemList;
   }

   public void setShowItemList(List showItemList) {
      this.showItemList = showItemList;
   }

   public String getTitle() {
      return this.title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getMaterialDes() {
      return this.materialDes;
   }

   public void setMaterialDes(String materialDes) {
      this.materialDes = materialDes;
   }

   public int getBasicRate() {
      return this.basicRate;
   }

   public void setBasicRate(int basicRate) {
      this.basicRate = basicRate;
   }

   public int getMaxRate() {
      return this.maxRate;
   }

   public void setMaxRate(int maxRate) {
      this.maxRate = maxRate;
   }

   public int getIngot() {
      return this.ingot;
   }

   public void setIngot(int ingot) {
      this.ingot = ingot;
   }

   public int getMoney() {
      return this.money;
   }

   public void setMoney(int money) {
      this.money = money;
   }

   public int getOpenLevel() {
      return this.openLevel;
   }

   public void setOpenLevel(int openLevel) {
      this.openLevel = openLevel;
   }

   public int getStatRuleID() {
      return this.statRuleID;
   }

   public void setStatRuleID(int statRuleID) {
      this.statRuleID = statRuleID;
   }

   public String getBroadcast() {
      return this.broadcast;
   }

   public void setBroadcast(String broadcast) {
      this.broadcast = broadcast;
   }
}
