package com.mu.game.model.gang;

import com.mu.game.CenterManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.bluevip.BlueVip;

public class ApplyInfo {
   private long roleId;
   private String name;
   private long applyTime;
   private int level;
   private int profession;
   private int vipLevel = 1;
   private String userName = null;
   private String vipTag = "0";

   public long getRoleId() {
      return this.roleId;
   }

   public void setRoleId(long roleId) {
      this.roleId = roleId;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public long getApplyTime() {
      return this.applyTime;
   }

   public void setApplyTime(long applyTime) {
      this.applyTime = applyTime;
   }

   public int getLevel() {
      Player player = CenterManager.getPlayerByRoleID(this.roleId);
      return player != null ? player.getLevel() : this.level;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public int getProfession() {
      Player player = CenterManager.getPlayerByRoleID(this.roleId);
      return player != null ? player.getProfessionID() : this.profession;
   }

   public void setProfession(int profession) {
      this.profession = profession;
   }

   public int getVipLevel() {
      return this.vipLevel;
   }

   public void setVipLevel(int vipLevel) {
      this.vipLevel = vipLevel;
   }

   public String getUserName() {
      return this.userName;
   }

   public boolean isOnline() {
      return CenterManager.isOnline(this.roleId);
   }

   public void setUserName(String userName) {
      this.userName = userName;
   }

   public String getVipTag() {
      return this.vipTag;
   }

   public void setVipTag(String vipTag) {
      this.vipTag = vipTag;
   }

   public int[] getVipIcons() {
      Player player = CenterManager.getPlayerByRoleID(this.roleId);
      return player != null ? player.getVipIcons() : BlueVip.getBlueIcon(this.getVipTag()).getIcons();
   }
}
