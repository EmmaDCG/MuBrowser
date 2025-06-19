package com.mu.game.dungeon.imp.plot;

import com.mu.game.dungeon.Dungeon;
import com.mu.game.model.map.Map;
import com.mu.io.game.packet.imp.dungeon.DungeonResult;

public class Plot extends Dungeon {
   private int plotLevel = 0;

   public Plot(int id, PlotTemplate t, int plotLevel) {
      super(id, t);
      this.plotLevel = plotLevel;
      this.saveWhenInterrupt = false;
   }

   public void initMap() {
      PlotMap map = new PlotMap(this);
      this.addMap(map);
   }

   public PlotMap getPlotMap() {
      Map map = this.getFirstMap();
      return map == null ? null : (PlotMap)map;
   }

   public void checkTime() {
   }

   public PlotLevel getPlotLevel() {
      return ((PlotTemplate)this.getTemplate()).getPlotLevel(this.plotLevel);
   }

   public DungeonResult createSuccessPacket() {
      return null;
   }
}
