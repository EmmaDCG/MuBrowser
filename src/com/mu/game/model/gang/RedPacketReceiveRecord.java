package com.mu.game.model.gang;

public class RedPacketReceiveRecord {
   private long packetId;
   private long rid;
   private String roleName;
   private long receiveTime;
   private long receiveDay;
   private int receiveIngot;

   public long getPacketId() {
      return this.packetId;
   }

   public void setPacketId(long packetId) {
      this.packetId = packetId;
   }

   public long getRid() {
      return this.rid;
   }

   public void setRid(long rid) {
      this.rid = rid;
   }

   public String getRoleName() {
      return this.roleName;
   }

   public void setRoleName(String roleName) {
      this.roleName = roleName;
   }

   public long getReceiveTime() {
      return this.receiveTime;
   }

   public void setReceiveTime(long receiveTime) {
      this.receiveTime = receiveTime;
   }

   public long getReceiveDay() {
      return this.receiveDay;
   }

   public void setReceiveDay(long receiveDay) {
      this.receiveDay = receiveDay;
   }

   public int getReceiveIngot() {
      return this.receiveIngot;
   }

   public void setReceiveIngot(int receiveIngot) {
      this.receiveIngot = receiveIngot;
   }
}
