package com.mu.game.dungeon.imp.plot;

import com.mu.game.model.map.BigMonsterGroup;

public class PlotMonsterGroup extends BigMonsterGroup {
   private int plotId = 1;

   public int getPlotId() {
      return this.plotId;
   }

   public void setPlotId(int plotId) {
      this.plotId = plotId;
   }
}
