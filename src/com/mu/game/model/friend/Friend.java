package com.mu.game.model.friend;

import com.mu.game.CenterManager;
import com.mu.game.model.gang.Gang;
import com.mu.game.model.gang.GangManager;
import com.mu.game.model.gang.GangMember;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.Profession;
import com.mu.game.model.unit.player.bluevip.BlueVip;

public class Friend {
   private long id;
   private int type;
   private int level;
   private int profession;
   private int zdl;
   private int friendDegree;
   private long offlineTime;
   private String name;
   private int beKilledTimes;
   private long addTime;
   private String blueTag;
   private int serverId;

   public Friend(long id, int type) {
      this.friendDegree = 0;
      this.offlineTime = -1L;
      this.name = "";
      this.beKilledTimes = 0;
      this.addTime = System.currentTimeMillis();
      this.blueTag = "0";
      this.id = id;
      this.type = type;
   }

   public Friend(Player player, int type) {
      this(player.getID(), type);
      this.setLevel(player.getLevel());
      this.setProfession(player.getProfessionID());
      this.setZdl(player.getStatValue(StatEnum.DOMINEERING));
      this.setName(player.getName());
      this.serverId = player.getUser().getServerID();
      this.blueTag = player.getUser().getBlueVip().getTag();
   }

   public int getServerId() {
      return this.serverId;
   }

   public void setServerId(int serverId) {
      this.serverId = serverId;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getHeader() {
      Player player = this.getFriendPlayer();
      return player != null ? player.getHeader() : ((Profession)Profession.getProfessions().get(this.profession)).getHeader();
   }

   public long getId() {
      return this.id;
   }

   public int getType() {
      return this.type;
   }

   public Player getFriendPlayer() {
      return CenterManager.getPlayerByRoleID(this.id);
   }

   public boolean isOnline() {
      return this.getFriendPlayer() != null;
   }

   public int getLevel() {
      Player player = this.getFriendPlayer();
      if (player != null) {
         this.level = player.getLevel();
      }

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

   public int getZdl() {
      Player player = this.getFriendPlayer();
      if (player != null) {
         this.zdl = player.getStatValue(StatEnum.DOMINEERING);
      }

      return this.zdl;
   }

   public void setZdl(int zdl) {
      this.zdl = zdl;
   }

   public long getGangId() {
      GangMember member = GangManager.getMember(this.id);
      return member == null ? -1L : member.getGangId();
   }

   public int getFriendDegree() {
      return this.friendDegree;
   }

   public void setFriendDegree(int friendDegree) {
      this.friendDegree = friendDegree;
   }

   public long getOfflineTime() {
      return this.offlineTime;
   }

   public void setOfflineTime(long offlineTime) {
      this.offlineTime = offlineTime;
   }

   public String getGangName() {
      long gangId = this.getGangId();
      if (gangId == -1L) {
         return "";
      } else {
         Gang gang = GangManager.getGang(gangId);
         return gang == null ? "" : gang.getName();
      }
   }

   public int getBeKilledTimes() {
      return this.beKilledTimes;
   }

   public void setBeKilledTimes(int beKilledTimes) {
      this.beKilledTimes = beKilledTimes;
   }

   public void addBekilledTimes() {
      ++this.beKilledTimes;
   }

   public void addDegree() {
      ++this.friendDegree;
   }

   public long getAddTime() {
      return this.addTime;
   }

   public void setAddTime(long addTime) {
      this.addTime = addTime;
   }

   public String getBlueTag() {
      Player player = this.getFriendPlayer();
      if (player != null) {
         this.blueTag = player.getUser().getBlueVip().getTag();
      }

      return this.blueTag;
   }

   public void setBlueTag(String blueTag) {
      this.blueTag = blueTag;
   }

   public int[] getBlueIcons() {
      return BlueVip.getBlueIcon(this.getBlueTag()).getIcons();
   }
}
