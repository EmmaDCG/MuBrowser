package com.mu.game.model.fo;

public class FunctionOpenStruct {
   private int id;
   private String des;
   private boolean isOpen;
   private int icon;

   public FunctionOpenStruct(int id) {
      this.id = id;
   }

   public String getDes() {
      return this.des;
   }

   public void setDes(String des) {
      this.des = des;
   }

   public boolean isOpen() {
      return this.isOpen;
   }

   public void setOpen(boolean isOpen) {
      this.isOpen = isOpen;
   }

   public int getIcon() {
      return this.icon;
   }

   public void setIcon(int icon) {
      this.icon = icon;
   }

   public int getId() {
      return this.id;
   }
}
