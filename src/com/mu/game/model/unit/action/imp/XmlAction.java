package com.mu.game.model.unit.action.imp;

import com.mu.game.model.unit.action.Action;

public abstract class XmlAction implements Action {
   private int id;

   public XmlAction(int id) {
      this.id = id;
   }

   public int getId() {
      return this.id;
   }

   public abstract void initAction(String var1);
}
