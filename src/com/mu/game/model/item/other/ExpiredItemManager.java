package com.mu.game.model.item.other;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.other.expriedHandel.ExpiredHandel;
import com.mu.game.model.item.other.expriedHandel.imp.ExpiredHandNone;
import com.mu.game.model.item.other.expriedHandel.imp.ExpiredHandelAngelEquip;
import com.mu.game.model.item.other.expriedHandel.imp.ExpiredHandelPanda;
import com.mu.game.model.item.other.expriedHandel.imp.ExpiredHandelRenew;
import com.mu.game.model.unit.player.Player;
import com.mu.utils.Tools;
import java.util.HashMap;
import java.util.Iterator;
import jxl.Sheet;

public class ExpiredItemManager {
   public static final int Type_OpenRenewPanel = 1;
   public static final int Type_Panda = 2;
   public static final int Type_AngelEquip = 11;
   private static HashMap expiredHandelMap = new HashMap();
   private static HashMap angleEquipment = new HashMap();
   private static HashMap cantDelItemMap = new HashMap();
   private static HashMap shortcutMap = new HashMap();

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 3; i <= rows; ++i) {
         int itemModelID = Tools.getCellIntValue(sheet.getCell("A" + i));
         int doubleClickHandelType = Tools.getCellIntValue(sheet.getCell("B" + i));
         String handelStr = Tools.getCellValue(sheet.getCell("C" + i));
         String des = Tools.getCellValue(sheet.getCell("D" + i));
         ExpiredHandel handel = createHandelAtom(itemModelID, doubleClickHandelType, handelStr, des);
         if (handel == null) {
            throw new Exception(sheet.getName() + " - 第 " + i + "行,填写错误");
         }

         expiredHandelMap.put(handel.getModelID(), handel);
         cantDelItemMap.put(itemModelID, true);
      }

   }

   public static void initAngelEquip(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int itemModelID = Tools.getCellIntValue(sheet.getCell("A" + i));
         ExpiredHandel handel = createHandelAtom(itemModelID, 11, "", "");
         if (handel == null) {
            throw new Exception(sheet.getName() + " - 第 " + i + "行,填写错误");
         }

         expiredHandelMap.put(itemModelID, handel);
         angleEquipment.put(itemModelID, true);
      }

   }

   public static void initCheck() throws Exception {
      Iterator var1 = expiredHandelMap.values().iterator();

      while(var1.hasNext()) {
         ExpiredHandel handel = (ExpiredHandel)var1.next();
         handel.initCheck();
      }

   }

   public static void addShortcut(int id, int shortcutID) {
      shortcutMap.put(id, shortcutID);
   }

   public static ExpiredHandel createHandelAtom(int modelID, int type, String handelStr, String des) throws Exception {
      switch(type) {
      case -1:
         return new ExpiredHandNone(modelID, type);
      case 1:
         int shortcutBuyID = Integer.parseInt(handelStr);
         return new ExpiredHandelRenew(modelID, type, shortcutBuyID, des);
      case 2:
         return new ExpiredHandelPanda(modelID, type, handelStr, des);
      case 11:
         return new ExpiredHandelAngelEquip(modelID, type);
      default:
         return null;
      }
   }

   public static boolean cantAutoDel(int modelID) {
      return cantDelItemMap.containsKey(modelID);
   }

   public static boolean isAngelEquip(int modelID) {
      return angleEquipment.containsKey(modelID);
   }

   public static ExpiredHandel getHandelType(int modelID) {
      return (ExpiredHandel)expiredHandelMap.get(modelID);
   }

   public static int getShortcutID(int systemFunctionID) {
      int shortcutID = -1;
      if (shortcutMap.containsKey(systemFunctionID)) {
         shortcutID = ((Integer)shortcutMap.get(systemFunctionID)).intValue();
      }

      return shortcutID;
   }

   public static boolean handelDoubleClick(Player player, Item item) {
      ExpiredHandel handel = getHandelType(item.getModelID());
      return handel != null ? handel.doubleClickHandel(player, item) : false;
   }

   public static void handelInstantCheck(Player player, Item item, boolean firstEnterMap) {
      ExpiredHandel handel = getHandelType(item.getModelID());
      if (handel != null) {
         handel.instantCheckHandel(player, item, firstEnterMap);
      }

   }
}
