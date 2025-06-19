package com.mu.game.model.item.container.imp;

import com.mu.game.dungeon.DungeonManager;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.item.container.Container;
import com.mu.game.model.packet.ItemPacketService;
import com.mu.game.model.spiritOfWar.SpiritTools;
import com.mu.game.model.spiritOfWar.refine.RefineData;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.hang.HangSale;
import com.mu.io.game.packet.imp.equip.RequestForgingEquip;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Storage extends Container {
   private static Logger logger = LoggerFactory.getLogger(Storage.class);
   private int type = 1;
   private int limit;
   private Item[] itemStorage = null;
   private HashMap itemMap = null;
   private int page = 1;

   public Storage(int type, int page) {
      this.type = type;
      this.setPage(page);
      this.itemStorage = new Item[this.limit];
      this.itemMap = new HashMap();
   }

   public void reset(int page) {
      this.setPage(page);
      this.itemStorage = new Item[this.limit];
      this.itemMap.clear();
   }

   public void loadItem(Item item) {
      int slot = item.getSlot();
      if (!this.effectiveSlot(slot)) {
         logger.error("物品位置不正确 {},{}", item.getID(), slot);
      } else {
         this.addToGrid(item, slot);
      }
   }

   private void setPage(int page) {
      int maxPage = this.getMaxPage();
      int minPage = this.getDefaultPage();
      if (page > maxPage) {
         page = maxPage;
      }

      if (page < minPage) {
         page = minPage;
      }

      this.page = page;
      this.limit = this.page * this.getAddtionCount();
   }

   protected int getMaxCount() {
      return this.getType() == 1 ? 98 : 324;
   }

   public int getDefaultCount() {
      return this.getType() == 1 ? 49 : 81;
   }

   protected int getAddtionCount() {
      return this.getType() == 1 ? 1 : 81;
   }

   protected int getDefaultPage() {
      switch(this.getType()) {
      case 4:
         return 1;
      default:
         return 49;
      }
   }

   protected int getMaxPage() {
      switch(this.getType()) {
      case 4:
         return 4;
      default:
         return 98;
      }
   }

   private void addToGrid(Item item, int slot) {
      if (!this.effectiveSlot(slot)) {
         logger.error("物品位置不正确，{}，{}，{}", new Object[]{item.getID(), item.getModelID(), slot});
      } else {
         Item tmpItem = this.itemStorage[slot];
         if (tmpItem != null) {
            this.itemMap.remove(tmpItem.getID());
         }

         this.itemStorage[slot] = item;
         this.itemMap.put(item.getID(), item);
         item.setMoneyType(1);
      }
   }

   public List getItemsByModelID(int modelID) {
      List itemList = new ArrayList();
      Item[] var6 = this.itemStorage;
      int var5 = this.itemStorage.length;

      for(int var4 = 0; var4 < var5; ++var4) {
         Item item = var6[var4];
         if (item != null && item.getModelID() == modelID) {
            itemList.add(item);
         }
      }

      return itemList;
   }

   public List getItemsByModelID(int modelID, boolean isBind) {
      List itemList = new ArrayList();
      Item[] var7 = this.itemStorage;
      int var6 = this.itemStorage.length;

      for(int var5 = 0; var5 < var6; ++var5) {
         Item item = var7[var5];
         if (item != null && item.getModelID() == modelID && item.isBind() == isBind) {
            itemList.add(item);
         }
      }

      return itemList;
   }

   public int getItemCountByModelId(int modelID, boolean isBind) {
      int count = 0;
      Item[] var7 = this.itemStorage;
      int var6 = this.itemStorage.length;

      for(int var5 = 0; var5 < var6; ++var5) {
         Item item = var7[var5];
         if (item != null && item.getModelID() == modelID && item.isBind() == isBind) {
            count += item.getCount();
         }
      }

      return count;
   }

   public int getRemainStackCount(int modelID, boolean isBind, long expireTime) {
      int freeCount = 0;
      Item[] var9 = this.itemStorage;
      int var8 = this.itemStorage.length;

      for(int var7 = 0; var7 < var8; ++var7) {
         Item item = var9[var7];
         if (item != null && item.getModelID() == modelID && item.isBind() == isBind && item.getExpireTime() == expireTime) {
            freeCount += item.getModel().getMaxStackCount() - item.getCount();
         }
      }

      return freeCount;
   }

   public Item getFirstItemByModelID(int modelID) {
      Item item = null;
      Item[] var6 = this.itemStorage;
      int var5 = this.itemStorage.length;

      for(int var4 = 0; var4 < var5; ++var4) {
         Item tmpItem = var6[var4];
         if (tmpItem != null && tmpItem.getModelID() == modelID) {
            item = tmpItem;
            break;
         }
      }

      return item;
   }

   public int getItemCount(int modelID) {
      int count = 0;
      Item[] var6 = this.itemStorage;
      int var5 = this.itemStorage.length;

      for(int var4 = 0; var4 < var5; ++var4) {
         Item item = var6[var4];
         if (item != null && item.getModelID() == modelID) {
            count += item.getCount();
         }
      }

      return count;
   }

   public boolean hasEnoughItem(int modelID, int count) {
      int actualCount = this.getItemCount(modelID);
      return actualCount >= count;
   }

   public boolean hasItem(long objId) {
      return this.itemMap.containsKey(objId);
   }

   public Item getItemByID(long objId) {
      return (Item)this.itemMap.get(objId);
   }

   public Item getItemBySlot(int slot) {
      return !this.effectiveSlot(slot) ? null : this.itemStorage[slot];
   }

   public int getSlotById(long objId) {
      Item item = this.getItemByID(objId);
      return item != null ? item.getSlot() : -1;
   }

   public ArrayList getAllItems() {
      ArrayList itemList = new ArrayList();
      Item[] var2 = this.itemStorage;
      synchronized(this.itemStorage) {
         Item[] var6 = this.itemStorage;
         int var5 = this.itemStorage.length;

         for(int var4 = 0; var4 < var5; ++var4) {
            Item item = var6[var4];
            if (item != null) {
               itemList.add(item);
            }
         }

         return itemList;
      }
   }

   public void filterForgingItem(List itemList, int forgingType) {
      Item[] var6 = this.itemStorage;
      int var5 = this.itemStorage.length;

      for(int var4 = 0; var4 < var5; ++var4) {
         Item item = var6[var4];
         if (item != null && RequestForgingEquip.filter(item, forgingType)) {
            itemList.add(item);
         }
      }

   }

   public ArrayList getItemsByBagIndex(int index) {
      ArrayList items = new ArrayList();
      if (!this.effectiveBagIndex(index)) {
         return items;
      } else {
         Item[] var3 = this.itemStorage;
         synchronized(this.itemStorage) {
            int first = index * this.getAddtionCount();

            for(int i = first; i < first + this.getAddtionCount(); ++i) {
               if (this.itemStorage[i] != null) {
                  items.add(this.itemStorage[i]);
               }
            }

            return items;
         }
      }
   }

   public HashMap getModelsByItemType(int type) {
      HashMap itemModels = new HashMap();
      Item[] var6 = this.itemStorage;
      int var5 = this.itemStorage.length;

      for(int var4 = 0; var4 < var5; ++var4) {
         Item item = var6[var4];
         if (item != null && item.getItemType() == type) {
            if (itemModels.containsKey(item.getModelID())) {
               itemModels.put(item.getModelID(), item.getCount() + ((Integer)itemModels.get(item.getModelID())).intValue());
            } else {
               itemModels.put(item.getModelID(), item.getCount());
            }
         }
      }

      return itemModels;
   }

   public boolean hasItemBySort(int sort) {
      Item[] var5 = this.itemStorage;
      int var4 = this.itemStorage.length;

      for(int var3 = 0; var3 < var4; ++var3) {
         Item item = var5[var3];
         if (item != null && item.getItemSort() == sort) {
            return true;
         }
      }

      return false;
   }

   public boolean hasItemByType(int type) {
      Item[] var5 = this.itemStorage;
      int var4 = this.itemStorage.length;

      for(int var3 = 0; var3 < var4; ++var3) {
         Item item = var5[var3];
         if (item != null && item.getItemType() == type) {
            return true;
         }
      }

      return false;
   }

   public boolean hasCanUseDrugItem(int type, int userLevel) {
      Item[] var6 = this.itemStorage;
      int var5 = this.itemStorage.length;

      for(int var4 = 0; var4 < var5; ++var4) {
         Item item = var6[var4];
         if (item != null && item.getItemType() == type && item.getUserLevel() <= userLevel) {
            return true;
         }
      }

      return false;
   }

   public HashMap getModelsByItemSort(int sort) {
      HashMap itemModels = new HashMap();
      Item[] var6 = this.itemStorage;
      int var5 = this.itemStorage.length;

      for(int var4 = 0; var4 < var5; ++var4) {
         Item item = var6[var4];
         if (item != null && item.getItemSort() == sort) {
            if (itemModels.containsKey(item.getModelID())) {
               itemModels.put(item.getModelID(), item.getCount() + ((Integer)itemModels.get(item.getModelID())).intValue());
            } else {
               itemModels.put(item.getModelID(), item.getCount());
            }
         }
      }

      return itemModels;
   }

   public ArrayList getItemsBySort(int sort) {
      ArrayList items = new ArrayList();
      Item[] var6 = this.itemStorage;
      int var5 = this.itemStorage.length;

      for(int var4 = 0; var4 < var5; ++var4) {
         Item item = var6[var4];
         if (item != null && item.getItemSort() == sort) {
            items.add(item);
         }
      }

      return items;
   }

   public void searchAllExpiredItems(List items) {
      Item[] var5 = this.itemStorage;
      int var4 = this.itemStorage.length;

      for(int var3 = 0; var3 < var4; ++var3) {
         Item item = var5[var3];
         if (item != null && item.isTimeExpired(System.currentTimeMillis())) {
            items.add(item);
         }
      }

   }

   public void getAllItemsByModel(HashSet models, List items) {
      Item[] var6 = this.itemStorage;
      int var5 = this.itemStorage.length;

      for(int var4 = 0; var4 < var5; ++var4) {
         Item item = var6[var4];
         if (item != null && models.contains(item.getModelID())) {
            items.add(item);
         }
      }

   }

   public ArrayList getItemsByItemType(ArrayList itemTypes) {
      ArrayList items = new ArrayList();
      Item[] var6 = this.itemStorage;
      int var5 = this.itemStorage.length;

      for(int var4 = 0; var4 < var5; ++var4) {
         Item item = var6[var4];
         if (item != null) {
            Iterator var8 = itemTypes.iterator();

            while(var8.hasNext()) {
               int itemType = ((Integer)var8.next()).intValue();
               if (item.getItemType() == itemType) {
                  items.add(item.getID());
                  break;
               }
            }
         }
      }

      return items;
   }

   public void getItemByItemType(int type, boolean orderByLevel, int userLevel, int maxLevel, ArrayList items) {
      Item[] var9 = this.itemStorage;
      int j = this.itemStorage.length;

      for(int var7 = 0; var7 < j; ++var7) {
         Item item = var9[var7];
         if (item != null && item.getItemType() == type && (userLevel < 1 || item.getUserLevel() <= userLevel) && (maxLevel < 1 || item.getLevel() <= maxLevel)) {
            items.add(item);
         }
      }

      if (orderByLevel) {
         for(int i = 0; i < items.size(); ++i) {
            Item temp = (Item)items.get(i);

            for(j = i + 1; j < items.size(); ++j) {
               if (((Item)items.get(i)).getLevel() > ((Item)items.get(j)).getLevel()) {
                  items.set(i, (Item)items.get(j));
                  items.set(j, temp);
                  temp = (Item)items.get(i);
               }
            }
         }
      }

   }

   public void getQuickSaleEquip(Player player, List itemList, int saleMaxCount, HangSale hangSale) {
      HashMap lowTicketMap = DungeonManager.getCanSellMap(player);

      for(int i = 0; i < this.itemStorage.length; ++i) {
         Item item = this.itemStorage[i];
         if (item != null && hangSale.suit(item, lowTicketMap)) {
            itemList.add(item);
            if (saleMaxCount != -1 && itemList.size() >= saleMaxCount) {
               break;
            }
         }
      }

      lowTicketMap.clear();
      lowTicketMap = null;
   }

   public int getSpiritItem(List itemList, List conditionList) {
      int points = 0;

      for(int i = 0; i < this.itemStorage.length; ++i) {
         Item item = this.itemStorage[i];
         if (item != null && SpiritTools.filterItem(item, conditionList)) {
            itemList.add(item);
            points += RefineData.getRefineExp(item, 0);
            if (itemList.size() >= 12) {
               break;
            }
         }
      }

      return points;
   }

   public boolean hasEnoughItem(long existingID, int starLevel, int zhuijiaLevel, int needCount) {
      int tmpCount = 0;

      for(int i = 0; i < this.itemStorage.length; ++i) {
         Item item = this.itemStorage[i];
         if (item != null && item.getID() != existingID) {
            if (item.isEquipment() && item.getStarLevel() >= starLevel && item.getZhuijiaLevel() >= zhuijiaLevel) {
               ++tmpCount;
            }

            if (tmpCount >= needCount) {
               return true;
            }
         }
      }

      return false;
   }

   public void moveAwayfromContainer(Item item) {
      this.thoroughRemoveItem(item);
   }

   public boolean removeItemById(long objId, int count) {
      if (count < 1) {
         return false;
      } else {
         Item item = this.getItemByID(objId);
         if (item == null) {
            return false;
         } else {
            count = this.reduceItemCount(item, count);
            return count == 0;
         }
      }
   }

   public boolean effectiveSlot(int slot) {
      return slot >= 0 && slot < this.limit;
   }

   private void clearGrid(long objId, int slot) {
      if (this.effectiveSlot(slot)) {
         this.itemStorage[slot] = null;
      }

      this.itemMap.remove(objId);
   }

   public int reduceItemCount(Item item, int count) {
      int tmpCount = item.getCount();
      if (tmpCount >= count) {
         item.decreaseCount(count);
         count = 0;
      } else {
         item.decreaseCount(tmpCount);
         count -= tmpCount;
      }

      if (item.getCount() <= 0) {
         this.thoroughRemoveItem(item);
      }

      return count;
   }

   private void thoroughRemoveItem(Item item) {
      int slot = -1;
      Item[] var6 = this.itemStorage;
      int var5 = this.itemStorage.length;

      for(int var4 = 0; var4 < var5; ++var4) {
         Item tmpItem = var6[var4];
         if (tmpItem != null && tmpItem.getID() == item.getID()) {
            slot = tmpItem.getSlot();
            break;
         }
      }

      this.clearGrid(item.getID(), slot);
   }

   public boolean putToContainerBySlot(Item item, int slot) {
      if (!this.effectiveSlot(slot)) {
         return false;
      } else {
         this.addToGrid(item, slot);
         item.setSlot(slot);
         item.setContainerType(this.getType());
         return true;
      }
   }

   public int canExpansion(int modelId, int icNeed) {
      if (this.page >= this.getMaxPage()) {
         return 2001;
      } else {
         return !this.hasEnoughItem(modelId, icNeed) ? 3001 : 1;
      }
   }

   public int expansion(Player player, int addedPage) {
      int result = 1;
      synchronized(this) {
         try {
            if (addedPage < 1) {
               return 2002;
            }

            int newPage = this.page + addedPage;
            this.itemStorage = (Item[])this.addLength(this.itemStorage, addedPage);
            this.setPage(newPage);
            ItemPacketService.noticeGatewayUpdateStorage(player, this);
         } catch (Exception var6) {
            var6.printStackTrace();
            result = 2002;
         }

         return result;
      }
   }

   public Object addLength(Object oldObject, int addPage) {
      int maxPage = this.getMaxPage();
      if (this.page == maxPage) {
         return oldObject;
      } else {
         int addLen = addPage * this.getAddtionCount();
         if (!oldObject.getClass().isArray()) {
            return null;
         } else {
            int length = Array.getLength(oldObject);
            int newLength = length + addLen;
            if (newLength > this.getMaxCount()) {
               newLength = this.getMaxCount();
            }

            Object newArray = Array.newInstance(oldObject.getClass().getComponentType(), newLength);
            System.arraycopy(oldObject, 0, newArray, 0, length);
            return newArray;
         }
      }
   }

   public int getNextSlot() {
      for(int i = 0; i < this.limit; ++i) {
         if (this.itemStorage[i] == null) {
            return i;
         }
      }

      return -1;
   }

   public int getNextSlotInSpecifyBag(int index) {
      if (!this.effectiveBagIndex(index)) {
         return -1;
      } else {
         int additionCount = this.getAddtionCount();
         int first = index * additionCount;

         for(int i = first; i < first + additionCount; ++i) {
            if (this.itemStorage[i] == null) {
               return i;
            }
         }

         return -1;
      }
   }

   private boolean effectiveBagIndex(int index) {
      return index >= 0 && index < this.limit / this.getAddtionCount();
   }

   public int getNextSlotBehindSlot(int slot) {
      if (slot == -1) {
         slot = 0;
      }

      for(int i = slot; i < this.limit; ++i) {
         if (this.itemStorage[i] == null) {
            return i;
         }
      }

      return -1;
   }

   public int getVacantSize() {
      return this.limit - this.itemMap.size();
   }

   public boolean isFull() {
      return this.itemMap.size() >= this.limit;
   }

   public int getCurrentCount() {
      return this.itemMap.size();
   }

   public boolean exchangePosition(int firstSlot, int secondSlot) {
      Item[] var3 = this.itemStorage;
      synchronized(this.itemStorage) {
         boolean result = true;
         Item item = this.itemStorage[firstSlot];
         if (item == null) {
            return result;
         } else {
            Item sItem = this.itemStorage[secondSlot];
            this.moveItemSlot(firstSlot, secondSlot, item);
            if (sItem != null) {
               this.moveItemSlot(-1, firstSlot, sItem);
            }

            return result;
         }
      }
   }

   private void moveItemSlot(int oldSlot, int targetSlot, Item item) {
      this.itemStorage[targetSlot] = item;
      item.setSlot(targetSlot);
      if (oldSlot != -1) {
         this.itemStorage[oldSlot] = null;
      }

   }

   public void sortOutItems(int index) {
      Item[] var2 = this.itemStorage;
      synchronized(this.itemStorage) {
         int first = 0;
         int end = this.limit;

         for(int i = first; i < end; ++i) {
            Item item = this.itemStorage[i];
            int itemId = -1;
            int sort = -1;
            if (item != null) {
               itemId = item.getModelID();
               sort = item.getModel().getSort();
            }

            for(int j = i + 1; j < end; ++j) {
               Item sItem = this.itemStorage[j];
               if (sItem != null) {
                  int sItemId = sItem.getModelID();
                  int sSort = sItem.getModel().getSort();
                  boolean change = false;
                  if (item == null) {
                     change = this.exchangeItem(i, j);
                  } else if (sort > sSort) {
                     change = this.exchangeItem(i, j);
                  } else if (sort == sSort && itemId >= sItemId) {
                     change = this.exchangeItem(i, j);
                  }

                  if (change) {
                     item = this.itemStorage[i];
                     if (item != null) {
                        itemId = item.getModelID();
                        sort = item.getModel().getSort();
                     }
                  }
               }
            }
         }

      }
   }

   private boolean exchangeItem(int firstSlot, int secondSlot) {
      boolean result = true;
      Item[] var4 = this.itemStorage;
      synchronized(this.itemStorage) {
         Item item = this.itemStorage[firstSlot];
         Item sItem = this.itemStorage[secondSlot];
         if (item == null && sItem == null) {
            return false;
         } else if (item == null) {
            this.moveItemSlot(secondSlot, firstSlot, sItem);
            return result;
         } else if (sItem == null) {
            this.moveItemSlot(firstSlot, secondSlot, item);
            return result;
         } else if (item.getModelID() != sItem.getModelID()) {
            this.moveItemSlot(firstSlot, secondSlot, item);
            this.moveItemSlot(-1, firstSlot, sItem);
            return result;
         } else {
            boolean flag = true;
            if (!ItemTools.canStack(item, sItem)) {
               if (item.isBind() == sItem.isBind() || item.isBind()) {
                  flag = false;
                  result = false;
               }
            } else {
               int maxCount = item.getModel().getMaxStackCount();
               if (maxCount <= 1) {
                  result = false;
                  flag = false;
               } else if (maxCount > 1) {
                  if (item.getCount() < maxCount) {
                     int need = maxCount - item.getCount();
                     if (sItem.getCount() <= need) {
                        item.increaseCount(sItem.getCount(), sItem.getExpireTime());
                        this.clearGrid(sItem.getID(), secondSlot);
                     } else {
                        item.increaseCount(need, sItem.getExpireTime());
                        sItem.decreaseCount(need);
                     }

                     flag = false;
                  } else {
                     result = false;
                     flag = false;
                  }
               }
            }

            if (flag) {
               this.moveItemSlot(firstSlot, secondSlot, item);
               this.moveItemSlot(-1, firstSlot, sItem);
            }

            return result;
         }
      }
   }

   public final int getType() {
      return this.type;
   }

   public final int getLimit() {
      return this.limit;
   }

   public final int getPage() {
      return this.page;
   }

   public void destroy() {
      this.itemStorage = null;
      if (this.itemMap != null) {
         Iterator var2 = this.itemMap.values().iterator();

         while(var2.hasNext()) {
            Item item = (Item)var2.next();
            item.destroy();
         }

         this.itemMap.clear();
         this.itemMap = null;
      }

   }
}
