package com.mu.game.model.unit.ai;

import com.mu.game.model.unit.player.Player;

public class HurtStatisticsInfo implements Comparable {
   private boolean isTeam = false;
   private Player owner;
   private int teamId;
   private long hurt = 0L;

   public boolean isTeam() {
      return this.isTeam;
   }

   public void setTeam(boolean isTeam) {
      this.isTeam = isTeam;
   }

   public Player getOwner() {
      return this.owner;
   }

   public void setOwner(Player owner) {
      this.owner = owner;
   }

   public int getTeamId() {
      return this.teamId;
   }

   public void setTeamId(int teamId) {
      this.teamId = teamId;
   }

   public long getHurt() {
      return this.hurt;
   }

   public void setHurt(long hurt) {
      this.hurt = hurt;
   }

   public void addHurt(long hurt) {
      this.hurt += hurt;
   }

   public int compareTo(HurtStatisticsInfo o) {
      return this.hurt < o.hurt ? 1 : -1;
   }

   @Override
   public int compareTo(Object o) {
      return 0;
   }
}
