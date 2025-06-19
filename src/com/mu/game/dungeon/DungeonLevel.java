package com.mu.game.dungeon;

import com.mu.game.model.item.Item;
import java.util.ArrayList;
import java.util.TreeMap;

public class DungeonLevel {
   private int level;
   private String rewardStr = "";
   private String strength = "";
   private String levelStr = "";
   private Item reqItem = null;
   private ArrayList dropList = new ArrayList();
   private int minLevelReq = 1;
   private int maxLevelReq = 999;
   private int[] canSellItem = null;
   private TreeMap buyItemIndexMap = new TreeMap();
   private String shortcutBuyTitle = "";

   public int getLevel() {
      return this.level;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public String getRewardStr() {
      return this.rewardStr;
   }

   public void setRewardStr(String rewardStr) {
      this.rewardStr = rewardStr;
   }

   public String getStrength() {
      return this.strength;
   }

   public void setStrength(String strength) {
      this.strength = strength;
   }

   public String getLevelStr() {
      return this.levelStr;
   }

   public void setLevelStr(String levelStr) {
      this.levelStr = levelStr;
   }

   public Item getReqItem() {
      return this.reqItem;
   }

   public void setReqItem(Item reqItem) {
      this.reqItem = reqItem;
   }

   public ArrayList getDropList() {
      return this.dropList;
   }

   public void addDropItem(Item item) {
      this.dropList.add(item);
   }

   public int getMinLevelReq() {
      return this.minLevelReq;
   }

   public void setMinLevelReq(int minLevelReq) {
      this.minLevelReq = minLevelReq;
   }

   public int getMaxLevelReq() {
      return this.maxLevelReq;
   }

   public void setMaxLevelReq(int maxLevelReq) {
      this.maxLevelReq = maxLevelReq;
   }

   public int[] getCanSellItem() {
      return this.canSellItem;
   }

   public void setCanSellItem(int[] canSellItem) {
      this.canSellItem = canSellItem;
   }

   public void addBuyItem(int index, Item item) {
      this.buyItemIndexMap.put(index, item);
   }

   public String getShortcutBuyTitle() {
      return this.shortcutBuyTitle;
   }

   public void setShortcutBuyTitle(String shortcutBuyTitle) {
      this.shortcutBuyTitle = shortcutBuyTitle;
   }

   public TreeMap getBuyItemMap() {
      return this.buyItemIndexMap;
   }
}
