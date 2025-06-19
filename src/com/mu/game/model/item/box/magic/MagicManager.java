package com.mu.game.model.item.box.magic;

import com.mu.config.MessageText;
import com.mu.db.log.IngotChangeType;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.item.operation.OperationResult;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.game.model.weight.WeightElement;
import com.mu.io.game.packet.imp.magicItem.GetMagicItemRecord;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import jxl.Sheet;

public class MagicManager {
   private static int everyIngot = 50;
   private static int ingotItemID = -1;
   private static SortedMap integralMap = new TreeMap();
   private static WeightElement weightItems = new WeightElement();
   private static List showItems = new ArrayList();

   public static void init(Sheet sheet) throws Exception {
      everyIngot = Tools.getCellIntValue(sheet.getCell("B1"));
      if (everyIngot < 1) {
         throw new Exception(sheet.getName() + " ：所需钻石数量不正确");
      } else {
         ingotItemID = Tools.getCellIntValue(sheet.getCell("B3"));
         if (!ItemModel.hasItemModel(ingotItemID)) {
            throw new Exception(sheet.getName() + "：代金券物品不存在");
         } else {
            String atomStr = Tools.getCellValue(sheet.getCell("B4"));
            String[] splits = atomStr.split(",");
            String[] var6 = splits;
            int times = splits.length;

            int i;
            for(i = 0; i < times; ++i) {
               String s = var6[i];
               Integer atomID = Integer.parseInt(s);
               MagicAtom atom = MagicAtom.getMagicAtom(atomID.intValue());
               if (atom == null) {
                  throw new Exception(sheet.getName() + "：魔盒元素物品不存在");
               }

               if (atom.isShow()) {
                  showItems.add(atom);
               }

               weightItems.addAtom(atom);
            }

            weightItems.sortByWeight(sheet.getName());
            if (showItems.size() >= 1 && weightItems.getMaxWeight() >= 1) {
               int rows = sheet.getRows();

               for(i = 6; i <= rows; ++i) {
                  times = Tools.getCellIntValue(sheet.getCell("A" + i));
                  int integral = Tools.getCellIntValue(sheet.getCell("B" + i));
                  if (times < 1 && integral < 1) {
                     throw new Exception(sheet.getName() + "-魔盒商城积分数据出错");
                  }

                  integralMap.put(times, integral);
               }

               if (integralMap.size() != 3) {
                  throw new Exception(sheet.getName() + "-魔盒商城积分数据长度不正确");
               }
            } else {
               throw new Exception(sheet.getName() + "：魔盒数据填写不正确");
            }
         }
      }
   }

   public static int canOpenMagicBox(Player player, int count) {
      if (count < 1) {
         return 3046;
      } else if (!hasInIntegral(count)) {
         return 3046;
      } else if (player.getTreasureHouse().getVacantSize() < count) {
         return 2011;
      } else {
         int ingotItemCount = player.getBackpack().getItemCount(ingotItemID);
         if (ingotItemCount >= count) {
            return 1;
         } else {
            int ingot = (count - ingotItemCount) * everyIngot;
            return player.getIngot() < ingot ? 1015 : 1;
         }
      }
   }

   public static List getItemData(Player player, int count) {
      List unitList = new ArrayList();

      for(int i = 0; i < count; ++i) {
         MagicAtom atom = (MagicAtom)weightItems.getRndAtom();
         ItemDataUnit dataUnit = atom.getRndItemData();
         unitList.add(dataUnit);
      }

      return unitList;
   }

   public static int getIndex(int atomID) {
      int index = -1;

      for(int i = 0; i < showItems.size(); ++i) {
         MagicAtom atom = (MagicAtom)showItems.get(i);
         if (atom.getID() == atomID) {
            index = i;
            break;
         }
      }

      return index;
   }

   public static List operateMagic(Player player, int count) {
      int result = canOpenMagicBox(player, count);
      if (result != 1) {
         SystemMessage.writeMessage(player, result);
         return null;
      } else {
         List indexList = new ArrayList();
         int ingotItemCount = player.getBackpack().getItemCount(ingotItemID);
         ingotItemCount = Math.min(ingotItemCount, count);
         int ingot = (count - ingotItemCount) * everyIngot;
         List recordList = null;

         int integral;
         for(integral = 0; integral < count; ++integral) {
            MagicAtom atom = (MagicAtom)weightItems.getRndAtom();
            ItemDataUnit unit = atom.getRndItemData();
            OperationResult or = player.getItemManager().addItem(unit);
            Item item = player.getTreasureHouse().getItemByID(or.getItemID());
            if (item != null && atom.isRecord()) {
               if (recordList == null) {
                  recordList = new ArrayList();
               }

               MagicRecord record = MagicRecord.addRecord(player, item, unit.getCount());
               recordList.add(record);
            }

            int index = getIndex(atom.getID());
            indexList.add(index);
         }

         if (recordList != null && recordList.size() > 0) {
            GetMagicItemRecord.sendToClient(recordList);
            recordList.clear();
            recordList = null;
         }

         if (ingot > 0) {
            PlayerManager.reduceIngot(player, ingot, IngotChangeType.OperateMaigcItem, "");
         }

         if (ingotItemCount > 0) {
            player.getItemManager().deleteItemByModel(ingotItemID, ingotItemCount, 36);
         }

         integral = getIntegral(count);
         if (integral > 0) {
            PlayerManager.addRedeemPoints(player, integral);
            SystemMessage.writeMessage(player, MessageText.getText(3062).replace("%s%", "" + integral), 3062);
         }

         return indexList;
      }
   }

   public static int getIntegral(int times) {
      return integralMap.containsKey(times) ? ((Integer)integralMap.get(times)).intValue() : 0;
   }

   public static boolean hasInIntegral(int times) {
      return integralMap.containsKey(times);
   }

   public static SortedMap getIntegralMap() {
      return integralMap;
   }

   public static List getShowItems() {
      return showItems;
   }

   public static int getEveryIngot() {
      return everyIngot;
   }

   public static int getIngotItemID() {
      return ingotItemID;
   }
}
