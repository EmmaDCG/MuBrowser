package com.mu.game.model.drop.rara;

import com.mu.game.model.item.Item;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.ArrayList;
import java.util.Collections;

public abstract class RaraDrop implements Comparable {
   public static final int From_Boss = 1;
   public static final int From_Box = 2;
   private static ArrayList raraList = new ArrayList();
   private Item item = null;
   private long playerId;
   private String playerName;
   private long time = 0L;
   private String linkStr = null;

   public RaraDrop(Item item, long playerId, String playerName, long dropTime) {
      this.item = item;
      this.playerId = playerId;
      this.playerName = playerName;
      this.time = dropTime;
   }

   public abstract String toLinkStr();

   public abstract void writeDetail(WriteOnlyPacket var1) throws Exception;

   public Item getItem() {
      return this.item;
   }

   public long getPlayerId() {
      return this.playerId;
   }

   public String getPlayerName() {
      return this.playerName;
   }

   public long getTime() {
      return this.time;
   }

   public String getLinkStr() {
      if (this.linkStr == null) {
         this.linkStr = this.toLinkStr();
      }

      return this.linkStr;
   }

   public static void addRaraDrop(ArrayList drops) {
      ArrayList var1 = raraList;
      synchronized(raraList) {
         int totalCount = raraList.size() + drops.size();
         int result = totalCount - 50;
         if (result > 1) {
            for(int i = 0; i < result; ++i) {
               raraList.remove(i);
            }
         }

         Collections.sort(drops);
         raraList.addAll(drops);
      }
   }

   public static void addRaraDrop(RaraDrop drop) {
      ArrayList list = new ArrayList();
      list.add(drop);
      addRaraDrop(list);
      list.clear();
      list = null;
   }

   public int compareTo(RaraDrop o) {
      return this.getItem().getQuality() > o.getItem().getQuality() ? 1 : 0;
   }
}
