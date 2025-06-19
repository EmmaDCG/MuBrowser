package com.mu.game.model.dialog;

import com.mu.game.model.unit.npc.Npc;
import com.mu.game.model.unit.player.Player;

public abstract class DialogOption {
   private int id;
   private String name;
   private int icon;
   private DialogOptionType type;
   private int value;
   private boolean close;

   public void setId(int id) {
      this.id = id;
   }

   public int getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public DialogOptionType getType() {
      return this.type;
   }

   public void setType(DialogOptionType type) {
      this.type = type;
   }

   public int getValue() {
      return this.value;
   }

   public void setValue(int value) {
      this.value = value;
   }

   public boolean isClose() {
      return this.close;
   }

   public void setClose(boolean close) {
      this.close = close;
   }

   public int getIcon() {
      return this.icon;
   }

   public void setIcon(int icon) {
      this.icon = icon;
   }

   public abstract DialogOptionSee canSee(Player var1, Npc var2);

   public abstract void option(Player var1, Npc var2);
}
