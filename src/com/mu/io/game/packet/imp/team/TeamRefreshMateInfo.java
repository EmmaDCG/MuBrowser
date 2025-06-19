package com.mu.io.game.packet.imp.team;

import com.mu.game.model.team.Team;
import com.mu.game.model.team.Teammate;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class TeamRefreshMateInfo extends ReadAndWritePacket {
   public TeamRefreshMateInfo(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public TeamRefreshMateInfo() {
      super(11008, (byte[])null);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      Team team = player.getCurrentTeam();
      if (team != null) {
         CopyOnWriteArrayList mateList = team.getMateList();
         this.writeByte(mateList.size());
         Iterator var5 = mateList.iterator();

         while(var5.hasNext()) {
            Teammate m = (Teammate)var5.next();
            this.writeDouble((double)m.getId());
            TeamCreate.writeVariableInfo(this, player, m);
         }

         player.writePacket(this);
      }
   }

   public static void refresh(Team team) {
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

         try {
            TeamRefreshMateInfo tc = new TeamRefreshMateInfo();
            tc.writeByte(mateList.size());
            Iterator var7 = mateList.iterator();

            while(var7.hasNext()) {
               Teammate m = (Teammate)var7.next();
               tc.writeDouble((double)m.getId());
               TeamCreate.writeVariableInfo(tc, player, m);
            }

            player.writePacket(tc);
            tc.destroy();
            tc = null;
            PushTeamExpBouns.pushBouns(player);
         } catch (Exception var8) {
            var8.printStackTrace();
         }
      }
   }
}
