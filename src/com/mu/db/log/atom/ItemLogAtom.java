package com.mu.db.log.atom;

import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import com.mu.utils.Time;

public class ItemLogAtom {
   public static int Type_Add = 1;
   public static int Type_Delete = 2;
   private int type;
   private long itemId;
   private int modelID;
   private String itemName;
   private int quality;
   private int count;
   private int starLevel;
   private int zhuijiaLevel;
   private int socket;
   private boolean bind;
   private long expireTime;
   private int durability;
   private String statStr;
   private String stones;
   private String runes;
   private long roleID;
   private String nickName;
   private int source;
   private String logTime;

   public static ItemLogAtom createItemLog(Game2GatewayPacket packet) {
      ItemLogAtom iLog = null;

      try {
         int type = packet.readByte();
         long itemID = packet.readLong();
         int modelID = packet.readInt();
         String itemName = packet.readUTF();
         int quality = packet.readByte();
         int count = packet.readInt();
         int starLevel = packet.readByte();
         int zhuijiaLevel = packet.readByte();
         int socket = packet.readByte();
         boolean bind = packet.readBoolean();
         long expierTime = packet.readLong();
         int durability = packet.readInt();
         String statStr = packet.readUTF();
         String stones = packet.readUTF();
         String runes = packet.readUTF();
         long roleID = packet.readLong();
         String nickName = packet.readUTF();
         int source = packet.readByte();
         iLog = new ItemLogAtom();
         iLog.setType(type);
         iLog.setItemId(itemID);
         iLog.setModelID(modelID);
         iLog.setItemName(itemName);
         iLog.setQuality(quality);
         iLog.setCount(count);
         iLog.setStarLevel(starLevel);
         iLog.setZhuijiaLevel(zhuijiaLevel);
         iLog.setSocket(socket);
         iLog.setBind(bind);
         iLog.setExpireTime(expierTime);
         iLog.setDurability(durability);
         iLog.setStatStr(statStr);
         iLog.setStones(stones);
         iLog.setRunes(runes);
         iLog.setRoleID(roleID);
         iLog.setNickName(nickName);
         iLog.setSource(source);
         iLog.setLogTime(Time.getTimeStr());
      } catch (Exception var23) {
         var23.printStackTrace();
      }

      return iLog;
   }

   public int getType() {
      return this.type;
   }

   public void setType(int type) {
      this.type = type;
   }

   public String getItemName() {
      return this.itemName;
   }

   public void setItemName(String itemName) {
      this.itemName = itemName;
   }

   public long getItemId() {
      return this.itemId;
   }

   public void setItemId(long itemId) {
      this.itemId = itemId;
   }

   public int getModelID() {
      return this.modelID;
   }

   public void setModelID(int modelID) {
      this.modelID = modelID;
   }

   public int getQuality() {
      return this.quality;
   }

   public void setQuality(int quality) {
      this.quality = quality;
   }

   public int getCount() {
      return this.count;
   }

   public void setCount(int count) {
      this.count = count;
   }

   public int getStarLevel() {
      return this.starLevel;
   }

   public void setStarLevel(int starLevel) {
      this.starLevel = starLevel;
   }

   public int getZhuijiaLevel() {
      return this.zhuijiaLevel;
   }

   public void setZhuijiaLevel(int zhuijiaLevel) {
      this.zhuijiaLevel = zhuijiaLevel;
   }

   public int getSocket() {
      return this.socket;
   }

   public void setSocket(int socket) {
      this.socket = socket;
   }

   public boolean isBind() {
      return this.bind;
   }

   public void setBind(boolean bind) {
      this.bind = bind;
   }

   public long getExpireTime() {
      return this.expireTime;
   }

   public void setExpireTime(long expireTime) {
      this.expireTime = expireTime;
   }

   public int getDurability() {
      return this.durability;
   }

   public void setDurability(int durability) {
      this.durability = durability;
   }

   public String getStatStr() {
      return this.statStr;
   }

   public void setStatStr(String statStr) {
      this.statStr = statStr;
   }

   public String getStones() {
      return this.stones;
   }

   public void setStones(String stones) {
      this.stones = stones;
   }

   public String getRunes() {
      return this.runes;
   }

   public void setRunes(String runes) {
      this.runes = runes;
   }

   public long getRoleID() {
      return this.roleID;
   }

   public void setRoleID(long roleID) {
      this.roleID = roleID;
   }

   public String getNickName() {
      return this.nickName;
   }

   public void setNickName(String nickName) {
      this.nickName = nickName;
   }

   public int getSource() {
      return this.source;
   }

   public void setSource(int source) {
      this.source = source;
   }

   public String getLogTime() {
      return this.logTime;
   }

   public void setLogTime(String logTime) {
      this.logTime = logTime;
   }
}
