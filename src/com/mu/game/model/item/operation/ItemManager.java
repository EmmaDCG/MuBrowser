package com.mu.game.model.item.operation;

import com.mu.game.model.equip.newStone.StoneDataManager;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.ItemRune;
import com.mu.game.model.item.amount.ItemAmount;
import com.mu.game.model.item.operation.imp.AddAndDeleteModelOperation;
import com.mu.game.model.item.operation.imp.AddItemByModelOperation;
import com.mu.game.model.item.operation.imp.AddItemByObjOperation;
import com.mu.game.model.item.operation.imp.DeleteItemAndAddOperation;
import com.mu.game.model.item.operation.imp.ExchangePositionOperation;
import com.mu.game.model.item.operation.imp.MoveAllMagicToBackpack;
import com.mu.game.model.item.operation.imp.MoveToOtherContainerOperation;
import com.mu.game.model.item.operation.imp.RuneInheritOperation;
import com.mu.game.model.item.operation.imp.SortoutItemOperation;
import com.mu.game.model.item.operation.imp.SplitItemOperation;
import com.mu.game.model.item.operation.imp.UpdateBindOperation;
import com.mu.game.model.item.operation.imp.UpdateDurability;
import com.mu.game.model.item.operation.imp.UpdateItemContainerOperation;
import com.mu.game.model.item.operation.imp.UpdateRuneOperation;
import com.mu.game.model.item.operation.imp.UpdateStarLevelOperation;
import com.mu.game.model.item.operation.imp.UpdateStoneOperation;
import com.mu.game.model.item.operation.imp.UpdateZhuijiaLevelOperation;
import com.mu.game.model.item.operation.imp.UpgradeItemOperation;
import com.mu.game.model.item.operation.imp.UseItemOperation;
import com.mu.game.model.item.other.ExpiredItemManager;
import com.mu.game.model.packet.ItemPacketService;
import com.mu.game.model.restrict.Restriction;
import com.mu.game.model.unit.player.Player;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class ItemManager {
   private Player player;
   private HashMap limitCounts = new HashMap();
   private HashMap itemCool;
   private HashMap itemTypeCool;
   private long pubCDTime = 0L;
   public static final int publicCD = 150;

   public ItemManager(Player player) {
      this.player = player;
      this.itemCool = new HashMap();
      this.itemTypeCool = new HashMap();
   }

   private OperationResult execute(ItemOperation operation) {
      OperationResult result = null;

      try {
         operation.setPlayer(this.player);
         result = operation.execute();
      } catch (Exception var4) {
         result = new OperationResult(8, -1L);
         var4.printStackTrace();
      }

      return result;
   }

   public int addItemCool(Item item, int coolTime) {
      long nowTime = System.currentTimeMillis();
      if (coolTime < 150) {
         coolTime = 150;
      }

      long endTime = nowTime + (long)coolTime;
      this.itemCool.put(item.getModelID(), endTime);
      if (item.getModel().isOccupationCD()) {
         this.pubCDTime = nowTime + 150L;
      }

      if (item.getModel().isOccupationCD()) {
         if (item.getItemSort() == 3) {
            this.itemTypeCool.put(item.getModel().getItemType(), endTime);
         } else {
            this.itemTypeCool.put(item.getModel().getItemType(), nowTime + 150L);
         }
      }

      return coolTime;
   }

   public boolean hasInCDTime(int itemType) {
      return this.itemTypeCool.containsKey(itemType) && System.currentTimeMillis() < ((Long)this.itemTypeCool.get(itemType)).longValue();
   }

   public boolean canUseItem(Item item) {
      boolean result = true;
      if (this.pubCDTime > System.currentTimeMillis()) {
         return false;
      } else {
         if (this.itemCool != null && this.itemCool.containsKey(item.getModelID())) {
            if (((Long)this.itemCool.get(item.getModelID())).longValue() > System.currentTimeMillis()) {
               return false;
            }

            this.itemCool.remove(item.getModelID());
         }

         if (item.getModel().isOccupationCD()) {
            int type = item.getModel().getItemType();
            if (this.itemTypeCool.containsKey(type)) {
               if (((Long)this.itemTypeCool.get(type)).longValue() > System.currentTimeMillis()) {
                  return false;
               }

               this.itemTypeCool.remove(type);
            }
         }

         return result;
      }
   }

   public OperationResult addItem(int modelID, int count, boolean isBind, int source) {
      ItemDataUnit unit = new ItemDataUnit(modelID, count, isBind);
      unit.setSource(source);
      List unitList = new ArrayList();
      unitList.add(unit);
      OperationResult result = this.addItem((List)unitList);
      unitList.clear();
      unitList = null;
      return result;
   }

   public OperationResult addItem(Item item, int source) {
      ItemOperation operation = new AddItemByObjOperation(item, source);
      return this.execute(operation);
   }

   public OperationResult addItem(ItemDataUnit unit) {
      List unitList = new ArrayList();
      if (unit != null) {
         unitList.add(unit);
      }

      OperationResult result = this.addItem((List)unitList);
      unitList.clear();
      unitList = null;
      return result;
   }

   public OperationResult addItem(List unitList) {
      AddItemByModelOperation operation = new AddItemByModelOperation(unitList);
      OperationResult result = this.execute(operation);
      return result;
   }

   public OperationResult deleteItem(Item item, int source) {
      return item != null && item.getCount() >= 1 ? this.deleteItem(item, item.getCount(), source) : new OperationResult(3002, -1L);
   }

   public OperationResult deleteItem(Item item, int count, int source) {
      if (item != null && item.getCount() >= 1) {
         if (item.getCount() < count) {
            return new OperationResult(3001, -1L);
         } else {
            HashMap itemMap = new HashMap();
            itemMap.put(item, count);
            ItemOperation operation = new DeleteItemAndAddOperation(itemMap, source, (List)null);
            OperationResult result = this.execute(operation);
            itemMap.clear();
            itemMap = null;
            return result;
         }
      } else {
         return new OperationResult(3002, -1L);
      }
   }

   public OperationResult deleteItemList(List itemList, int source) {
      if (itemList != null && itemList.size() >= 1) {
         HashMap itemMap = new HashMap();
         Iterator var5 = itemList.iterator();

         while(var5.hasNext()) {
            Item item = (Item)var5.next();
            if (item.getCount() < 1) {
               return new OperationResult(3002, -1L);
            }

            itemMap.put(item, item.getCount());
         }

         ItemOperation operation = new DeleteItemAndAddOperation(itemMap, source, (List)null);
         OperationResult result = this.execute(operation);
         itemMap.clear();
         itemMap = null;
         return result;
      } else {
         return new OperationResult(3002, -1L);
      }
   }

   public OperationResult deleteItemAndAddModel(Item item, int count, int source, List unitList) {
      if (item == null) {
         return new OperationResult(3002, -1L);
      } else if (item.getCount() < count) {
         return new OperationResult(3001, -1L);
      } else {
         HashMap delMap = new HashMap();
         delMap.put(item, count);
         ItemOperation operation = new DeleteItemAndAddOperation(delMap, source, unitList);
         OperationResult result = this.execute(operation);
         delMap.clear();
         delMap = null;
         return result;
      }
   }

   public OperationResult deleteItemByModel(int modelID, int count, int source) {
      return this.deleteAndAddModel(modelID, count, source, (List)null);
   }

   public OperationResult deleteAndAddModel(int modelID, int count, int source, List unitList) {
      HashMap delMap = new HashMap();
      delMap.put(modelID, count);
      OperationResult result = this.deleteAndAddModel(delMap, source, true, unitList);
      delMap.clear();
      delMap = null;
      return result;
   }

   public OperationResult deleteItemByModel(HashMap delMap, int source) {
      return this.deleteAndAddModel(delMap, source, false, (List)null);
   }

   public OperationResult deleteAndAddModel(HashMap delMap, int source, boolean defaultBind, List unitList) {
      ItemOperation operation = new AddAndDeleteModelOperation(delMap, source, defaultBind, unitList);
      OperationResult result = this.execute(operation);
      return result;
   }

   public OperationResult exchangePosition(int containerType, int firstPosition, int secondPosition) {
      ExchangePositionOperation operation = new ExchangePositionOperation(containerType, firstPosition, secondPosition);
      OperationResult result = this.execute(operation);
      return result;
   }

   public OperationResult sortoutStorage(int containerType) {
      Restriction.itemOperationPre(this.getPlayer());
      if (this.getPlayer().getContainer(containerType) == null) {
         return new OperationResult(2005, -1L);
      } else {
         SortoutItemOperation operation = new SortoutItemOperation(containerType);
         OperationResult result = this.execute(operation);
         return result;
      }
   }

   public OperationResult splitItem(long itemID, int count, int containerType) {
      if (count < 1) {
         return new OperationResult(3010, itemID);
      } else if (this.getPlayer().getStorage(containerType) == null) {
         return new OperationResult(2005, -1L);
      } else {
         ItemOperation operation = new SplitItemOperation(itemID, count, containerType);
         OperationResult result = this.execute(operation);
         return result;
      }
   }

   public OperationResult moveToOtherContainer(int fromS, int toS, long itemID, int slot) {
      if (this.getPlayer().getContainer(fromS) == null) {
         return new OperationResult(2005, -1L);
      } else if (this.getPlayer().getContainer(toS) == null) {
         return new OperationResult(2005, -1L);
      } else {
         ItemOperation operation = new MoveToOtherContainerOperation(fromS, toS, itemID, slot);
         OperationResult result = this.execute(operation);
         return result;
      }
   }

   public OperationResult moveAllMagicToBackpack() {
      ItemOperation operation = new MoveAllMagicToBackpack();
      OperationResult result = this.execute(operation);
      return result;
   }

   public OperationResult useItem(Item item, int useNum, boolean definite) {
      if (item == null) {
         return new OperationResult(3002, -1L);
      } else {
         ItemOperation operation = new UseItemOperation(item, useNum, definite);
         OperationResult result = this.execute(operation);
         return result;
      }
   }

   public OperationResult equipItem(Item item, int slot) {
      if (item == null) {
         return new OperationResult(3002, -1L);
      } else {
         UseItemOperation operation = new UseItemOperation(item, 1, false);
         operation.setWantedSlot(slot);
         return this.execute(operation);
      }
   }

   public OperationResult updateItemContainer(Item item, int ts, int targetSlot) {
      if (item == null) {
         return new OperationResult(3002, -1L);
      } else {
         ItemOperation operation = new UpdateItemContainerOperation(item, ts, targetSlot);
         return this.execute(operation);
      }
   }

   public OperationResult updateStarLevel(Item item, int newStarLevel, List runes) {
      if (item == null) {
         return new OperationResult(3002, -1L);
      } else {
         ItemOperation operation = new UpdateStarLevelOperation(item, newStarLevel, (List)null);
         return this.execute(operation);
      }
   }

   public OperationResult updateZhuijiaLevel(Item item, int newZhuijiaLevel) {
      if (item == null) {
         return new OperationResult(3002, -1L);
      } else {
         ItemOperation operation = new UpdateZhuijiaLevelOperation(item, newZhuijiaLevel);
         return this.execute(operation);
      }
   }

   public OperationResult upgradeItem(Item item, int newModelID) {
      if (item == null) {
         return new OperationResult(3002, -1L);
      } else {
         ItemOperation operation = new UpgradeItemOperation(item, newModelID);
         return this.execute(operation);
      }
   }

   public OperationResult addRune(Item item, Item runeItem) {
      if (item != null && runeItem != null) {
         OperationResult result = this.deleteItem(runeItem, 1, 21);
         if (result.isOk()) {
            ItemOperation addRune = new UpdateRuneOperation(item, runeItem.getModelID(), true);
            this.execute(addRune);
         }

         return result;
      } else {
         return new OperationResult(3002, -1L);
      }
   }

   public OperationResult inheriteRune(Item targetItem, Item materialItem, int consumeID, int consumeCount) {
      if (targetItem != null && materialItem != null && targetItem.getCount() >= 1 && materialItem.getCount() >= 1) {
         if (targetItem.getModelID() != consumeID && materialItem.getModelID() != consumeID) {
            OperationResult result = this.deleteItemByModel(consumeID, consumeCount, 21);
            if (result.isOk()) {
               ItemOperation inheriteRune = new RuneInheritOperation(targetItem, materialItem);
               this.execute(inheriteRune);
            }

            return result;
         } else {
            return new OperationResult(5047, -1L);
         }
      } else {
         return new OperationResult(3002, -1L);
      }
   }

   public OperationResult deleRune(Item item, int index) {
      if (item != null && item.getCount() >= 1) {
         ItemRune rune = item.getRuneByIndex(index);
         if (rune == null) {
            return new OperationResult(5032, item.getID());
         } else {
            ItemOperation operation = new UpdateRuneOperation(item, index, false);
            return this.execute(operation);
         }
      } else {
         return new OperationResult(3002, -1L);
      }
   }

   public void updateBind(Item item, boolean bind, int source) {
      if (item != null) {
         ItemOperation operation = new UpdateBindOperation(item, bind, source);
         this.execute(operation);
      }
   }

   public OperationResult updateItemAddStones(Item item, Item stoneItem) {
      if (item != null && item.getCount() >= 1 && StoneDataManager.hasStoneStat(stoneItem.getModelID())) {
         OperationResult result = this.deleteItem(stoneItem, 1, 21);
         if (result.isOk()) {
            ItemOperation operation = new UpdateStoneOperation(item, stoneItem.getModelID(), true);
            return this.execute(operation);
         } else {
            return result;
         }
      } else {
         return new OperationResult(3002, -1L);
      }
   }

   public OperationResult updateItemDeleteStone(Item item, int index) {
      if (item != null && item.getCount() >= 1) {
         OperationResult result = new OperationResult(1, item.getID());
         if (result.isOk()) {
            ItemOperation operation = new UpdateStoneOperation(item, index, false);
            return this.execute(operation);
         } else {
            return result;
         }
      } else {
         return new OperationResult(3002, -1L);
      }
   }

   public OperationResult compositeItem(HashMap autoConsume, HashMap chooseItems, ItemDataUnit addUnit) {
      if (addUnit != null && this.player.getBackpack().isFull()) {
         return new OperationResult(2004, -1L);
      } else {
         boolean bind = false;
         HashMap delItems = new HashMap();
         HashMap tmpMap = new HashMap();
         tmpMap.putAll(autoConsume);

         Iterator it;
         Entry entry;
         Item item;
         for(it = chooseItems.entrySet().iterator(); it.hasNext(); delItems.put(item, (Integer)entry.getValue())) {
            entry = (Entry)it.next();
            item = this.player.getBackpack().getItemByID(((Long)entry.getKey()).longValue());
            if (item == null || item.getCount() < 1) {
               return new OperationResult(3002, -1L);
            }

            if (item.getCount() < ((Integer)entry.getValue()).intValue()) {
               return new OperationResult(3001, -1L);
            }

            if (item.isBind()) {
               bind = true;
            }

            if (tmpMap.containsKey(item.getModelID())) {
               int tmpCount = ((Integer)entry.getValue()).intValue() + ((Integer)tmpMap.get(item.getModelID())).intValue();
               tmpMap.put(item.getModelID(), tmpCount);
            }
         }

         it = tmpMap.entrySet().iterator();

         while(it.hasNext()) {
            entry = (Entry)it.next();
            if (!this.player.getBackpack().hasEnoughItem(((Integer)entry.getKey()).intValue(), ((Integer)entry.getValue()).intValue())) {
               return new OperationResult(3001, -1L);
            }

            if (!bind && this.player.getBackpack().getItemCountByModelId(((Integer)entry.getKey()).intValue(), false) < ((Integer)entry.getValue()).intValue()) {
               bind = true;
            }
         }

         tmpMap.clear();
         tmpMap = null;
         OperationResult result = this.deleteItemByModel(autoConsume, 3);
         if (result.isOk()) {
            Iterator it2 = delItems.entrySet().iterator();

            while(it2.hasNext()) {
               Entry entry1 = (Entry)it2.next();
               this.deleteItem((Item)entry1.getKey(), ((Integer)entry1.getValue()).intValue(), 3);
            }

            if (addUnit != null) {
               addUnit.setBind(bind);
               addUnit.setSource(3);
               result = this.addItem(addUnit);
            }
         }

         delItems.clear();
         delItems = null;
         return result;
      }
   }

   public void updateItemDurability(HashMap itemMap) {
      if (itemMap != null && itemMap.size() >= 1) {
         HashMap itemInEquip = null;
         Iterator var4 = itemMap.keySet().iterator();

         while(var4.hasNext()) {
            Item item = (Item)var4.next();
            if (item.getContainerType() == 0) {
               if (itemInEquip == null) {
                  itemInEquip = new HashMap();
               }

               itemInEquip.put(item, item.getDurability());
            }
         }

         ItemOperation operation = new UpdateDurability(itemMap);
         this.execute(operation);
         if (itemInEquip != null) {
            this.player.getEquipment().doDuraChange(itemInEquip);
            itemInEquip.clear();
         }

         itemInEquip = null;
      }
   }

   public void handleExpiredItem(boolean firstEnterMap) {
      List itemList = new ArrayList();
      List cantAutoDelList = null;
      this.player.getBackpack().searchAllExpiredItems(itemList);
      this.player.getDepot().searchAllExpiredItems(itemList);
      Iterator it = itemList.iterator();

      Item item;
      while(it.hasNext()) {
         item = (Item)it.next();
         if (ExpiredItemManager.cantAutoDel(item.getModelID())) {
            it.remove();
         }

         if (firstEnterMap) {
            ExpiredItemManager.handelInstantCheck(this.player, item, firstEnterMap);
         }
      }

      if (itemList.size() > 0) {
         this.deleteItemList(itemList, 34);
         itemList.clear();
      }

      this.player.getEquipment().searchAllExpiredItems(itemList);

      for(it = itemList.iterator(); it.hasNext(); ExpiredItemManager.handelInstantCheck(this.player, item, firstEnterMap)) {
         item = (Item)it.next();
         if (ExpiredItemManager.cantAutoDel(item.getModelID())) {
            it.remove();
            if (cantAutoDelList == null) {
               cantAutoDelList = new ArrayList();
            }

            cantAutoDelList.add(item);
         }
      }

      if (itemList.size() > 0) {
         this.deleteItemList(itemList, 34);
      }

      if (cantAutoDelList != null) {
         Iterator var7 = cantAutoDelList.iterator();

         while(var7.hasNext()) {
            Item item2 = (Item)var7.next();
            if (!this.player.getBackpack().isFull()) {
               this.player.getEquipment().unEquipItem(item2.getID(), -1, false);
            }
         }

         cantAutoDelList.clear();
         cantAutoDelList = null;
      }

      itemList.clear();
      itemList = null;
   }

   public void addItemUseCount(int modelID, int count) {
      ItemAmount amount = ItemAmount.getAmount(modelID);
      if (amount != null) {
         Integer usedCount = (Integer)this.limitCounts.get(modelID);
         if (usedCount == null) {
            this.limitCounts.put(modelID, count);
         } else {
            this.limitCounts.put(modelID, usedCount.intValue() + count);
         }

         ItemPacketService.noticeGatewayUpdateItemLimits(this.player, modelID, ((Integer)this.limitCounts.get(modelID)).intValue());
      }

   }

   public int getRemainUseCount(int modelID) {
      ItemAmount amount = ItemAmount.getAmount(modelID);
      if (amount == null) {
         return Integer.MAX_VALUE;
      } else {
         Integer count = (Integer)this.limitCounts.get(modelID);
         return count == null ? amount.getLimitCountByPlayer(this.getPlayer()) : amount.getLimitCountByPlayer(this.getPlayer()) - count.intValue();
      }
   }

   public void loadItemLimit(int modelID, int limitCount) {
      this.limitCounts.put(modelID, limitCount);
   }

   public void clearLimitCount() {
      this.limitCounts.clear();
   }

   public void destroy() {
      this.player = null;
      if (this.itemCool != null) {
         this.itemCool.clear();
         this.itemCool = null;
      }

      if (this.itemTypeCool != null) {
         this.itemTypeCool.clear();
         this.itemTypeCool = null;
      }

      if (this.limitCounts != null) {
         this.limitCounts.clear();
         this.limitCounts = null;
      }

   }

   public Player getPlayer() {
      return this.player;
   }
}
