package com.mu.game.model.chat;

public class ChatLinkInfo {
   private int type;
   private String value;
   private String color;
   private String newColor;

   public ChatLinkInfo(int type) {
      this.type = type;
   }

   public int getType() {
      return this.type;
   }

   public String getValue() {
      return this.value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public String getColor() {
      return this.color;
   }

   public void setColor(String color) {
      this.color = color;
   }

   public String getNewColor() {
      return this.newColor;
   }

   public void setNewColor(String newColor) {
      this.newColor = newColor;
   }
}
