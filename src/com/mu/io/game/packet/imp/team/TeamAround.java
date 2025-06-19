package com.mu.io.game.packet.imp.team;

import com.mu.game.model.map.Map;
import com.mu.game.model.team.Team;
import com.mu.game.model.team.Teammate;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.HashMap;
import java.util.Iterator;

public class TeamAround extends ReadAndWritePacket {
   public TeamAround(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      Map map = player.getMap();
      HashMap teamMap = new HashMap();
      Iterator it = map.getPlayerMap().values().iterator();

      while(it.hasNext()) {
         Player p = (Player)it.next();
         Team team = p.getCurrentTeam();
         if (team != null && p.getID() != player.getID()) {
            teamMap.put(team.getId(), team);
         }
      }

      this.writeByte(teamMap.size());
      it = teamMap.values().iterator();

      while(it.hasNext()) {
         Team team = (Team)it.next();
         this.writeInt(team.getId());
         Player leader = team.getLeader();
         this.writeDouble((double)leader.getID());
         this.writeUTF(leader.getName());
         this.writeShort(leader.getLevel());
         Teammate highestMate = team.getHighestLevelMate();
         this.writeShort(highestMate.getLevel());
         int averageLevel = team.getAverageLevel();
         this.writeShort(averageLevel);
         this.writeByte(team.getMateList().size());
      }

      player.writePacket(this);
      teamMap.clear();
      teamMap = null;
   }
}
