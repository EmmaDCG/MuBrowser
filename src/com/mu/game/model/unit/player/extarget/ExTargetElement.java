package com.mu.game.model.unit.player.extarget;

import com.mu.game.model.item.Item;
import com.mu.io.game.packet.imp.extarget.ExTargetGetWay;

public class ExTargetElement {
   private Item item;
   private int index;
   private ExTargetGetWay packet;
   private int zoom = 100;
   private String tips;

   public Item getItem() {
      return this.item;
   }

   public void setItem(Item item) {
      this.item = item;
   }

   public int getIndex() {
      return this.index;
   }

   public void setIndex(int index) {
      this.index = index;
   }

   public ExTargetGetWay getPacket() {
      return this.packet;
   }

   public void setPacket(ExTargetGetWay packet) {
      this.packet = packet;
   }

   public int getZoom() {
      return this.zoom;
   }

   public void setZoom(int zoom) {
      this.zoom = zoom;
   }

   public String getTips() {
      return this.tips;
   }

   public void setTips(String tips) {
      this.tips = tips;
   }
}
