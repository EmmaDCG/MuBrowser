package com.mu.game.model.item.box.magic;

import com.mu.game.model.chat.ChatProcess;
import com.mu.game.model.chat.newlink.NewChatLink;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ShowItemManager;
import com.mu.game.model.unit.player.Player;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MagicRecord {
   private static ConcurrentLinkedQueue recordList = new ConcurrentLinkedQueue();
   public static final int MaxRecordCount = 12;
   private long roleID;
   private String roleName;
   private Item item;
   private String des;
   private NewChatLink[] link = null;

   public MagicRecord(long roleID, String roleName) {
      this.roleID = roleID;
      this.roleName = roleName;
   }

   private static MagicRecord createRecord(Player player, Item item, int realCount) {
      MagicRecord record = new MagicRecord(player.getID(), player.getName());
      Item tmpItem = item.cloneItem(2);
      tmpItem.setCount(realCount);
      tmpItem.setId(item.getID());
      record.setItem(tmpItem);
      return record;
   }

   public static MagicRecord addRecord(Player player, Item item, int realCount) {
      MagicRecord record = createRecord(player, item, realCount);
      ChatProcess.spellMagicBox(record);
      if (recordList.size() >= 12) {
         MagicRecord oldRecord = (MagicRecord)recordList.poll();
         if (oldRecord != null) {
            if (needRemove(oldRecord.getItemID())) {
               ShowItemManager.removeMagicBoxItem(oldRecord.getItemID());
            }

            oldRecord.destroy();
            oldRecord = null;
         }
      }

      ShowItemManager.addMagicBoxItem(record.getItem());
      recordList.add(record);
      return record;
   }

   private static boolean needRemove(long itemID) {
      Iterator var3 = recordList.iterator();

      while(var3.hasNext()) {
         MagicRecord record = (MagicRecord)var3.next();
         if (record.getItemID() == itemID) {
            return false;
         }
      }

      return true;
   }

   public static List getRecordList() {
      List list = new ArrayList();
      list.addAll(recordList);
      return list;
   }

   public long getItemID() {
      return this.item.getID();
   }

   public void destroy() {
      if (this.item != null) {
         this.item.destroy();
      }

      this.link = null;
   }

   public long getRoleID() {
      return this.roleID;
   }

   public void setRoleID(long roleID) {
      this.roleID = roleID;
   }

   public String getRoleName() {
      return this.roleName;
   }

   public void setRoleName(String roleName) {
      this.roleName = roleName;
   }

   public Item getItem() {
      return this.item;
   }

   public void setItem(Item item) {
      this.item = item;
   }

   public String getDes() {
      return this.des;
   }

   public void setDes(String des) {
      this.des = des;
   }

   public NewChatLink[] getLink() {
      return this.link;
   }

   public void setLink(NewChatLink[] link) {
      this.link = link;
   }
}
