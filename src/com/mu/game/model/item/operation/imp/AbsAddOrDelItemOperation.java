package com.mu.game.model.item.operation.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.item.container.imp.Storage;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.item.operation.ItemOperation;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public abstract class AbsAddOrDelItemOperation extends ItemOperation {
   protected int result = 1;
   protected long itemID = -1L;

   protected int canPutToContainer(List items) {
      HashMap needGrids = new HashMap();
      Iterator var4 = items.iterator();

      while(var4.hasNext()) {
         ItemDataUnit unit = (ItemDataUnit)var4.next();
         ItemModel itemModel = ItemModel.getModel(unit.getModelID());
         if (itemModel == null) {
            return 3002;
         }

         if (!itemModel.isnotStorage()) {
            if (unit.getCount() < 1) {
               return 3002;
            }

            Storage storage = this.getTargetStorage(itemModel.getSort(), unit.getSource());
            int tmpCount = 0;
            if (itemModel.getMaxStackCount() == 1) {
               tmpCount = unit.getCount();
            } else {
               int freeSpace = storage.getRemainStackCount(unit.getModelID(), unit.isBind(), unit.getActualExpireTime());
               int value = unit.getCount();
               if (freeSpace < value) {
                  value -= freeSpace;
                  tmpCount = value / itemModel.getMaxStackCount();
                  if (value % itemModel.getMaxStackCount() != 0) {
                     ++tmpCount;
                  }
               }
            }

            this.addNeedGrids(needGrids, storage.getType(), tmpCount);
         }
      }

      var4 = needGrids.entrySet().iterator();

      while(var4.hasNext()) {
         Entry entry = (Entry)var4.next();
         int type = ((Integer)entry.getKey()).intValue();
         int needCount = ((Integer)entry.getValue()).intValue();
         Storage storage = this.getPlayer().getStorage(type);
         if (storage.getVacantSize() < needCount) {
            return 2003;
         }
      }

      needGrids.clear();
      needGrids = null;
      return 1;
   }

   protected void addNeedGrids(HashMap targetMap, int containerType, int count) {
      Integer hasCount = (Integer)targetMap.get(containerType);
      if (hasCount == null) {
         targetMap.put(containerType, count);
      } else {
         targetMap.put(containerType, hasCount.intValue() + count);
      }

   }

   protected long addItemDetail(ItemDataUnit unit, Storage storage) {
      long Id = -1L;

      try {
         ItemModel model = ItemModel.getModel(unit.getModelID());
         int modelID = model.getID();
         if (specialActionWhenAdd(this.getPlayer(), model, unit.getCount(), unit.isBind(), unit.getSource())) {
            return Id;
         } else {
            int count = unit.getCount();
            int maxStackCount = model.getMaxStackCount();
            int slot = 0;
            List itemList;
            if (maxStackCount > 1) {
               itemList = storage.getItemsByModelID(modelID);
               Iterator var12 = itemList.iterator();

               while(var12.hasNext()) {
                  Item tempItem = (Item)var12.next();
                  if (ItemTools.canStack(tempItem, unit.isBind(), unit.getActualExpireTime()) && tempItem.getCount() < maxStackCount) {
                     int needCount = maxStackCount - tempItem.getCount();
                     if (needCount > count) {
                        needCount = count;
                     }

                     tempItem.increaseCount(needCount, unit.getActualExpireTime());
                     this.doUpdateItemAction(tempItem, storage, 1, needCount, unit.getSource(), storage.getType());
                     Id = tempItem.getID();
                     count -= needCount;
                     if (count <= 0) {
                        break;
                     }
                  }
               }

               itemList.clear();
               itemList = null;
            }

            ItemDataUnit tmpUnit = unit.cloneUnit();
            tmpUnit.setCount(maxStackCount);
            int tem = count / maxStackCount;
            slot = 0;
            if (tem >= 1) {
               for(int i = 0; i < tem; ++i) {
                  slot = storage.getNextSlot();
                  Item item = ItemTools.createItem(1, tmpUnit);
                  item.setSlot(slot);
                  if (Id == -1L) {
                     Id = item.getID();
                  }

                  this.doAddToContainerAction(item, slot, storage, tmpUnit.getSource());
                  count -= maxStackCount;
                  if (count < maxStackCount) {
                     break;
                  }
               }
            }

            tmpUnit.setCount(count);
            if (count > 0) {
               Item item = ItemTools.createItem(1, tmpUnit);
               slot = storage.getNextSlot();
               item.setSlot(slot);
               if (Id == -1L) {
                  Id = item.getID();
               }

               this.doAddToContainerAction(item, slot, storage, unit.getSource());
            }

            itemList = null;
            return Id;
         }
      } catch (Exception var14) {
         var14.printStackTrace();
         return 2L;
      }
   }

   protected void deleteItemDetail(int modelID, int count, Storage container, boolean defaultBind, int source) {
      try {
         List itemList = null;
         List otherBindItemList = null;
         if (defaultBind) {
            itemList = container.getItemsByModelID(modelID, true);
            otherBindItemList = container.getItemsByModelID(modelID, false);
         } else {
            itemList = container.getItemsByModelID(modelID, false);
            otherBindItemList = container.getItemsByModelID(modelID, true);
         }

         itemList.addAll(otherBindItemList);
         Iterator var9 = itemList.iterator();

         while(var9.hasNext()) {
            Item tempItem = (Item)var9.next();
            int tempCount = tempItem.getCount();
            if (count >= tempCount) {
               this.doRemoveItemAction(tempItem, tempCount, container, source);
            } else {
               this.doRemoveItemAction(tempItem, count, container, source);
            }

            count -= tempCount;
            if (count <= 0) {
               break;
            }
         }

         itemList.clear();
         itemList = null;
         otherBindItemList.clear();
         otherBindItemList = null;
      } catch (Exception var11) {
         var11.printStackTrace();
      }

   }

   public int checkAddItems(List items, List units) {
      if (items.size() < 1) {
         return 3002;
      } else {
         units.add((ItemDataUnit)items.get(0));

         for(int i = 1; i < items.size(); ++i) {
            ItemDataUnit is = (ItemDataUnit)items.get(i);
            ItemModel model = ItemModel.getModel(is.getModelID());
            if (model == null) {
               return 3002;
            }

            boolean stack = false;
            Iterator var8 = units.iterator();

            while(var8.hasNext()) {
               ItemDataUnit unit = (ItemDataUnit)var8.next();
               if (model.getMaxStackCount() != 1 && is.getModelID() == unit.getModelID() && is.isBind() == unit.isBind() && is.getActualExpireTime() == unit.getActualExpireTime()) {
                  stack = true;
                  unit.setCount(unit.getCount() + is.getCount());
               }
            }

            if (!stack) {
               units.add((ItemDataUnit)items.get(i));
            }

            model = null;
         }

         return 1;
      }
   }

   protected boolean hasEnoughItem(HashMap items) {
      boolean result = true;
      if (items == null) {
         return false;
      } else if (items.size() == 0) {
         return true;
      } else {
         Iterator it = items.entrySet().iterator();

         while(it.hasNext()) {
            Entry entry = (Entry)it.next();
            ItemModel model = ItemModel.getModel(((Integer)entry.getKey()).intValue());
            if (model == null) {
               return false;
            }

            Storage storage = this.getTargetStorage(model.getSort(), 100);
            if (!model.isnotStorage()) {
               result = storage.hasEnoughItem(((Integer)entry.getKey()).intValue(), ((Integer)entry.getValue()).intValue());
               if (!result) {
                  return false;
               }
            }
         }

         return result;
      }
   }
}
