package com.mu.game.model.team;

import com.mu.game.CenterManager;
import com.mu.game.model.unit.player.Player;
import com.mu.utils.geom.MathUtil;

public class Teammate {
   public static final int Status_Normal = 0;
   public static final int Status_Far = 1;
   public static final int Status_Offline = 2;
   private int teamId = 1;
   private long id;
   private long offlineTime = -1L;
   private int level = 1;
   private int header = -1;
   private String name = "";
   private int professionId = 1;
   private int warComment = 1;
   private int vipLevel = 0;
   private long time = System.currentTimeMillis();
   private String imgText = "";

   public Teammate(Player player, int teamId) {
      this.id = player.getID();
      this.teamId = teamId;
      this.level = player.getLevel();
      this.name = player.getName();
      this.header = player.getHeader();
      this.professionId = player.getProfessionID();
      this.warComment = player.getWarComment();
      this.vipLevel = player.getVipShowLevel();
      this.imgText = player.getUser().getBlueVip().getBlueIcon().getText();
   }

   public long getId() {
      return this.id;
   }

   public boolean isOnline() {
      return CenterManager.isOnline(this.id);
   }

   public Player getPlayer() {
      return CenterManager.getPlayerByRoleID(this.id);
   }

   public long getOfflineTime() {
      return this.offlineTime;
   }

   public void setOfflineTime(long offlineTime) {
      this.offlineTime = offlineTime;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public int getLevel() {
      Player player = this.getPlayer();
      return player == null ? this.level : player.getLevel();
   }

   public int getTeamId() {
      return this.teamId;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getHeader() {
      return this.header;
   }

   public int getProfessionId() {
      Player player = this.getPlayer();
      return player == null ? this.professionId : player.getProfessionID();
   }

   public int getVipLevel() {
      Player player = this.getPlayer();
      return player == null ? this.vipLevel : player.getVipShowLevel();
   }

   public int getWarComment() {
      Player player = this.getPlayer();
      return player == null ? this.warComment : player.getWarComment();
   }

   public void setWarComment(int warComment) {
      this.warComment = warComment;
   }

   public int getLine() {
      Player player = this.getPlayer();
      return player == null ? 0 : player.getMap().getLine();
   }

   public int getStatus(Player player) {
      if (player.getID() == this.id) {
         return 0;
      } else {
         Player p = this.getPlayer();
         if (p == null) {
            return 2;
         } else {
            return player.getMap().equals(p.getMap()) && MathUtil.getDistance(p.getPosition(), player.getPosition()) < 36000 ? 0 : 1;
         }
      }
   }

   public boolean isLeader() {
      Team team = TeamManager.getTeam(this.teamId);
      return team != null && team.isLeader(this.id);
   }

   public long getTime() {
      return this.time;
   }

   public String getImgText() {
      return this.imgText;
   }
}
