package com.mu.game.model.team;

import com.mu.config.MessageText;
import com.mu.game.CenterManager;
import com.mu.game.model.chat.ChatProcess;
import com.mu.game.model.chat.newlink.NewCharactorLink;
import com.mu.game.model.chat.newlink.NewChatLink;
import com.mu.game.model.friend.Friend;
import com.mu.game.model.friend.FriendManager;
import com.mu.game.model.map.Map;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.chat.ForwardMessage;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.io.game.packet.imp.team.TeamCreate;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Team {
   public static final int MAX_SIZE = 4;
   public static final int Pick_Normal = 1;
   public static final int Pick_Random = 2;
   private CopyOnWriteArrayList mateList = new CopyOnWriteArrayList();
   private int id;
   private Player teamLeader = null;
   private int pickModel = 1;
   private static Logger logger = LoggerFactory.getLogger(Team.class);

   public Team(Player leader, int id) {
      this.id = id;
      this.teamLeader = leader;
   }

   public int getId() {
      return this.id;
   }

   public Player getLeader() {
      return this.teamLeader;
   }

   public int getMateExpBouns(Player player) {
      Teammate mate = this.getTeamate(player.getID());
      if (mate == null) {
         return 0;
      } else {
         int bouns = 0;

         try {
            Map selfMap = player.getMap();
            Iterator var6 = this.mateList.iterator();

            while(var6.hasNext()) {
               Teammate tm = (Teammate)var6.next();
               Player otherPlayer = tm.getPlayer();
               if (tm.getId() != player.getID() && otherPlayer != null) {
                  Friend friend;
                  if (player.isInDungeon()) {
                     if (selfMap.equals(otherPlayer.getMap())) {
                        bouns += 5000;
                        if (player.getFriendManager().isFriend(otherPlayer.getID())) {
                           friend = player.getFriendManager().getFriend(otherPlayer.getID(), 0);
                           if (friend != null) {
                              bouns += FriendManager.getCurrentFriendly(friend).getExpBouns() * 1000;
                           }
                        }
                     }
                  } else if (selfMap.getID() == otherPlayer.getMap().getID()) {
                     bouns += 5000;
                     friend = player.getFriendManager().getFriend(otherPlayer.getID(), 0);
                     if (friend != null) {
                        bouns += FriendManager.getCurrentFriendly(friend).getExpBouns() * 1000;
                     }
                  }
               }
            }
         } catch (Exception var9) {
            var9.printStackTrace();
         }

         return bouns;
      }
   }

   public void kickOut(long rid) {
      Teammate mate = this.getTeamate(rid);
      if (mate != null) {
         this.outTeam(rid);
         this.writeKickOutMessage(mate);
      }

      TeamCreate.pushCreateTeam(this);
   }

   private void outTeam(long rid) {
      this.removeMate(rid);
      Player player = CenterManager.getPlayerByRoleID(rid);
      if (player != null) {
         player.setCurrentTeam((Team)null);
         TeamCreate.leaveTeam(player);
      }
   }

   private Player searchNextLeader(long oldLeaderID) {
      for(int i = 0; i < this.mateList.size(); ++i) {
         Player player = ((Teammate)this.mateList.get(i)).getPlayer();
         if (player != null && player.getID() != oldLeaderID) {
            return player;
         }
      }

      return null;
   }

   public int changeLeader(long id) {
      Teammate mate = this.getTeamate(id);
      if (mate == null) {
         return 19006;
      } else {
         Player newLeader = mate.getPlayer();
         if (newLeader == null) {
            return 1021;
         } else {
            this.teamLeader = newLeader;
            TeamCreate.pushCreateTeam(this);
            this.writeNewLeaderMessage(mate);
            return 1;
         }
      }
   }

   public Teammate getTeamate(long rid) {
      Iterator var4 = this.mateList.iterator();

      while(var4.hasNext()) {
         Teammate mate = (Teammate)var4.next();
         if (mate.getId() == rid) {
            return mate;
         }
      }

      return null;
   }

   public boolean playerOut(Player player) {
      Teammate mate = this.getTeamate(player.getID());
      if (this.mateList.size() <= 1) {
         this.writeOutDisbindMessage(mate);
         this.disband();
         return true;
      } else {
         this.outTeam(player.getID());
         if (this.isLeader(player.getID())) {
            Player newLeader = this.searchNextLeader(player.getID());
            if (newLeader == null) {
               this.writeOutDisbindMessage(mate);
               this.disband();
               return true;
            }

            this.teamLeader = newLeader;
            this.writeNewLeaderMessage(this.getTeamate(newLeader.getID()));
         }

         this.writeOutMessage(mate);
         TeamCreate.pushCreateTeam(this);
         return true;
      }
   }

   public void outTeamForOffline(long rid) {
      Teammate mate = this.getTeamate(rid);
      this.outTeam(rid);
      this.writeOutMessage(mate);
      TeamCreate.pushCreateTeam(this);
   }

   public void inTeam(Player player, boolean isCreate) {
      if (player != null) {
         Teammate mate = new Teammate(player, this.id);
         this.addMate(mate);
         player.setCurrentTeam(this);
         TeamCreate.pushCreateTeam(this);
         this.writeInteamMessage(player, isCreate);
      }

   }

   private void writeInteamMessage(Player player, boolean isCreate) {
      if (player != null) {
         if (isCreate) {
            ChatProcess.writeChannelNoLinkMessageByServer(3, player, MessageText.getText(19016));
         } else {
            ForwardMessage fm = createJoinTeamMessage(player);
            if (fm != null) {
               Iterator var5 = this.mateList.iterator();

               while(var5.hasNext()) {
                  Teammate mate = (Teammate)var5.next();
                  Player owner = mate.getPlayer();
                  if (owner != null) {
                     if (owner.getID() != player.getID()) {
                        owner.writePacket(fm);
                     } else {
                        ChatProcess.writeChannelNoLinkMessageByServer(3, player, MessageText.getText(19017));
                     }
                  }
               }

               fm.destroy();
               fm = null;
            }
         }

      }
   }

   private void writeKickOutMessage(Teammate tm) {
      if (tm != null) {
         try {
            if (tm.getPlayer() != null) {
               ChatProcess.writeChannelNoLinkMessageByServer(3, tm.getPlayer(), MessageText.getText(19023));
            }

            NewCharactorLink link = new NewCharactorLink(0, tm.getId(), tm.getName(), tm.getImgText(), false);
            ForwardMessage fm = ChatProcess.createNewChatMessage(3, MessageText.getText(19024).replace("%n%", link.getContent()), new NewChatLink[]{link}, false, (byte[])null);
            Iterator var5 = this.mateList.iterator();

            while(var5.hasNext()) {
               Teammate mate = (Teammate)var5.next();
               Player owner = mate.getPlayer();
               if (owner != null && owner.getID() != tm.getId()) {
                  owner.writePacket(fm);
               }
            }

            fm.destroy();
            fm = null;
            link.destroy();
         } catch (Exception var7) {
            var7.printStackTrace();
         }

      }
   }

   private void writeNewLeaderMessage(Teammate tm) {
      if (tm != null) {
         try {
            if (tm.getPlayer() != null) {
               ChatProcess.writeChannelNoLinkMessageByServer(3, tm.getPlayer(), MessageText.getText(19025));
            }

            NewCharactorLink link = new NewCharactorLink(0, tm.getId(), tm.getName(), tm.getImgText(), false);
            ForwardMessage fm = ChatProcess.createNewChatMessage(3, MessageText.getText(19026).replace("%n%", link.getContent()), new NewChatLink[]{link}, false, (byte[])null);
            Iterator var5 = this.mateList.iterator();

            while(var5.hasNext()) {
               Teammate mate = (Teammate)var5.next();
               Player owner = mate.getPlayer();
               if (owner != null && owner.getID() != tm.getId()) {
                  owner.writePacket(fm);
               }
            }

            fm.destroy();
            fm = null;
         } catch (Exception var7) {
            var7.printStackTrace();
         }

      }
   }

   private void writeOutDisbindMessage(Teammate tm) {
      if (tm != null) {
         try {
            if (tm.getPlayer() != null) {
               ChatProcess.writeChannelNoLinkMessageByServer(3, tm.getPlayer(), MessageText.getText(19020));
            }

            NewCharactorLink link = new NewCharactorLink(0, tm.getId(), tm.getName(), tm.getImgText(), false);
            String msg = MessageText.getText(19022).replace("%n%", link.getContent());
            ForwardMessage fm = ChatProcess.createNewChatMessage(3, msg, new NewChatLink[]{link}, false, (byte[])null);
            Iterator var6 = this.mateList.iterator();

            while(var6.hasNext()) {
               Teammate mate = (Teammate)var6.next();
               Player owner = mate.getPlayer();
               if (owner != null && owner.getID() != tm.getId()) {
                  owner.writePacket(fm);
               }
            }

            fm.destroy();
            fm = null;
            link.destroy();
            link = null;
         } catch (Exception var8) {
            var8.printStackTrace();
         }

      }
   }

   private void writeOutMessage(Teammate tm) {
      if (tm != null) {
         try {
            if (tm.getPlayer() != null) {
               ChatProcess.writeChannelNoLinkMessageByServer(3, tm.getPlayer(), MessageText.getText(19019));
            }

            NewCharactorLink link = new NewCharactorLink(0, tm.getId(), tm.getName(), tm.getImgText(), false);
            String msg = MessageText.getText(19021).replace("%n%", link.getContent());
            ForwardMessage fm = ChatProcess.createNewChatMessage(3, msg, new NewChatLink[]{link}, false, (byte[])null);
            Iterator var6 = this.mateList.iterator();

            while(var6.hasNext()) {
               Teammate mate = (Teammate)var6.next();
               Player owner = mate.getPlayer();
               if (owner != null && owner.getID() != tm.getId()) {
                  owner.writePacket(fm);
               }
            }

            fm.destroy();
            fm = null;
         } catch (Exception var8) {
            var8.printStackTrace();
         }

      }
   }

   public boolean isFull() {
      return this.mateList.size() >= 4;
   }

   private void addMate(Teammate mate) {
      this.mateList.add(mate);
   }

   public void agreeInvite(Player player) {
      Team tmpTeam = player.getCurrentTeam();
      if (tmpTeam != null) {
         SystemMessage.writeMessage(player, 19001);
      } else if (this.isFull()) {
         SystemMessage.writeMessage(player, 19004);
      } else if (!player.getMap().canInTeam()) {
         SystemMessage.writeMessage(player, 19027);
      } else {
         this.inTeam(player, false);
      }
   }

   public boolean disband() {
      Iterator var2 = this.mateList.iterator();

      while(var2.hasNext()) {
         Teammate mate = (Teammate)var2.next();
         TeamManager.removeOfflineMate(mate.getId());
         Player player = mate.getPlayer();
         if (player != null) {
            player.setCurrentTeam((Team)null);
            TeamCreate.leaveTeam(player);
         }
      }

      this.mateList.clear();
      this.destroy();
      return true;
   }

   private void removeMate(long roleID) {
      TeamManager.removeOfflineMate(roleID);
      int index = -1;

      for(int i = 0; i < this.mateList.size(); ++i) {
         Teammate mate = (Teammate)this.mateList.get(i);
         if (mate.getId() == roleID) {
            index = i;
            break;
         }
      }

      if (index != -1) {
         this.mateList.remove(index);
      }

   }

   public void destroy() {
      TeamManager.removeTeam(this.id);
      if (this.mateList != null) {
         this.mateList.clear();
         this.mateList = null;
      }

      this.teamLeader = null;
   }

   public int getAverageLevel() {
      int sum = 0;

      Teammate mate;
      for(Iterator var3 = this.mateList.iterator(); var3.hasNext(); sum += mate.getLevel()) {
         mate = (Teammate)var3.next();
      }

      return sum / this.mateList.size();
   }

   public int getPickModel() {
      return this.pickModel;
   }

   public void setPickModel(int pickModel) {
      this.pickModel = pickModel;
   }

   public CopyOnWriteArrayList getMateList() {
      return this.mateList;
   }

   public Teammate getHighestLevelMate() {
      int tmplevel = 0;
      Teammate mate = null;
      Iterator var4 = this.mateList.iterator();

      while(var4.hasNext()) {
         Teammate m = (Teammate)var4.next();
         if (m.getLevel() > tmplevel) {
            tmplevel = m.getLevel();
            mate = m;
         }
      }

      return mate;
   }

   public void playerOffline(long rid) {
      if (this.teamLeader.getID() == rid) {
         Player player = this.searchNextLeader(rid);
         if (player == null) {
            this.disband();
            return;
         }

         this.teamLeader = player;
         TeamCreate.pushCreateTeam(this);
         this.writeNewLeaderMessage(this.getTeamate(player.getID()));
      }

      Teammate mate = this.getTeamate(rid);
      if (mate != null) {
         mate.setOfflineTime(System.currentTimeMillis());
         TeamManager.addOfflineMate(mate);
      }

   }

   public boolean isLeader(long id) {
      return this.teamLeader != null && this.teamLeader.getID() == id;
   }

   public static ForwardMessage createOutTeamMessage(Player player, boolean disbind) {
      return null;
   }

   public static ForwardMessage createJoinTeamMessage(Player player) {
      NewCharactorLink link = new NewCharactorLink(0, player.getID(), player.getName(), player.getVipImgText(), false);
      String msg = MessageText.getText(19018).replace("%n%", link.getContent());
      ForwardMessage fm = ChatProcess.createNewChatMessage(3, msg, new NewChatLink[]{link}, true, (byte[])null);
      return fm;
   }
}
