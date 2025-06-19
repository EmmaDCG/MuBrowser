package com.mu.io.game.packet.imp.team;

import com.mu.game.model.map.Map;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.ArrayList;
import java.util.Iterator;

public class TeamAroundPlayers extends ReadAndWritePacket {
   public TeamAroundPlayers(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      String key = this.readUTF().trim();
      if (key.equals("")) {
         key = null;
      }

      Map map = player.getMap();
      ArrayList playerList = new ArrayList();
      int count = 0;
      Iterator it = map.getPlayerMap().values().iterator();

      while(it.hasNext()) {
         Player p = (Player)it.next();
         if (p.getID() != player.getID()) {
            if (key == null) {
               playerList.add(p);
               ++count;
            } else if (p.getName().indexOf(key) != -1) {
               playerList.add(p);
               ++count;
            }

            if (count >= 50) {
               break;
            }
         }
      }

      this.writeByte(playerList.size());
      Iterator var9 = playerList.iterator();

      while(var9.hasNext()) {
         Player p = (Player)var9.next();
         this.writeDouble((double)p.getID());
         this.writeUTF(p.getName());
         this.writeByte(p.getProfessionID());
         this.writeBoolean(p.getCurrentTeam() != null);
         this.writeShort(p.getLevel());
      }

      player.writePacket(this);
      playerList.clear();
      playerList = null;
   }
}
