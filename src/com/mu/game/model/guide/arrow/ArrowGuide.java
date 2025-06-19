package com.mu.game.model.guide.arrow;

import com.mu.game.model.unit.player.Player;

public abstract class ArrowGuide {
   protected int arrowId;

   public ArrowGuide(int arrowId) {
      this.arrowId = arrowId;
   }

   public int getArrowId() {
      return this.arrowId;
   }

   public String getDefaultContent() {
      ArrowInfo info = ArrowGuideManager.getArrowInfo(this.arrowId);
      return info == null ? "" : info.getContent();
   }

   public abstract void pushArrow(Player var1, String var2);
}
