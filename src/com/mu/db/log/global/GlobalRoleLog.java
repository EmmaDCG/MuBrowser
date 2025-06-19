package com.mu.db.log.global;

import com.mu.game.model.unit.player.Player;

public class GlobalRoleLog {
   private long rid;
   private String name;
   private String userName;
   private int serverId;
   private String createTime = "";
   private int level;
   private int profession;
   private int professionLevel;
   private int bindIngot = 0;

   public static GlobalRoleLog createLog(Player player) {
      GlobalRoleLog log = new GlobalRoleLog();
      log.setRid(player.getID());
      log.setUserName(player.getUserName());
      log.setLevel(player.getLevel());
      log.setName(player.getName());
      log.setProfession(player.getProType());
      log.setProfessionLevel(player.getProLevel());
      log.setServerId(player.getUser().getServerID());
      log.setBindIngot(player.getBindIngot());
      return log;
   }

   public int getBindIngot() {
      return this.bindIngot;
   }

   public void setBindIngot(int bindIngot) {
      this.bindIngot = bindIngot;
   }

   public long getRid() {
      return this.rid;
   }

   public void setRid(long rid) {
      this.rid = rid;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getUserName() {
      return this.userName;
   }

   public void setUserName(String userName) {
      this.userName = userName;
   }

   public int getServerId() {
      return this.serverId;
   }

   public void setServerId(int serverId) {
      this.serverId = serverId;
   }

   public String getCreateTime() {
      return this.createTime;
   }

   public void setCreateTime(String createTime) {
      this.createTime = createTime;
   }

   public int getLevel() {
      return this.level;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public int getProfession() {
      return this.profession;
   }

   public void setProfession(int profession) {
      this.profession = profession;
   }

   public int getProfessionLevel() {
      return this.professionLevel;
   }

   public void setProfessionLevel(int professionLevel) {
      this.professionLevel = professionLevel;
   }
}
