package com.mu.game.model.spiritOfWar.model;

import com.mu.game.model.item.Item;
import java.util.List;

public class RefineItem {
   private int itemId;
   private List statList;
   private int domineering;
   private Item showItem = null;

   public RefineItem(int itemID, int domineering) {
      this.itemId = itemID;
      this.domineering = domineering;
   }

   public int getItemId() {
      return this.itemId;
   }

   public void setItemId(int itemId) {
      this.itemId = itemId;
   }

   public List getStatList() {
      return this.statList;
   }

   public void setStatList(List statList) {
      this.statList = statList;
   }

   public int getDomineering() {
      return this.domineering;
   }

   public void setDomineering(int domineering) {
      this.domineering = domineering;
   }

   public Item getShowItem() {
      return this.showItem;
   }

   public void setShowItem(Item showItem) {
      this.showItem = showItem;
   }
}
