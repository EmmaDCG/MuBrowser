package com.mu.game.model.chat;

public class ChatChannelInfo {
   private int id = 0;
   private String color = "{0xffffff}";
   private String name = "";
   private String newColor = "";

   public ChatChannelInfo(int id) {
      this.id = id;
   }

   public int getId() {
      return this.id;
   }

   public String getColor() {
      return this.color;
   }

   public void setColor(String color) {
      this.color = color;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getNewColor() {
      return this.newColor;
   }

   public void setNewColor(String newColor) {
      this.newColor = newColor;
   }
}
