package com.mu.game.dungeon.map;

import com.mu.game.model.map.Map;
import com.mu.game.model.map.MapConfig;

public class SubMap extends Map {
   public SubMap(int referMapID) {
      super(referMapID);
      Map referMap = this.getReferMap();
      this.setName(referMap.getName());
      this.setWeather(referMap.getWeather());
      this.setMusic(referMap.getMusic());
      this.setWidth(referMap.getWidth());
      this.setHeight(referMap.getHeight());
      this.setLeft(referMap.getLeft());
      this.setTop(referMap.getTop());
      this.setTileHorizontalNumber(referMap.getTileHorizontalNumber());
      this.setTileVerticalNumber(referMap.getTileVerticalNumber());
      this.resetArea();
      referMap = null;
   }

   public void resetArea() {
      this.setAreaWidth(this.getWidth());
      this.setAreaHeight(this.getHeight());
      this.createArea();
   }

   public Map getReferMap() {
      return MapConfig.getDefaultMap(this.getID());
   }

   public boolean isBlocked(int x, int y) {
      return this.getReferMap().isBlocked(x, y);
   }

   public byte[][] getBlocks() {
      return this.getReferMap().getBlocks();
   }
}
