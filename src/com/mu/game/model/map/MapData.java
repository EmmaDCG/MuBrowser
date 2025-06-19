package com.mu.game.model.map;

import com.mu.game.model.item.Item;
import com.mu.game.model.map.enter.req.EnterMapRequirement;
import com.mu.game.model.unit.material.MaterialGroup;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.tp.TransPoint;
import com.mu.io.game.packet.imp.item.GetItemStats;
import com.mu.io.game.packet.imp.map.GetSmallMapElement;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MapData {
   private int mapID;
   private int modelID;
   private int smallID;
   private int defaultX = 0;
   private int defaultY = 0;
   private String mapName;
   private byte[] configData;
   private int music = -1;
   private int findWayID = 0;
   private int groupID = 1;
   private int[] icons = null;
   private boolean isWorldMap = true;
   private boolean isUndergroundPalace = false;
   private boolean canPk = true;
   private boolean canZening = true;
   private boolean isLine = true;
   private boolean isShow = true;
   private int worldMapX = 0;
   private int worldMapY = 0;
   private HashMap tpMap = new HashMap();
   private int nameImage = -1;
   private int mapNameIconId;
   private boolean canChangePkModel = true;
   private int lineNumber = 1;
   private boolean isPkPunish = true;
   private boolean isOpen = true;
   private int interMapType = 0;
   private ArrayList monsterList = new ArrayList();
   private ArrayList materialList = new ArrayList();
   private ArrayList npcList = new ArrayList();
   private ArrayList smElement = new ArrayList();
   private boolean isMainCity = false;
   private int cityPkModel = 0;
   private String bigMapDes = "";
   private GetSmallMapElement smallMapElementPacket = null;
   private int landscape = 1;
   private boolean checkLandscapeChange = false;
   protected HashMap diffrentLandscape = null;
   protected int configId = -1;
   protected ArrayList dropList = new ArrayList();
   protected String groupName = "";
   protected int[] mapGroup = null;
   private ArrayList reqList = new ArrayList();
   private int backMusic = -1;
   private int recommendLevel = 1;
   private int recommendMinLevel = 1;
   private int reqLevel = 1;
   protected static final int TransePointType = 5;

   public MapData(int mapID) {
      this.mapID = mapID;
   }

   public String getMapName() {
      return this.mapName;
   }

   public int[] getIcons() {
      return this.icons;
   }

   public void setIcons(int[] icons) {
      this.icons = icons;
   }

   public void setMapName(String mapName) {
      this.mapName = mapName;
   }

   public int getWorldMapX() {
      return this.worldMapX;
   }

   public void setWorldMapX(int worldMapX) {
      this.worldMapX = worldMapX;
   }

   public int getRecommendMinLevel() {
      return this.recommendMinLevel;
   }

   public void setRecommendMinLevel(int recommendMinLevel) {
      this.recommendMinLevel = recommendMinLevel;
   }

   public int getRecommendLevel() {
      return this.recommendLevel;
   }

   public void setRecommendLevel(int recommendLevel) {
      this.recommendLevel = recommendLevel;
   }

   public int getWorldMapY() {
      return this.worldMapY;
   }

   public void setWorldMapY(int worldMapY) {
      this.worldMapY = worldMapY;
   }

   public int getGroupID() {
      return this.groupID;
   }

   public void setGroupID(int groupID) {
      this.groupID = groupID;
   }

   public int getModelID() {
      return this.modelID;
   }

   public void setModelID(int modelID) {
      this.modelID = modelID;
   }

   public int getSmallID() {
      return this.smallID;
   }

   public void setSmallID(int smallID) {
      this.smallID = smallID;
   }

   public byte[] getConfigData() {
      return this.configData;
   }

   public void setConfigData(byte[] data) {
      this.configData = data;
   }

   public int getMusic() {
      return this.music;
   }

   public void setMusic(int music) {
      this.music = music;
      if (this.music < 0) {
         this.music = 0;
      }

   }

   public int getMapID() {
      return this.mapID;
   }

   public boolean isCanPk() {
      return this.canPk;
   }

   public void setCanPk(boolean canPk) {
      this.canPk = canPk;
   }

   public boolean isLine() {
      return this.isLine;
   }

   public void setLine(boolean isLine) {
      this.isLine = isLine;
   }

   public HashMap getTransPointMap() {
      return this.tpMap;
   }

   public void addTransPoint(TranspointData tp) {
      this.tpMap.put(tp.getId(), tp);
   }

   public final int getInterMapType() {
      return this.interMapType;
   }

   public final void setInterMapType(int interMapType) {
      this.interMapType = interMapType;
   }

   public boolean isWorldMap() {
      return this.isWorldMap;
   }

   public void setWorldMap(boolean isWorldMap) {
      this.isWorldMap = isWorldMap;
   }

   public final int getDefaultX() {
      return this.defaultX;
   }

   public final void setDefaultX(int defaultX) {
      this.defaultX = defaultX;
   }

   public final int getDefaultY() {
      return this.defaultY;
   }

   public final void setDefaultY(int defaultY) {
      this.defaultY = defaultY;
   }

   public int getMapNameIconId() {
      return this.mapNameIconId;
   }

   public void setMapNameIconId(int mapNameIconId) {
      this.mapNameIconId = mapNameIconId;
   }

   public boolean isCanZening() {
      return this.canZening;
   }

   public void setCanZening(boolean canZening) {
      this.canZening = canZening;
   }

   public boolean isUndergroundPalace() {
      return this.isUndergroundPalace;
   }

   public void setUndergroundPalace(boolean isUndergroundPalace) {
      this.isUndergroundPalace = isUndergroundPalace;
   }

   public int getFindWayID() {
      return this.findWayID;
   }

   public void setFindWayID(int findWayID) {
      this.findWayID = findWayID;
   }

   public int getNameImage() {
      return this.nameImage;
   }

   public void setNameImage(int img) {
      this.nameImage = img;
   }

   public boolean isCanChangePkModel() {
      return this.canChangePkModel;
   }

   public void setCanChangePkModel(boolean canChangePkModel) {
      this.canChangePkModel = canChangePkModel;
   }

   public int getLineNumber() {
      return this.lineNumber;
   }

   public void setLineNumber(int lineNumber) {
      this.lineNumber = lineNumber;
   }

   public boolean isPkPunish() {
      return this.isPkPunish;
   }

   public void setPkPunish(boolean isPkPunish) {
      this.isPkPunish = isPkPunish;
   }

   public boolean isOpen() {
      return this.isOpen;
   }

   public void setOpen(boolean isOpen) {
      this.isOpen = isOpen;
   }

   public void addMaterialGroup(MaterialGroup mg) {
      this.materialList.add(mg);
   }

   public ArrayList getMaterialList() {
      return this.materialList;
   }

   public int getConfigId() {
      return this.configId;
   }

   public void setConfigId(int configId) {
      this.configId = configId;
   }

   public void addMonsterGroup(BigMonsterGroup mg) {
      this.monsterList.add(mg);
   }

   public ArrayList getMonsterList() {
      return this.monsterList;
   }

   public void addNpcInfo(NpcInfo info) {
      this.npcList.add(info);
   }

   public ArrayList getNpcList() {
      return this.npcList;
   }

   public boolean isMainCity() {
      return this.isMainCity;
   }

   public void setMainCity(boolean isMainCity) {
      this.isMainCity = isMainCity;
   }

   public int getLandscape() {
      return this.landscape;
   }

   public void setLandscape(int landscape) {
      this.landscape = landscape;
   }

   public int getCityPkModel() {
      return this.cityPkModel;
   }

   public void setCityPkModel(int cityPkModel) {
      this.cityPkModel = cityPkModel;
   }

   public String getBigMapDes() {
      return this.bigMapDes;
   }

   public void setBigMapDes(String bigMapDes) {
      this.bigMapDes = bigMapDes;
   }

   public void addSmallMapElement(SmallMapElement se) {
      this.smElement.add(se);
   }

   public boolean isCheckLandscapeChange() {
      return this.checkLandscapeChange;
   }

   public void setCheckLandscapeChange(boolean checkLandscapeChange) {
      this.checkLandscapeChange = checkLandscapeChange;
   }

   public GetSmallMapElement getSmallMapElementPacket() {
      if (this.smallMapElementPacket == null) {
         this.initSmallElementMapPacket();
      }

      return this.smallMapElementPacket;
   }

   public void addDifLandscape(Shape shape, int type) {
      if (this.diffrentLandscape == null) {
         this.diffrentLandscape = new HashMap();
      }

      this.diffrentLandscape.put(shape, type);
   }

   public HashMap getDiffrentLandscape() {
      return this.diffrentLandscape;
   }

   private void initSmallElementMapPacket() {
      this.smallMapElementPacket = new GetSmallMapElement();

      try {
         Map map = MapConfig.getDefaultMap(this.mapID);
         this.smallMapElementPacket.writeShort(this.mapID);
         this.smallMapElementPacket.writeByte(this.smElement.size() + map.getTransPointMap().size());
         Iterator var3 = this.smElement.iterator();

         while(var3.hasNext()) {
            SmallMapElement se = (SmallMapElement)var3.next();
            this.smallMapElementPacket.writeDouble((double)se.getId());
            this.smallMapElementPacket.writeUTF(se.getName());
            this.smallMapElementPacket.writeInt(se.getX());
            this.smallMapElementPacket.writeInt(se.getY());
            this.smallMapElementPacket.writeByte(se.getType());
         }

         Iterator it = map.getTransPointMap().values().iterator();

         while(it.hasNext()) {
            TransPoint tp = (TransPoint)it.next();
            this.smallMapElementPacket.writeDouble((double)tp.getID());
            this.smallMapElementPacket.writeUTF(tp.getName());
            this.smallMapElementPacket.writeInt(tp.getX());
            this.smallMapElementPacket.writeInt(tp.getY());
            this.smallMapElementPacket.writeByte(5);
         }

         this.smallMapElementPacket.writeByte(this.dropList.size());
         var3 = this.dropList.iterator();

         while(var3.hasNext()) {
            Item item = (Item)var3.next();
            GetItemStats.writeItem(item, this.smallMapElementPacket);
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public String getGroupName() {
      return this.groupName;
   }

   public void setGroupName(String groupName) {
      this.groupName = groupName;
   }

   public boolean isShow() {
      return this.isShow;
   }

   public void setShow(boolean isShow) {
      this.isShow = isShow;
   }

   public void addDropItem(Item item) {
      this.dropList.add(item);
   }

   public int[] getMapGroup() {
      return this.mapGroup;
   }

   public void setMapGroup(int[] mapGroup) {
      this.mapGroup = mapGroup;
   }

   public void addEnterMapRequirement(EnterMapRequirement req) {
      this.reqList.add(req);
   }

   public boolean canEnter(Player player, boolean pushMessage) {
      Iterator var4 = this.reqList.iterator();

      while(var4.hasNext()) {
         EnterMapRequirement req = (EnterMapRequirement)var4.next();
         if (!req.canEnter(player, pushMessage)) {
            return false;
         }
      }

      return true;
   }

   public boolean needWing() {
      Iterator var2 = this.reqList.iterator();

      while(var2.hasNext()) {
         EnterMapRequirement req = (EnterMapRequirement)var2.next();
         if (req instanceof EnterMapRequirement) {
            return true;
         }
      }

      return false;
   }

   public int getBackMusic() {
      return this.backMusic;
   }

   public void setBackMusic(int backMusic) {
      this.backMusic = backMusic;
   }

   public int getReqLevel() {
      return this.reqLevel;
   }

   public void setReqLevel(int reqLevel) {
      this.reqLevel = reqLevel;
   }
}
