package com.mu.game.model.unit.player.bluevip;

public class BlueIcon {
   private String tag;
   private String text;
   private int[] icons = new int[]{-1, -1};
   private String privilege;

   public String getTag() {
      return this.tag;
   }

   public void setTag(String tag) {
      this.tag = tag;
   }

   public String getText() {
      return this.text;
   }

   public void setText(String text) {
      this.text = text;
   }

   public int[] getIcons() {
      return this.icons;
   }

   public String getPrivilege() {
      return this.privilege;
   }

   public void setPrivilege(String privilege) {
      this.privilege = privilege;
   }
}
