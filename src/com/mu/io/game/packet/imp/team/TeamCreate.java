package com.mu.io.game.packet.imp.team;

import com.mu.game.model.team.Team;
import com.mu.game.model.team.Teammate;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class TeamCreate extends WriteOnlyPacket {
   public TeamCreate() {
      super(11007);
   }

   public static void pushCreateTeam(Team team) {
      try {
         CopyOnWriteArrayList mateList = team.getMateList();
         Iterator var3 = mateList.iterator();

         while(true) {
            Player player;
            do {
               if (!var3.hasNext()) {
                  return;
               }

               Teammate mate = (Teammate)var3.next();
               player = mate.getPlayer();
            } while(player == null);

            TeamCreate tc = new TeamCreate();
            tc.writeInt(team.getId());
            tc.writeByte(mateList.size());
            Iterator var7 = mateList.iterator();

            while(var7.hasNext()) {
               Teammate m = (Teammate)var7.next();
               tc.writeDouble((double)m.getId());
               tc.writeUTF(m.getName());
               tc.writeShort(m.getHeader());
               tc.writeBoolean(m.isLeader());
               tc.writeDouble((double)m.getTime());
               writeVariableInfo(tc, player, m);
            }

            player.writePacket(tc);
            tc.destroy();
            tc = null;
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }
   }

   public static void leaveTeam(Player player) {
      try {
         TeamCreate tc = new TeamCreate();
         tc.writeInt(0);
         tc.writeUTF("");
         tc.writeByte(0);
         player.writePacket(tc);
         tc.destroy();
         tc = null;
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public static void writeVariableInfo(WriteOnlyPacket packet, Player player, Teammate mate) throws Exception {
      int status = mate.getStatus(player);
      packet.writeByte(status);
      packet.writeShort(mate.getLevel());
      packet.writeByte(mate.getProfessionId());
      packet.writeByte(mate.getWarComment());
      Player p = mate.getPlayer();
      if (p == null) {
         packet.writeShort(0);
         packet.writeInt(0);
         packet.writeInt(0);
         packet.writeByte(0);
         packet.writeShort(0);
      } else {
         packet.writeShort(p.getMapID());
         packet.writeInt(p.getX());
         packet.writeInt(p.getY());
         packet.writeByte(p.getMap().getLine());
         packet.writeShort(player.getTeamMateExpBonus(p));
      }

   }
}
