package com.mu.game.model.drop.model;

import com.mu.game.model.chat.ChatProcess;
import com.mu.game.model.chat.newlink.NewChatLink;
import com.mu.game.model.chat.newlink.NewItemLink;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;

public class WellShowItem {
   private long time;
   private Item item;
   private long roleID;
   private String roleName;
   private NewChatLink[] newLink = null;
   private String newDes;
   private String newReason;
   private String desWithoutTime;

   public WellShowItem(Item item, String reason, String newReason, Player player) {
      this.roleID = player.getID();
      this.roleName = player.getName();
      this.item = item.cloneItem(2);
      this.item.setId(item.getID());
      this.newReason = newReason;
      this.time = System.currentTimeMillis();
   }

   public String getNewReason() {
      return this.newReason;
   }

   public long getID() {
      return this.item.getID();
   }

   public void addLink() {
      ChatProcess.spellWellDrop(this);
   }

   public NewItemLink createNewItemLink(int index) {
      return ChatProcess.createNewItemLink(this, index);
   }

   public long getTime() {
      return this.time;
   }

   public void setTime(long time) {
      this.time = time;
   }

   public Item getItem() {
      return this.item;
   }

   public void setItem(Item item) {
      this.item = item;
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

   public NewChatLink[] getNewLink() {
      return this.newLink;
   }

   public void setNewLink(NewChatLink[] link) {
      this.newLink = link;
   }

   public void destroy() {
      if (this.item != null) {
         this.item.destroy();
      }

      this.newLink = null;
   }

   public String getNewDes() {
      return this.newDes;
   }

   public void setNewDes(String newDes) {
      this.newDes = newDes;
   }

   public String getDesWithoutTime() {
      return this.desWithoutTime;
   }

   public void setDesWithoutTime(String desWithoutTime) {
      this.desWithoutTime = desWithoutTime;
   }
}
