package com.mu.game.model.item.other.expriedHandel;

import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;

public abstract class ExpiredHandel {
   private int modelID;
   private int type;

   public ExpiredHandel(int modelID, int type) {
      this.modelID = modelID;
      this.type = type;
   }

   public abstract boolean doubleClickHandel(Player var1, Item var2);

   public abstract void instantCheckHandel(Player var1, Item var2, boolean var3);

   public abstract void initCheck() throws Exception;

   public int getModelID() {
      return this.modelID;
   }

   public void setModelID(int modelID) {
      this.modelID = modelID;
   }

   public int getType() {
      return this.type;
   }

   public void setType(int type) {
      this.type = type;
   }
}
