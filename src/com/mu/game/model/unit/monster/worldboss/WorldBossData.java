package com.mu.game.model.unit.monster.worldboss;

import com.mu.game.model.item.Item;
import com.mu.game.model.map.BigMonsterGroup;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class WorldBossData extends BigMonsterGroup {
   private int bossId;
   private int mapId;
   private String refreshRule;
   private String mapName;
   private int zoom;
   private int reqLevel;
   private int bossType = 1;
   private CopyOnWriteArrayList recordList = new CopyOnWriteArrayList();
   private ArrayList dropList = new ArrayList();
   private long telTime = 0L;

   public WorldBossData(int bossId) {
      this.bossId = bossId;
   }

   public int getMapId() {
      return this.mapId;
   }

   public void setMapId(int mapId) {
      this.mapId = mapId;
   }

   public int getBossId() {
      return this.bossId;
   }

   public long getTelTime() {
      return this.telTime;
   }

   public void setTelTime(long telTime) {
      this.telTime = telTime;
   }

   public void addDrop(Item item) {
      this.dropList.add(item);
   }

   public int getZoom() {
      return this.zoom;
   }

   public void setZoom(int zoom) {
      this.zoom = zoom;
   }

   public int getReqLevel() {
      return this.reqLevel;
   }

   public void setReqLevel(int reqLevel) {
      this.reqLevel = reqLevel;
   }

   public ArrayList getDropList() {
      return this.dropList;
   }

   public String getRefreshRule() {
      return this.refreshRule;
   }

   public void setRefreshRule(String refreshRule) {
      this.refreshRule = refreshRule;
   }

   public String getMapName() {
      return this.mapName;
   }

   public void setMapName(String mapName) {
      this.mapName = mapName;
   }

   public void addRecord(KillBossRecord record) {
      if (this.recordList.size() < 5) {
         this.recordList.add(record);
      } else {
         this.recordList.remove(0);
         this.recordList.add(record);
      }

   }

   public int getBossType() {
      return this.bossType;
   }

   public void setBossType(int bossType) {
      this.bossType = bossType;
   }

   public CopyOnWriteArrayList getRecordList() {
      return this.recordList;
   }
}
