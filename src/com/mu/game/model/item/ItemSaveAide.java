package com.mu.game.model.item;

import com.mu.io.game.packet.ReadAndWritePacket;

public class ItemSaveAide {
   private long itemId;
   private int modelID;
   private int quality;
   private int count;
   private int slot;
   private int containerType;
   private int starLevel;
   private int socket;
   private boolean bind;
   private int money;
   private int moneyType;
   private int starUpTimes;
   private int onceMaxStarLevel;
   private long expireTime;
   private int durability;
   private String basisStats;
   private String otherStats;
   private String stones;
   private String runes;
   private int zhuijiaLevel;

   public ItemSaveAide() {
   }

   public ItemSaveAide(long itemId, int modelID, int quality, int count, int slot, int containerType, int starLevel, int socket, boolean bind, int money, int moneyType, int starUpTimes, int onceMaxStarLevel, long expireTime, int durability, String basisStats, String otherStats, String stones, String runes, int zhuijiaLevel) {
      this.itemId = itemId;
      this.modelID = modelID;
      this.quality = quality;
      this.count = count;
      this.slot = slot;
      this.containerType = containerType;
      this.starLevel = starLevel;
      this.socket = socket;
      this.bind = bind;
      this.money = money;
      this.moneyType = moneyType;
      this.starUpTimes = starUpTimes;
      this.onceMaxStarLevel = onceMaxStarLevel;
      this.expireTime = expireTime;
      this.durability = durability;
      this.basisStats = basisStats;
      this.otherStats = otherStats;
      this.stones = stones;
      this.runes = runes;
      this.zhuijiaLevel = zhuijiaLevel;
   }

   public static ItemSaveAide createSaveAide(Item item) {
      ItemSaveAide isa = new ItemSaveAide(item.getID(), item.getModelID(), item.getQuality(), item.getCount(), item.getSlot(), item.getContainerType(), item.getStarLevel(), item.getSocket(), item.isBind(), item.getMoney(), item.getMoneyType(), item.getStarUpTimes(), item.getOnceMaxStarLevel(), item.getExpireTime(), item.getDurability(), item.getBasisStr(), item.getOtherStr(), item.getStoneStr(), item.getRuneStr(), item.getZhuijiaLevel());
      if (item.isEquipment()) {
         isa.setBasisStats("");
      }

      return isa;
   }

   public static ItemSaveAide createSaveAide(ReadAndWritePacket packet) throws Exception {
      long itemId = packet.readLong();
      int modelID = packet.readInt();
      int quality = packet.readByte();
      int count = packet.readInt();
      int slot = packet.readShort();
      int containerType = packet.readByte();
      int starLevel = packet.readByte();
      int socket = packet.readByte();
      boolean bind = packet.readBoolean();
      int money = packet.readInt();
      int moneyType = packet.readByte();
      int starUpTimes = packet.readInt();
      int onceMaxStarLevel = packet.readByte();
      long expireTime = packet.readLong();
      int durability = packet.readShort();
      String basisStats = packet.readUTF();
      String otherStats = packet.readUTF();
      String stones = packet.readUTF();
      String runes = packet.readUTF();
      int zhuijiaLevel = packet.readByte();
      ItemSaveAide isa = new ItemSaveAide(itemId, modelID, quality, count, slot, containerType, starLevel, socket, bind, money, moneyType, starUpTimes, onceMaxStarLevel, expireTime, durability, basisStats, otherStats, stones, runes, zhuijiaLevel);
      return isa;
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

   public int getCount() {
      return this.count;
   }

   public void setCount(int count) {
      this.count = count;
   }

   public int getSlot() {
      return this.slot;
   }

   public void setSlot(int slot) {
      this.slot = slot;
   }

   public int getContainerType() {
      return this.containerType;
   }

   public void setContainerType(int containerType) {
      this.containerType = containerType;
   }

   public int getStarLevel() {
      return this.starLevel;
   }

   public void setStarLevel(int starLevel) {
      this.starLevel = starLevel;
   }

   public int getQuality() {
      return this.quality;
   }

   public void setQuality(int quality) {
      this.quality = quality;
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

   public int getMoney() {
      return this.money;
   }

   public void setMoney(int money) {
      this.money = money;
   }

   public int getMoneyType() {
      return this.moneyType;
   }

   public void setMoneyType(int moneyType) {
      this.moneyType = moneyType;
   }

   public int getStarUpTimes() {
      return this.starUpTimes;
   }

   public void setStarUpTimes(int starUpTimes) {
      this.starUpTimes = starUpTimes;
   }

   public int getOnceMaxStarLevel() {
      return this.onceMaxStarLevel;
   }

   public void setOnceMaxStarLevel(int onceMaxStarLevel) {
      this.onceMaxStarLevel = onceMaxStarLevel;
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

   public String getBasisStats() {
      return this.basisStats;
   }

   public void setBasisStats(String basisStats) {
      this.basisStats = basisStats;
   }

   public String getOtherStats() {
      return this.otherStats;
   }

   public void setOtherStats(String otherStats) {
      this.otherStats = otherStats;
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

   public int getZhuijiaLevel() {
      return this.zhuijiaLevel;
   }

   public void setZhuijiaLevel(int zhuijiaLevel) {
      this.zhuijiaLevel = zhuijiaLevel;
   }
}
