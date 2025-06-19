package com.mu.game.model.dialog;

public enum DialogOptionSee {
   HIDE(0, 0),
   VISIBLE(1, 0),
   VISIBLE_TASK_ACCEPT(2, 1),
   VISIBLE_TASK_VISIT(3, 2),
   VISIBLE_TASK_SUBMIT(4, 3);

   private int value;
   private int icon;

   private DialogOptionSee(int value, int icon) {
      this.value = value;
   }

   public int getValue() {
      return this.value;
   }

   public boolean is(DialogOptionSee see) {
      return this == see;
   }

   public boolean isVisible() {
      return this != HIDE;
   }

   public boolean isHide() {
      return this == HIDE;
   }

   public int getIcon() {
      return this.icon;
   }
}
