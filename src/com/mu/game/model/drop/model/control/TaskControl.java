package com.mu.game.model.drop.model.control;

import com.mu.game.model.drop.model.DropControl;
import com.mu.game.model.service.StringTools;
import com.mu.game.model.unit.player.Player;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class TaskControl extends DropControl {
   private int taskID = -1;
   private HashMap itemMap = null;

   public TaskControl(int controlID, int type, int taskID, HashMap itemMap) {
      super(controlID, type);
      this.taskID = taskID;
      this.itemMap = itemMap;
   }

   public boolean checkDrop(Player player, int templateID) {
      if (!player.getTaskManager().isCurrent(this.getTaskID())) {
         return false;
      } else {
         Iterator it = this.itemMap.entrySet().iterator();

         while(it.hasNext()) {
            Entry entry = (Entry)it.next();
            if (!player.getBackpack().hasEnoughItem(((Integer)entry.getKey()).intValue(), ((Integer)entry.getValue()).intValue())) {
               return true;
            }
         }

         return false;
      }
   }

   public int getCanDropCount(Player player, int itemModelID) {
      if (this.itemMap.containsKey(itemModelID)) {
         int hasCount = player.getBackpack().getItemCount(itemModelID);
         int maxCount = ((Integer)this.itemMap.get(itemModelID)).intValue();
         maxCount -= hasCount;
         return maxCount >= 0 ? maxCount : 0;
      } else {
         return 2147483646;
      }
   }

   public HashMap getItemMap() {
      return this.itemMap;
   }

   public void setItemMap(HashMap itemMap) {
      this.itemMap = itemMap;
   }

   public int getTaskID() {
      return this.taskID;
   }

   public void setTaskID(int taskID) {
      this.taskID = taskID;
   }

   public static TaskControl createTaskControl(int controlID, int type, int targetID, String value) throws Exception {
      HashMap itemMap = StringTools.analyzeIntegerMap(value, ",");
      Iterator var6 = itemMap.values().iterator();

      Integer count;
      do {
         if (!var6.hasNext()) {
            TaskControl tc = new TaskControl(controlID, type, targetID, itemMap);
            return tc;
         }

         count = (Integer)var6.next();
      } while(count.intValue() >= 1 || count.intValue() == -1);

      throw new Exception("控制ID = " + controlID + ",数据出错");
   }
}
