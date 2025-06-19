package com.mu.game.model.team;

import com.mu.config.MessageText;
import com.mu.game.CenterManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.imp.TeamInvitePopup;
import com.mu.io.game.packet.imp.player.pop.ShowPopup;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.io.game.packet.imp.team.TeamApplyInviteList;
import com.mu.io.game.packet.imp.team.TeamCreate;
import com.mu.io.game.packet.imp.team.TeamRefreshMateInfo;
import com.mu.utils.concurrent.ThreadFixedPoolManager;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

public class TeamManager {
   public static final int Team_Create = 1;
   public static final int Team_Active = 2;
   public static final int Team_Apply = 3;
   public static final int Team_AgreeInvite = 4;
   public static final int Team_Leave = 5;
   public static final int Team_KickOut = 6;
   public static final int Team_ChangeLeader = 7;
   public static final int Team_AgreeApply = 8;
   private static final int KeepTime = 600000;
   private static int teamId = 1;
   private static ConcurrentHashMap teamMap = new ConcurrentHashMap();
   private static ConcurrentHashMap offlineMate = new ConcurrentHashMap();
   public static final int STATE_NO_TEAM = 0;
   public static final int STATE_TEAMER = 1;
   public static final int STATE_TEAMELEADER = 2;
   private static ScheduledFuture broadcastTeammenberTask = null;
   private static ScheduledFuture offlineMateCheck = null;

   public static void startBroadcastTask() {
      broadcastTeammenberTask = ThreadFixedPoolManager.POOL_OTHER.scheduleTask(new Runnable() {
         public void run() {
            TeamManager.broadcastMenbers();
         }
      }, 5000L, 5000L);
      offlineMateCheck = ThreadFixedPoolManager.POOL_OTHER.scheduleTask(new Runnable() {
         public void run() {
            TeamManager.checkOfflineMate();
         }
      }, 5000L, 10000L);
   }

   public static synchronized int doOperation(int type, Player player, Object... obj) {
      switch(type) {
      case 1:
         return createNewTeam(player);
      case 2:
         return activeTeam(player, (Player)obj[0]);
      case 3:
         return applyInTeam(player, ((Integer)obj[0]).intValue());
      case 4:
         return agreeInvite(player, ((Long)obj[0]).longValue());
      case 5:
         return leaveTeam(player);
      case 6:
         return kickOut(player, ((Long)obj[0]).longValue());
      case 7:
         return changeLeader(player, ((Long)obj[0]).longValue());
      case 8:
         return agreeApply(player, ((Long)obj[0]).longValue());
      default:
         return 1;
      }
   }

   public static void stopTask() {
      if (broadcastTeammenberTask != null && !broadcastTeammenberTask.isCancelled()) {
         broadcastTeammenberTask.cancel(true);
      }

      if (offlineMateCheck != null && !offlineMateCheck.isCancelled()) {
         offlineMateCheck.cancel(true);
      }

   }

   public static void checkOfflineMate() {
      long now = System.currentTimeMillis();
      Iterator it = offlineMate.values().iterator();

      while(it.hasNext()) {
         Teammate mate = (Teammate)it.next();
         if (now - mate.getOfflineTime() >= 600000L) {
            Team team = getTeam(mate.getTeamId());
            if (team != null) {
               team.outTeamForOffline(mate.getId());
            }
         }
      }

   }

   private static void broadcastMenbers() {
      Iterator it = teamMap.values().iterator();

      while(it.hasNext()) {
         TeamRefreshMateInfo.refresh((Team)it.next());
      }

   }

   private static int getTeamID() {
      return teamId++;
   }

   public static void addOfflineMate(Teammate mate) {
      offlineMate.put(mate.getId(), mate);
   }

   public static Teammate removeOfflineMate(long roleID) {
      return (Teammate)offlineMate.remove(roleID);
   }

   public static Team getTeam(int id) {
      return (Team)teamMap.get(id);
   }

   public static void addTeam(Team team) {
      teamMap.put(team.getId(), team);
   }

   private static int createNewTeam(Player player) {
      int result = 1;
      if (player.getCurrentTeam() != null) {
         result = 19001;
      } else if (!player.getMap().canInTeam()) {
         result = 19027;
      } else {
         Team team = new Team(player, getTeamID());
         addTeam(team);
         team.inTeam(player, true);
      }

      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      }

      return result;
   }

   public static Team removeTeam(int id) {
      return (Team)teamMap.remove(id);
   }

   public static boolean isTeammate(Player p1, Player p2) {
      if (p1.getCurrentTeam() != null && p2.getCurrentTeam() != null) {
         return p1.getCurrentTeam().getId() == p2.getCurrentTeam().getId();
      } else {
         return false;
      }
   }

   private static int activeTeam(Player self, Player other) {
      Team teamSefl = self.getCurrentTeam();
      Team teamOther = other.getCurrentTeam();
      if (teamSefl == null && teamOther != null) {
         return applyInTeam(self, teamOther.getId());
      } else {
         if (teamSefl == null && teamOther == null) {
            createNewTeam(self);
         } else if (teamSefl != null && teamOther != null) {
            SystemMessage.writeMessage(self, MessageText.getText(19005).replace("%n%", other.getName()), 19005);
            return 19005;
         }

         return inviteTeam(self, other);
      }
   }

   private static int changeLeader(Player self, long rid) {
      Team team = self.getCurrentTeam();
      if (team == null) {
         return 1;
      } else if (!team.isLeader(self.getID())) {
         return 1;
      } else {
         int result = team.changeLeader(rid);
         if (result != 1) {
            SystemMessage.writeMessage(self, result);
         }

         return 1;
      }
   }

   private static int kickOut(Player self, long rid) {
      Team team = self.getCurrentTeam();
      if (team != null && self.getID() != rid && team.isLeader(self.getID())) {
         team.kickOut(rid);
         return 1;
      } else {
         return 1;
      }
   }

   private static int inviteTeam(Player self, Player other) {
      Team teamSefl = self.getCurrentTeam();
      if (teamSefl == null) {
         return 0;
      } else if (teamSefl.isFull()) {
         SystemMessage.writeMessage(self, 19004);
         return 0;
      } else {
         if (other.getSystemSetup().isAutoInTeamWhenBeInvite()) {
            teamSefl.agreeInvite(other);
         } else {
            SystemMessage.writeMessage(self, 19030);
            TeamApplyInviteList.addList(other, self.getID(), self.getName(), self.getHeader(), self.getProfessionID(), self.getLevel(), 2);
         }

         return 1;
      }
   }

   private static int applyInTeam(Player player, int teamId) {
      if (!player.getMap().canInTeam()) {
         SystemMessage.writeMessage(player, 19027);
         return 0;
      } else if (player.getCurrentTeam() != null) {
         SystemMessage.writeMessage(player, 19001);
         return 0;
      } else {
         Team team = getTeam(teamId);
         if (team != null) {
            Player leader = team.getLeader();
            if (leader == null) {
               return 0;
            }

            if (leader.getSystemSetup().isAutoInTeamWhenBeApply()) {
               if (team.isFull()) {
                  SystemMessage.writeMessage(player, 19004);
                  return 0;
               }

               team.inTeam(player, false);
            } else {
               SystemMessage.writeMessage(player, 19029);
               TeamApplyInviteList.addList(leader, player.getID(), player.getName(), player.getHeader(), player.getProfessionID(), player.getLevel(), 1);
            }
         }

         return 1;
      }
   }

   private static int leaveTeam(Player player) {
      Team team = player.getCurrentTeam();
      if (team == null) {
         player.setCurrentTeam((Team)null);
         TeamCreate.leaveTeam(player);
      } else {
         team.playerOut(player);
      }

      return 1;
   }

   private static int agreeInvite(Player player, long invitorId) {
      Player invitor = CenterManager.getPlayerByRoleID(invitorId);
      if (invitor == null) {
         SystemMessage.writeMessage(player, 19009);
         return 1;
      } else {
         Team team = invitor.getCurrentTeam();
         if (team == null) {
            SystemMessage.writeMessage(player, 19009);
            return 1;
         } else {
            Team selfTeam = player.getCurrentTeam();
            if (selfTeam != null) {
               ShowPopup.open(player, new TeamInvitePopup(player.createPopupID(), team, invitor));
            } else {
               team.agreeInvite(player);
            }

            return 1;
         }
      }
   }

   private static int agreeApply(Player player, long rid) {
      Team team = player.getCurrentTeam();
      if (team == null) {
         return 0;
      } else if (team.isFull()) {
         SystemMessage.writeMessage(player, 19004);
         return 0;
      } else if (player.getID() != team.getLeader().getID()) {
         SystemMessage.writeMessage(player, 19003);
         return 0;
      } else {
         Player other = CenterManager.getPlayerByRoleID(rid);
         if (other == null) {
            SystemMessage.writeMessage(player, 1021);
            return 0;
         } else if (other.getCurrentTeam() != null) {
            SystemMessage.writeMessage(player, MessageText.getText(19005).replace("%n%", other.getName()), 19005);
            return 0;
         } else {
            team.inTeam(other, false);
            return 1;
         }
      }
   }

   public static boolean hasTeam(int teamId) {
      return teamMap.containsKey(teamId);
   }

   public static void playerOnline(Player player) {
      Teammate mate = (Teammate)offlineMate.remove(player.getID());
      if (mate != null) {
         Team team = getTeam(mate.getTeamId());
         if (team != null) {
            TeamCreate.pushCreateTeam(team);
            player.setCurrentTeam(team);
         }
      }

   }
}
