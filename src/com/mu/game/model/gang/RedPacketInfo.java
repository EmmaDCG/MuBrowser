package com.mu.game.model.gang;

public class RedPacketInfo {
   private int id;
   private int type;
   private int ingotReq;
   private int ingot;
   private int[] detail;
   private String name;
   private String des;
   private boolean isBroadcast = false;
   private String broadcastContent = null;

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public int getType() {
      return this.type;
   }

   public void setType(int type) {
      this.type = type;
   }

   public int getIngotReq() {
      return this.ingotReq;
   }

   public void setIngotReq(int ingotReq) {
      this.ingotReq = ingotReq;
   }

   public int getIngot() {
      return this.ingot;
   }

   public void setIngot(int ingot) {
      this.ingot = ingot;
   }

   public int[] getDetail() {
      return this.detail;
   }

   public void setDetail(int[] detail) {
      this.detail = detail;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getDes() {
      return this.des;
   }

   public void setDes(String des) {
      this.des = des;
   }

   public boolean isBroadcast() {
      return this.isBroadcast;
   }

   public void setBroadcast(boolean isBroadcast) {
      this.isBroadcast = isBroadcast;
   }

   public String getBroadcastContent() {
      return this.broadcastContent;
   }

   public void setBroadcastContent(String broadcastContent) {
      this.broadcastContent = broadcastContent;
   }
}
