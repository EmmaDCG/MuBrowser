package com.mu.game.model.task;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.ItemTools;
import java.util.Iterator;
import java.util.List;

public class TaskRewardItemData extends ItemDataUnit {
   private Item item;

   public TaskRewardItemData(int modelID, int count, boolean isBind, int statRuleID, long expireTime) {
      super(modelID, count, isBind);
      this.setStatRuleID(statRuleID);
      this.setExpireTime(expireTime);
   }

   public static TaskRewardItemData newInstance(int modelID, int count, boolean isBind, int statRuleID, long expireTime) {
      TaskRewardItemData trd = new TaskRewardItemData(modelID, count, isBind, statRuleID, expireTime);
      trd.setHide(true);
      Item item = ItemTools.createItem(2, trd);
      if (item == null) {
         return null;
      } else {
         ItemTools.setSystemExpire(item, expireTime);
         trd.setItem(item);
         return trd;
      }
   }

   public TaskRewardItemData newInstance(int count) {
      TaskRewardItemData newTRI = new TaskRewardItemData(this.getModelID(), count, this.isBind(), this.getStatRuleID(), this.getExpireTime());
      newTRI.item = this.item.cloneItem(2);
      newTRI.item.setCount(count);
      return newTRI;
   }

   public Item getItem() {
      return this.item;
   }

   public void setItem(Item item) {
      this.item = item;
   }

   public void destroy() {
      if (this.item != null) {
         this.item.destroy();
      }

      this.item = null;
   }

   public static void destroyList(List rewardList) {
      if (rewardList != null) {
         Iterator it = rewardList.iterator();

         while(it.hasNext()) {
            TaskRewardItemData data = (TaskRewardItemData)it.next();
            data.destroy();
         }

         rewardList.clear();
      }

   }
}
