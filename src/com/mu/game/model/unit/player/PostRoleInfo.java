package com.mu.game.model.unit.player;

public class PostRoleInfo {
   private String name;
   private int serverId;
   private String userName;
   private int roleNumber;
   private long postTime = System.currentTimeMillis();
   private int level;
   private int todayOnlineTime;
   private int totalOnlineTime;
   private long rid;
   private int zdl;

   public PostRoleInfo() {
   }

   public PostRoleInfo(Player player) {
      this.name = player.getName();
      this.level = player.getLevel();
      this.userName = player.getUserName();
      this.roleNumber = player.getRoleNumber();
      this.todayOnlineTime = player.getTodayOnlineTime();
      this.totalOnlineTime = player.getTotalOnlineTime();
      this.serverId = player.getUser().getServerID();
      this.rid = player.getID();
      this.zdl = player.getWarComment();
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getServerId() {
      return this.serverId;
   }

   public void setServerId(int serverId) {
      this.serverId = serverId;
   }

   public String getUserName() {
      return this.userName;
   }

   public void setUserName(String userName) {
      this.userName = userName;
   }

   public int getRoleNumber() {
      return this.roleNumber;
   }

   public void setRoleNumber(int roleNumber) {
      this.roleNumber = roleNumber;
   }

   public long getPostTime() {
      return this.postTime;
   }

   public void setPostTime(long postTime) {
      this.postTime = postTime;
   }

   public int getLevel() {
      return this.level;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public int getTodayOnlineTime() {
      return this.todayOnlineTime;
   }

   public void setTodayOnlineTime(int todayOnlineTime) {
      this.todayOnlineTime = todayOnlineTime;
   }

   public int getTotalOnlineTime() {
      return this.totalOnlineTime;
   }

   public void setTotalOnlineTime(int totalOnlineTime) {
      this.totalOnlineTime = totalOnlineTime;
   }

   public long getRid() {
      return this.rid;
   }

   public void setRid(long rid) {
      this.rid = rid;
   }

   public int getZdl() {
      return this.zdl;
   }

   public void setZdl(int zdl) {
      this.zdl = zdl;
   }
}
