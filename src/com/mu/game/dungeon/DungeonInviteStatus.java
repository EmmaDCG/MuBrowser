package com.mu.game.dungeon;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class DungeonInviteStatus {
   public static final int Status_None = -1;
   public static final int Status_NotConfirm = 0;
   public static final int Status_Confirm = 1;
   private static ConcurrentHashMap inviteMap = new ConcurrentHashMap(16, 0.75F, 2);
   private int teamId;
   private int dungeonId;
   private HashMap confirmRoles = new HashMap();

   public DungeonInviteStatus(int teamId, int dungeonId) {
      this.teamId = teamId;
      this.dungeonId = dungeonId;
   }

   public void destroy() {
      if (this.confirmRoles != null) {
         this.confirmRoles.clear();
         this.confirmRoles = null;
      }

   }

   public int getDungeonId() {
      return this.dungeonId;
   }

   public void setDungeonId(int dungeonId) {
      this.dungeonId = dungeonId;
   }

   public HashMap getConfirmRoles() {
      return this.confirmRoles;
   }

   public int getTeamId() {
      return this.teamId;
   }

   public void addConfirmRoles(Long rid) {
      this.confirmRoles.put(rid, true);
   }

   public boolean hasConfirm(long rid) {
      return this.confirmRoles.containsKey(rid);
   }

   public static boolean hasInvite(int teamId) {
      return inviteMap.containsKey(teamId);
   }

   public static void addDungeonInviteStatus(int teamId, DungeonInviteStatus status) {
      inviteMap.put(teamId, status);
   }

   public static DungeonInviteStatus getStatus(int teamId) {
      return (DungeonInviteStatus)inviteMap.get(teamId);
   }

   public static DungeonInviteStatus removeStatus(int teamId) {
      return (DungeonInviteStatus)inviteMap.remove(teamId);
   }
}
