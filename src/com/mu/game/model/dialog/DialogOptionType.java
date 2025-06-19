package com.mu.game.model.dialog;

import com.mu.game.model.dialog.options.DialogOptionDungeon;
import com.mu.game.model.dialog.options.DialogOptionNone;
import com.mu.game.model.dialog.options.DialogOptionShop;
import com.mu.game.model.dialog.options.DialogOptionStorehouse;
import com.mu.game.model.dialog.options.DialogOptionSwitchServer;
import com.mu.game.model.dialog.options.DialogOptionTask;

public enum DialogOptionType {
   DOT_NONE(0, "NULL", DialogOptionNone.class),
   DOT_SHOP(1, "SHOP", DialogOptionShop.class),
   DOT_SS(2, "SwitchServer", DialogOptionSwitchServer.class),
   DOT_TASK(3, "Task", DialogOptionTask.class),
   DOT_Dungeon(4, "Dungeon", DialogOptionDungeon.class),
   DOT_Storehouse(5, "Dungeon", DialogOptionStorehouse.class);

   private int id;
   private String name;
   private Class clazz;

   private DialogOptionType(int id, String name, Class clazz) {
      this.id = id;
      this.name = name;
      this.clazz = clazz;
   }

   public String getName() {
      return this.name;
   }

   public int getId() {
      return this.id;
   }

   public static DialogOptionType valueOf(int id) {
      DialogOptionType[] var4;
      int var3 = (var4 = values()).length;

      for(int var2 = 0; var2 < var3; ++var2) {
         DialogOptionType type = var4[var2];
         if (type.getId() == id) {
            return type;
         }
      }

      return null;
   }

   public DialogOption newInstance() {
      try {
         return (DialogOption)this.clazz.newInstance();
      } catch (Exception var2) {
         var2.printStackTrace();
         return null;
      }
   }
}
