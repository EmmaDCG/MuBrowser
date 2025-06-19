package com.mu.game.model.unit.player.shortcut;

public class ShortcutEntry {
   private int type;
   private int modelID;
   private int position;

   public ShortcutEntry(int type, int modelID, int position) {
      this.type = type;
      this.modelID = modelID;
      this.position = position;
   }

   public int getType() {
      return this.type;
   }

   public void setType(int type) {
      this.type = type;
   }

   public int getModelID() {
      return this.modelID;
   }

   public void setModelID(int modelID) {
      this.modelID = modelID;
   }

   public int getPosition() {
      return this.position;
   }

   public void setPosition(int position) {
      this.position = position;
   }
}
