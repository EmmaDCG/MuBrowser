package com.mu.db.log.atom;

import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import com.mu.utils.Time;

public class ItemForgingLogAtom {
   public static int forging_success = 1;
   public static int forging_failure = 0;
   private int type;
   private long roleID;
   private String nickname;
   private long itemID;
   private int modelID;
   private String itemName;
   private int quality;
   private int preOperationLevel;
   private int newOperationLevel;
   private String stats;
   private String stones;
   private String runes;
   private String logTime;
   private int status;
   private int reduceIngot;
   private int reduceMoney;

   public static ItemForgingLogAtom createItemLog(Game2GatewayPacket packet) {
      ItemForgingLogAtom iLog = null;

      try {
         int type = packet.readByte();
         long itemID = packet.readLong();
         int modelID = packet.readInt();
         String itemName = packet.readUTF();
         int quality = packet.readByte();
         int preOperationLevel = packet.readByte();
         int newOperationLevel = packet.readByte();
         String statStr = packet.readUTF();
         String stones = packet.readUTF();
         String runes = packet.readUTF();
         int status = packet.readByte();
         int reduceIngot = packet.readInt();
         int reduceMoney = packet.readInt();
         long roleID = packet.readLong();
         String nickName = packet.readUTF();
         iLog = new ItemForgingLogAtom();
         iLog.setType(type);
         iLog.setItemID(itemID);
         iLog.setModelID(modelID);
         iLog.setItemName(itemName);
         iLog.setQuality(quality);
         iLog.setPreOperationLevel(preOperationLevel);
         iLog.setNewOperationLevel(newOperationLevel);
         iLog.setStats(statStr);
         iLog.setStones(stones);
         iLog.setRunes(runes);
         iLog.setRoleID(roleID);
         iLog.setNickname(nickName);
         iLog.setStatus(status);
         iLog.setReduceIngot(reduceIngot);
         iLog.setReduceMoney(reduceMoney);
         iLog.setLogTime(Time.getTimeStr());
      } catch (Exception var19) {
         var19.printStackTrace();
      }

      return iLog;
   }

   public int getType() {
      return this.type;
   }

   public void setType(int type) {
      this.type = type;
   }

   public long getItemID() {
      return this.itemID;
   }

   public void setItemID(long itemID) {
      this.itemID = itemID;
   }

   public int getModelID() {
      return this.modelID;
   }

   public void setModelID(int modelID) {
      this.modelID = modelID;
   }

   public String getItemName() {
      return this.itemName;
   }

   public void setItemName(String itemName) {
      this.itemName = itemName;
   }

   public int getQuality() {
      return this.quality;
   }

   public void setQuality(int quality) {
      this.quality = quality;
   }

   public int getPreOperationLevel() {
      return this.preOperationLevel;
   }

   public void setPreOperationLevel(int preOperationLevel) {
      this.preOperationLevel = preOperationLevel;
   }

   public int getNewOperationLevel() {
      return this.newOperationLevel;
   }

   public void setNewOperationLevel(int neOperationLevel) {
      this.newOperationLevel = neOperationLevel;
   }

   public String getStats() {
      return this.stats;
   }

   public void setStats(String stats) {
      this.stats = stats;
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

   public String getLogTime() {
      return this.logTime;
   }

   public void setLogTime(String logTime) {
      this.logTime = logTime;
   }

   public int getReduceIngot() {
      return this.reduceIngot;
   }

   public void setReduceIngot(int reduceIngot) {
      this.reduceIngot = reduceIngot;
   }

   public int getReduceMoney() {
      return this.reduceMoney;
   }

   public void setReduceMoney(int reduceMoney) {
      this.reduceMoney = reduceMoney;
   }

   public long getRoleID() {
      return this.roleID;
   }

   public void setRoleID(long roleID) {
      this.roleID = roleID;
   }

   public String getNickname() {
      return this.nickname;
   }

   public void setNickname(String nickname) {
      this.nickname = nickname;
   }

   public int getStatus() {
      return this.status;
   }

   public void setStatus(int status) {
      this.status = status;
   }
}
