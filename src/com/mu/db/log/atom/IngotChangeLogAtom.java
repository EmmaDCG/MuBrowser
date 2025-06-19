package com.mu.db.log.atom;

import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class IngotChangeLogAtom {
   private String userName;
   private long roleID;
   private String roleName;
   private int ingot;
   private int changeType;
   private String changeTime;
   private int roleServerID;
   private String changeDetail = "";

   public IngotChangeLogAtom(String userName, long roleID, String roleName, int ingot, int changeType, String changeTime, int roleServerID, String changeDetail) {
      this.userName = userName;
      this.roleID = roleID;
      this.roleName = roleName;
      this.ingot = ingot;
      this.changeType = changeType;
      this.changeTime = changeTime;
      this.roleServerID = roleServerID;
      this.changeDetail = changeDetail;
   }

   public static IngotChangeLogAtom createLogAtom(Game2GatewayPacket packet) throws Exception {
      long roleID = packet.readLong();
      String roleName = packet.readUTF();
      String userName = packet.readUTF();
      int ingot = packet.readInt();
      int changeType = packet.readShort();
      String changeTime = packet.readUTF();
      int roleServerID = packet.readInt();
      String changeDetail = packet.readUTF();
      IngotChangeLogAtom logAtom = new IngotChangeLogAtom(userName, roleID, roleName, ingot, changeType, changeTime, roleServerID, changeDetail);
      return logAtom;
   }

   public int getRoleServerID() {
      return this.roleServerID;
   }

   public void setRoleServerID(int roleServerID) {
      this.roleServerID = roleServerID;
   }

   public String getUserName() {
      return this.userName;
   }

   public void setUserName(String userName) {
      this.userName = userName;
   }

   public long getRoleID() {
      return this.roleID;
   }

   public void setRoleID(long roleID) {
      this.roleID = roleID;
   }

   public String getRoleName() {
      return this.roleName;
   }

   public void setRoleName(String roleName) {
      this.roleName = roleName;
   }

   public int getIngot() {
      return this.ingot;
   }

   public void setIngot(int ingot) {
      this.ingot = ingot;
   }

   public int getChangeType() {
      return this.changeType;
   }

   public void setChangeType(int changeType) {
      this.changeType = changeType;
   }

   public String getChangeTime() {
      return this.changeTime;
   }

   public void setChangeTime(String changeTime) {
      this.changeTime = changeTime;
   }

   public String getChangeDetail() {
      return this.changeDetail;
   }

   public void setChangeDetail(String changeDetail) {
      this.changeDetail = changeDetail;
   }
}
