package com.mu.game.model.drop.model;

import com.mu.config.Constant;
import com.mu.game.model.drop.SystemDropManager;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.weight.WeightElement;
import java.util.HashMap;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DropGroup extends WeightElement {
   private static Logger logger = LoggerFactory.getLogger(DropGroup.class);
   private static HashMap groups = new HashMap();
   private int groupID;
   private boolean isEmpty = false;

   public DropGroup(int groupID) {
      this.groupID = groupID;
   }

   public void addDropModel(DropModel model) {
      this.addAtom(model);
      if (model.getModelID() == -1) {
         this.isEmpty = true;
      }

   }

   public static void addGroup(DropGroup group) {
      groups.put(group.getGroupID(), group);
   }

   public static DropGroup getGroup(int groupID) {
      return (DropGroup)groups.get(groupID);
   }

   public static boolean hasGroup(int groupID) {
      return groups.containsKey(groupID);
   }

   public static void checkAll() throws Exception {
      boolean err = false;
      Iterator var2 = groups.values().iterator();

      while(var2.hasNext()) {
         DropGroup group = (DropGroup)var2.next();
         boolean c = group.check();
         if (!c) {
            err = true;
         }
      }

      if (err) {
         throw new Exception("掉落组不正确");
      }
   }

   public boolean check() throws Exception {
      if (this.isEmpty) {
         this.getAtoms().clear();
         return true;
      } else if (!this.isEmpty && this.getAtoms().size() < 1) {
         logger.error("掉落组数量有误 id = {}，数量 = {}", this.groupID, Integer.valueOf(0));
         return false;
      } else {
         this.sortByWeight("掉落组  " + this.groupID);
         if (this.getMaxWeight() < 1) {
            logger.error("掉落组最大权重有错 id = {}，数量 = {}", this.groupID, Integer.valueOf(0));
            return false;
         } else {
            return true;
         }
      }
   }

   public int createDropItems(HashMap itemList, Player player, int type, boolean isBind, int protecteTime, int statRuleID, int controlID) {
      if (this.isEmpty) {
         return 0;
      } else {
         DropModel model = (DropModel)this.getRndAtom();
         int roleLevel = player.getLevel();
         int count = model.getMaxCount();
         int dropID = model.getDropID();
         boolean canDrop = true;
         switch(type) {
         case 1:
            canDrop = model.canDrop(roleLevel);
            if (!canDrop) {
               return 0;
            }

            int serverDay = SystemDropManager.getWorldDayCount(dropID);
            int personDay = player.getDropManager().getPersonDayCount(dropID);
            count = model.getCount(personDay, serverDay);
            break;
         default:
            count = Math.min(count, DropControlManager.getDropCount(player, controlID, model.getModelID()));
         }

         if (count < 1) {
            return count;
         } else {
            switch(type) {
            case 1:
               SystemDropManager.addWorldCount(model, count);
               player.getDropManager().addWorldCount(model, count);
            default:
               this.createItem(itemList, model, count, isBind, player, protecteTime, statRuleID);
               return count;
            }
         }
      }
   }

   private void createItem(HashMap itemList, DropModel dmodel, int count, boolean isBind, Player player, int protecteTime, int statRuleID) {
      ItemModel model = ItemModel.getModel(dmodel.getModelID());
      int stackNumber = model.getMaxStackCount();
      int itemCount;
      int i;
      if (model.isMoney()) {
         itemCount = player.getStatValue(StatEnum.MONEY_ADD_WKM);
         if (itemCount > 0) {
            i = Constant.getPercentValue(count, itemCount);
            if (i > 0) {
               count += count > 2147483646 - i ? Integer.MAX_VALUE - count - 1 : i;
            }
         }
      }

      if (model.isMoney()) {
         ItemDataUnit unit = dmodel.createUnit(count, isBind, statRuleID);
         SystemDropManager.createMoenyDrop(unit, itemList, protecteTime);
      } else {
         for(; count > 0; count -= itemCount) {
            itemCount = count;
            if (count > stackNumber) {
               itemCount = stackNumber;
            }

            if (model.isMoney()) {
               Item item = ItemTools.createItem(2, dmodel.createUnit(itemCount, isBind, statRuleID));
               if (item != null) {
                  itemList.put(item, protecteTime);
               }
            } else {
               for(i = 1; i <= itemCount; ++i) {
                  Item item = ItemTools.createItem(2, dmodel.createUnit(1, isBind, statRuleID));
                  if (item != null) {
                     itemList.put(item, protecteTime);
                  }
               }
            }
         }
      }

   }

   public int getGroupID() {
      return this.groupID;
   }

   public void setGroupID(int groupID) {
      this.groupID = groupID;
   }

   public boolean isEmpty() {
      return this.isEmpty;
   }

   public void setEmpty(boolean isEmpty) {
      this.isEmpty = isEmpty;
   }
}
