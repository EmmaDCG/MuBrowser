package com.mu.game.model.equip.compositenew;

import com.mu.game.model.fo.FunctionOpenManager;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.io.game.packet.imp.player.tips.SystemFunctionTip;
import com.mu.utils.Tools;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import jxl.Cell;
import jxl.Sheet;

public class CompositeGuideModel {
   public static final int guideType_Maya = 1;
   public static final int guideType_Wing = 2;
   private static HashMap guideMap = new HashMap();
   private static HashMap comIDtoType = new HashMap();
   private int type;
   private int proType;
   private String arrowDes;
   private String aKeyDes;
   private int itemModel;
   private int equipResource;
   private int iconResource;
   private int comSort;
   private int comType;
   private int comID;
   private HashMap needMap1;
   private HashMap needMap2;
   private int equipCount;
   private Item item = null;

   public CompositeGuideModel(int type, int proType, String arrowDes, String aKeyDes, int itemModel, int equipResource, int iconResource, int comSort, int comType, int comID, int equipCount) {
      this.type = type;
      this.proType = proType;
      this.arrowDes = arrowDes;
      this.aKeyDes = aKeyDes;
      this.itemModel = itemModel;
      this.equipResource = equipResource;
      this.iconResource = iconResource;
      this.comSort = comSort;
      this.comType = comType;
      this.comID = comID;
      this.equipCount = equipCount;
   }

   private static int createKey(int type, int proType) {
      return type * 100 + proType + 1;
   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int type = Tools.getCellIntValue(sheet.getCell("A" + i));
         int proType = Tools.getCellIntValue(sheet.getCell("B" + i));
         String arrowDes = Tools.getCellValue(sheet.getCell("C" + i));
         String aKeyDes = Tools.getCellValue(sheet.getCell("D" + i));
         int itemModel = Tools.getCellIntValue(sheet.getCell("E" + i));
         int equipResource = Tools.getCellIntValue(sheet.getCell("F" + i));
         int iconResource = Tools.getCellIntValue(sheet.getCell("G" + i));
         int comSort = Tools.getCellIntValue(sheet.getCell("H" + i));
         int comType = Tools.getCellIntValue(sheet.getCell("I" + i));
         int comID = Tools.getCellIntValue(sheet.getCell("J" + i));
         int needItem1 = Tools.getCellIntValue(sheet.getCell("K" + i));
         HashMap needMap2 = new HashMap();
         Cell cell = sheet.getCell("L" + i);
         if (cell != null && cell.getContents() != null && cell.getContents().length() > 0) {
            String itemStr = cell.getContents();
            String[] itemSplits = itemStr.split(",");
            String[] var21 = itemSplits;
            int var20 = itemSplits.length;

            for(int var19 = 0; var19 < var20; ++var19) {
               String s = var21[var19];
               needMap2.put(Integer.parseInt(s), Integer.valueOf(1));
            }
         }

         HashMap needMap1 = new HashMap();
         needMap1.put(needItem1, Integer.valueOf(1));
         int equipCount = Tools.getCellIntValue(sheet.getCell("M" + i));
         Item item = ItemTools.createItem(itemModel, 1, 2);
         if (item == null) {
            throw new Exception(sheet.getName() + " 道具模板填写错误 ，第 " + i + " 行");
         }

         if (CompositeModel.getModel(comID) == null) {
            throw new Exception(sheet.getName() + " , 合成小类填写错误，第 " + i + " 行");
         }

         CompositeGuideModel model = new CompositeGuideModel(type, proType, arrowDes, aKeyDes, itemModel, equipResource, iconResource, comSort, comType, comID, equipCount);
         model.setNeedMap1(needMap1);
         model.setNeedMap2(needMap2);
         model.setItem(item);
         guideMap.put(createKey(type, proType), model);
         comIDtoType.put(comID, type);
      }

   }

   public static CompositeGuideModel getGuideModel(int type, int proType) {
      int key = createKey(type, proType);
      return (CompositeGuideModel)guideMap.get(key);
   }

   public static int getGuideType(int comID) {
      return comIDtoType.containsKey(comID) ? ((Integer)comIDtoType.get(comID)).intValue() : -1;
   }

   public static boolean certainlySuccess(Player player, int comID) {
      int type = getGuideType(comID);
      if (type == -1) {
         return false;
      } else {
         switch(type) {
         case 1:
            if (!player.getArrowGuideManager().isFinishMayaWeapon()) {
               return true;
            }
            break;
         case 2:
            if (!player.getArrowGuideManager().isFinishComposeWine()) {
               return true;
            }
         }

         return false;
      }
   }

   public static void finishComposite(Player player, int comID) {
      int type = getGuideType(comID);
      if (type != -1) {
         switch(type) {
         case 1:
            player.getArrowGuideManager().finishMayaWeapon();
            break;
         case 2:
            player.getArrowGuideManager().finishComposeWine();
         }

      }
   }

   public static void doGuide(Player player, Item item) {
      if (FunctionOpenManager.isOpen(player, 1)) {
         switch(item.getItemSort()) {
         case 3:
         case 10:
         case 12:
         case 16:
         case 23:
            return;
         default:
            switch(item.getItemType()) {
            case 11:
            case 14:
            case 20:
            case 22:
            case 23:
            case 24:
               return;
            case 12:
            case 13:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 21:
            default:
               CompositeGuideModel guideModel = null;
               boolean doFlag = false;
               if (!player.getArrowGuideManager().isFinishMayaWeapon()) {
                  guideModel = getGuideModel(1, player.getProType());
                  doFlag = guideDetail(player, item, guideModel);
               }

               if (!doFlag) {
                  if (!player.getArrowGuideManager().isFinishComposeWine()) {
                     guideModel = getGuideModel(2, player.getProType());
                  }

                  guideDetail(player, item, guideModel);
               }

            }
         }
      }
   }

   private static boolean guideDetail(Player player, Item item, CompositeGuideModel guideModel) {
      if (guideModel == null) {
         return false;
      } else {
         boolean flag = false;
         if (guideModel.getNeedMap1().containsKey(item.getModelID())) {
            flag = true;
         } else if (!guideModel.getNeedMap2().isEmpty() && guideModel.getNeedMap2().containsKey(item.getModelID())) {
            flag = true;
         } else if (item.isEquipment() && item.getStarLevel() >= 4 && item.getZhuijiaLevel() >= 1) {
            flag = true;
         }

         if (!flag) {
            return false;
         } else {
            Iterator var5 = guideModel.getNeedMap1().keySet().iterator();

            while(var5.hasNext()) {
               Integer needItem1 = (Integer)var5.next();
               if (!player.getBackpack().hasEnoughItem(needItem1.intValue(), 1)) {
                  return false;
               }
            }

            long needItem2ID = -1L;
            Iterator var7 = guideModel.getNeedMap2().keySet().iterator();

            while(var7.hasNext()) {
               Integer needItem2 = (Integer)var7.next();
               Item tmpItem = player.getBackpack().getFirstItemByModelID(needItem2.intValue());
               if (tmpItem != null) {
                  needItem2ID = tmpItem.getID();
                  break;
               }
            }

            if (!guideModel.getNeedMap2().isEmpty() && needItem2ID == -1L) {
               return false;
            } else if (player.getBackpack().hasEnoughItem(needItem2ID, 4, 1, guideModel.getEquipCount())) {
               CompositeModel model = CompositeModel.getModel(guideModel.getComID());
               if (PlayerManager.hasEnoughMoney(player, model.getMoney())) {
                  SystemFunctionTip.sendToGuide(player, guideModel);
               }

               return true;
            } else {
               return false;
            }
         }
      }
   }

   public static void arrowComposite(Player player, int comID, List chooseItems, HashMap chooseMetris) {
      int type = getGuideType(comID);
      if (type != -1) {
         switch(type) {
         case 1:
            if (player.getArrowGuideManager().isFinishMayaWeapon()) {
               return;
            }
            break;
         case 2:
            if (player.getArrowGuideManager().isFinishComposeWine()) {
               return;
            }
            break;
         default:
            return;
         }

         CompositeGuideModel guideModel = getGuideModel(type, player.getProType());
         if (guideModel != null) {
            boolean flag = false;
            Iterator var8 = guideModel.getNeedMap1().keySet().iterator();

            do {
               if (!var8.hasNext()) {
                  CompositeModel model = CompositeModel.getModel(comID);
                  if (model == null) {
                     return;
                  }

                  Iterator var9 = model.getNeedMaterial().iterator();

                  MaterialModel material;
                  do {
                     if (!var9.hasNext()) {
                        player.getArrowGuideManager().pushArrow(6, guideModel.getArrowDes());
                        return;
                     }

                     material = (MaterialModel)var9.next();
                  } while(material.isAuto() || chooseMetris.containsKey(material.getMaterialID()));

                  return;
               }

               Integer needItem1 = (Integer)var8.next();
               if (player.getBackpack().hasEnoughItem(needItem1.intValue(), 1)) {
                  flag = true;
               }
            } while(flag);

         }
      }
   }

   public int getType() {
      return this.type;
   }

   public void setType(int type) {
      this.type = type;
   }

   public int getProType() {
      return this.proType;
   }

   public void setProType(int proType) {
      this.proType = proType;
   }

   public String getArrowDes() {
      return this.arrowDes;
   }

   public void setArrowDes(String arrowDes) {
      this.arrowDes = arrowDes;
   }

   public String getaKeyDes() {
      return this.aKeyDes;
   }

   public void setaKeyDes(String aKeyDes) {
      this.aKeyDes = aKeyDes;
   }

   public int getItemModel() {
      return this.itemModel;
   }

   public void setItemModel(int itemModel) {
      this.itemModel = itemModel;
   }

   public int getIconResource() {
      return this.iconResource;
   }

   public int getEquipResource() {
      return this.equipResource;
   }

   public void setEquipResource(int equipResource) {
      this.equipResource = equipResource;
   }

   public void setIconResource(int iconResource) {
      this.iconResource = iconResource;
   }

   public int getComSort() {
      return this.comSort;
   }

   public void setComSort(int comSort) {
      this.comSort = comSort;
   }

   public int getComType() {
      return this.comType;
   }

   public void setComType(int comType) {
      this.comType = comType;
   }

   public int getComID() {
      return this.comID;
   }

   public void setComID(int comID) {
      this.comID = comID;
   }

   public HashMap getNeedMap1() {
      return this.needMap1;
   }

   public void setNeedMap1(HashMap needMap1) {
      this.needMap1 = needMap1;
   }

   public HashMap getNeedMap2() {
      return this.needMap2;
   }

   public void setNeedMap2(HashMap needMap2) {
      this.needMap2 = needMap2;
   }

   public int getEquipCount() {
      return this.equipCount;
   }

   public void setEquipCount(int equipCount) {
      this.equipCount = equipCount;
   }

   public Item getItem() {
      return this.item;
   }

   public void setItem(Item item) {
      this.item = item;
   }

   public static HashMap getGuideMap() {
      return guideMap;
   }
}
