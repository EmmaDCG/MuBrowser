package com.mu.game.model.market;

import com.mu.config.MessageText;
import com.mu.db.manager.MarketDBManager;
import com.mu.game.CenterManager;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ShowItemManager;
import com.mu.game.model.mail.SendLocalServerMailTask;
import com.mu.game.model.mail.SendMailTask;
import com.mu.game.model.market.condition.ConditionAtom;
import com.mu.game.model.market.condition.MarketCondition;
import com.mu.game.model.market.record.MarketRecord;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.item.DeleteItem;
import com.mu.io.game.packet.imp.market.MarketRecords;
import com.mu.utils.Tools;
import com.mu.utils.concurrent.ThreadCachedPoolManager;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import jxl.Workbook;

public class MarketManager {
   private static Object obj = new Object();
   private static ConcurrentHashMap marketItemMap = Tools.newConcurrentHashMap2();
   private static ConcurrentHashMap sortItemMap = Tools.newConcurrentHashMap2();
   private static ConcurrentHashMap roleItems = Tools.newConcurrentHashMap2();
   private static HashMap records = new HashMap();

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      ConditionAtom.init(wb.getSheet(1));
      MarketCondition.init(wb.getSheet(2));
      MarketSort.init(wb.getSheet(3));
   }

   public static void checkExpired() {
      long now = System.currentTimeMillis();
      Iterator it = marketItemMap.values().iterator();

      while(it.hasNext()) {
         MarketItem item = (MarketItem)it.next();
         boolean expire = item.isExpire(now);
         if (expire) {
            removeMarketItem(item.getItemID());
            Player owner = CenterManager.getPlayerByRoleID(item.getRoleID());
            Item tmpItem = item.getItem().cloneItem(2);
            tmpItem.setContainerType(1);
            tmpItem.setMoneyType(1);
            ArrayList iList = new ArrayList();
            iList.add(item.getItem());
            if (owner != null) {
               SendMailTask.sendMail(owner, owner.getID(), MessageText.getText(16612), MessageText.getText(16614), iList);
               DeleteItem.sendToClient(owner, item.getItemID(), 11);
               iList.clear();
               iList = null;
            } else {
               SendLocalServerMailTask.sendMail(item.getRoleID(), MessageText.getText(16612), MessageText.getText(16613), iList);
            }
         }
      }

   }

   public static void addMarketItem(MarketItem marketItem) {
      Object var1 = obj;
      synchronized(obj) {
         marketItemMap.put(marketItem.getItemID(), marketItem);
         Iterator var3 = MarketSort.getSortMap().values().iterator();

         while(var3.hasNext()) {
            MarketSort sort = (MarketSort)var3.next();
            boolean canAdd = sort.addCheck(marketItem.getItem());
            if (canAdd) {
               ConcurrentHashMap sortItem = (ConcurrentHashMap)sortItemMap.get(sort.getSortID());
               if (sortItem == null) {
                  sortItem = Tools.newConcurrentHashMap2();
                  sortItemMap.put(sort.getSortID(), sortItem);
               }

               sortItem.put(marketItem.getItemID(), marketItem);
            }
         }

         ConcurrentHashMap roleMap = (ConcurrentHashMap)roleItems.get(marketItem.getRoleID());
         if (roleMap == null) {
            roleMap = Tools.newConcurrentHashMap2();
            roleItems.put(marketItem.getRoleID(), roleMap);
         }

         roleMap.put(marketItem.getItemID(), marketItem.getItem().getSlot());
      }
   }

   public static MarketItem removeMarketItem(final long marketItemID) {
      Object var2 = obj;
      synchronized(obj) {
         MarketItem marketItem = (MarketItem)marketItemMap.remove(marketItemID);
         Iterator it = sortItemMap.values().iterator();

         while(it.hasNext()) {
            ConcurrentHashMap sortItem = (ConcurrentHashMap)it.next();
            sortItem.remove(marketItemID);
         }

         if (marketItem != null) {
            ConcurrentHashMap roleMap = (ConcurrentHashMap)roleItems.get(marketItem.getRoleID());
            if (roleMap != null) {
               roleMap.remove(marketItemID);
               if (roleMap.size() < 1) {
                  roleItems.remove(marketItem.getRoleID());
               }
            }
         }

         if (marketItem != null) {
            ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
               public void run() {
                  MarketDBManager.delMarketItem(marketItemID);
               }
            });
         }

         return marketItem;
      }
   }

   public static List getShowItem(int sortID, List conList, String key) {
      List items = new ArrayList();
      ConcurrentHashMap sortItems = marketItemMap;
      if (sortID != -1) {
         sortItems = (ConcurrentHashMap)sortItemMap.get(sortID);
      }

      if (sortItems != null) {
         Iterator var6 = sortItems.values().iterator();

         while(var6.hasNext()) {
            MarketItem item = (MarketItem)var6.next();
            if (checkCondition(item, conList, key)) {
               items.add(item);
            }
         }
      }

      return items;
   }

   public static boolean checkCondition(MarketItem item, List conList, String key) {
      if (key.trim().length() > 0 && item.getItem().getName().indexOf(key) == -1) {
         return false;
      } else {
         Iterator var4 = conList.iterator();

         while(var4.hasNext()) {
            ConditionAtom condition = (ConditionAtom)var4.next();
            if (!condition.check(item.getItem())) {
               return false;
            }
         }

         return true;
      }
   }

   public static List getRoleItems(long roleID) {
      List itemList = new ArrayList();
      ConcurrentHashMap roleMap = (ConcurrentHashMap)roleItems.get(roleID);
      if (roleMap != null) {
         Iterator var5 = roleMap.keySet().iterator();

         while(var5.hasNext()) {
            Long itemID = (Long)var5.next();
            MarketItem mItem = getItem(itemID.longValue());
            if (mItem != null) {
               itemList.add(mItem.getItem());
            }
         }
      }

      return itemList;
   }

   public static int getRoleItemCount(long roleID) {
      ConcurrentHashMap roleMap = (ConcurrentHashMap)roleItems.get(roleID);
      return roleMap == null ? 0 : roleMap.size();
   }

   public static int getNextSlot(long roleID) {
      ConcurrentHashMap roleMap = (ConcurrentHashMap)roleItems.get(roleID);
      if (roleMap == null) {
         return 0;
      } else if (roleMap.size() >= 11) {
         return -1;
      } else {
         int slot = -1;

         for(int i = 0; i < 11; ++i) {
            if (!roleMap.containsValue(i)) {
               slot = i;
               break;
            }
         }

         return slot;
      }
   }

   public static MarketItem getItem(long itemID) {
      return (MarketItem)marketItemMap.get(itemID);
   }

   public static void addRecord(MarketItem mItem, Player buyer, int tax) {
      final MarketRecord record = MarketRecord.createRecord(mItem, buyer, tax);
      addRecord(record, buyer.getID());
      addRecord(record, mItem.getRoleID());
      ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
         public void run() {
            MarketDBManager.saveRecord(record);
         }
      });
   }

   private static void addRecord(MarketRecord record, long roleID) {
      List targetRecordList = (List)records.get(roleID);
      if (targetRecordList == null) {
         targetRecordList = new ArrayList();
         records.put(roleID, targetRecordList);
      }

      if (((List)targetRecordList).size() > 20) {
         MarketRecord firstRecord = (MarketRecord)((List)targetRecordList).remove(0);
         boolean canRemove = false;
         long checkRoleID = firstRecord.getBuyerID();
         if (firstRecord.getBuyerID() == roleID) {
            checkRoleID = firstRecord.getSalerID();
         }

         canRemove = !hasRecordItem(checkRoleID, firstRecord.getItem().getID());
         if (canRemove) {
            ShowItemManager.removeMarketItem(firstRecord.getItem().getID());
         }
      }

      ((List)targetRecordList).add(record);
      Player player = CenterManager.getPlayerByRoleID(roleID);
      if (player != null && !player.isDestroy()) {
         MarketRecords.sendToClient(player, record);
      }

   }

   public static List getRecordList(long roleID) {
      return (List)records.get(roleID);
   }

   public static boolean hasRecordItem(long roleID, long itemID) {
      List roleRecords = getRecordList(roleID);
      if (roleRecords == null) {
         return false;
      } else {
         Iterator var6 = roleRecords.iterator();

         while(var6.hasNext()) {
            MarketRecord record = (MarketRecord)var6.next();
            if (record.getItem().getID() == itemID) {
               return true;
            }
         }

         return false;
      }
   }
}
