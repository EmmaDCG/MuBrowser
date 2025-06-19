package com.mu.game.model.map;

import com.mu.config.Global;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LineMap {
   private Map[] maps = null;
   private int mapID = 1;
   private boolean isCreateLine = true;

   public LineMap(Map firstMap) throws Exception {
      this.mapID = firstMap.getID();
      MapData data = MapConfig.getMapData(this.mapID);
      int lineNumber = data.getLineNumber();
      if (lineNumber < 1) {
         lineNumber = 1;
      }

      if (lineNumber > 1 && Global.isLine()) {
         this.maps = new Map[lineNumber];
         this.maps[0] = firstMap;

         for(int i = 1; i < lineNumber; ++i) {
            this.maps[i] = MapConfig.createMap(data, i);
         }
      } else {
         this.maps = new Map[1];
         this.maps[0] = firstMap;
      }

   }

   public int getMapID() {
      return this.mapID;
   }

   public boolean isCreateLine() {
      return this.isCreateLine;
   }

   public Map getMapByLine(int line) {
      return line >= 0 && line < this.maps.length ? this.maps[line] : null;
   }

   public Map getFirstMap() {
      return this.getMapByLine(0);
   }

   public Map[] getAllMaps() {
      return this.maps;
   }

   public Map getAutoSwitchMap() {
      if (this.maps.length == 1) {
         return this.getFirstMap();
      } else {
         Map map = null;

         final Comparator<Map> com = new Comparator<Map>() {
            @Override
            public int compare(final Map o1, final Map o2) {
               final int size1 = o1.getPlayerSize();
               final int size2 = o2.getPlayerSize();
               return (size1 < size2) ? -1 : 1;
            }
         };
         ArrayList list = new ArrayList();

         for(int i = 0; i < this.maps.length; ++i) {
            list.add(this.maps[i]);
         }

         Collections.sort(list, com);
         map = (Map)list.get(0);
         list.clear();
         list = null;
         return map;
      }
   }

   public Map getEarlyMap() {
      if (this.maps.length == 1) {
         return this.getFirstMap();
      } else {
         for(int i = 0; i < this.maps.length; ++i) {
            Map tmpMap = this.maps[i];
            if (tmpMap != null) {
               int playerSize = tmpMap.getPlayerSize();
               if (playerSize <= 100) {
                  return tmpMap;
               }
            }
         }

         return this.getAutoSwitchMap();
      }
   }
}
