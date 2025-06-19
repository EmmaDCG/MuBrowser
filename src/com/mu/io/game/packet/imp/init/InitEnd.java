package com.mu.io.game.packet.imp.init;

import com.mu.game.model.stats.statId.StatIdCreator;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.RoleInfo;
import com.mu.game.model.unit.talent.TalentModel;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.map.EnterMap;
import com.mu.io.game.packet.imp.map.TransferMap;

public class InitEnd extends ReadAndWritePacket {
   public InitEnd(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   private InitEnd() {
      super(10004, (byte[])null);
   }

   public static InitEnd initEnd(int initType) throws Exception {
      InitEnd end = new InitEnd();
      end.writeByte(initType);
      return end;
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      RoleInfo info = player.getRoleInfo();
      player.setHp(info.getCurrentHp());
      player.setMp(info.getCurrentMp());
      player.setSd(info.getCurrentSd());
      player.setAg(info.getCurrentAg());
      player.setAp(info.getCurrentAp());
      player.getProperty().addModifies(StatIdCreator.createTalentID(), TalentModel.getTalent(player.getProfessionID()).getStats());
      int hp = player.getHp();
      hp = Math.max(100, hp);
      hp = Math.min(player.getMaxHp(), hp);
      player.setHp(hp);
      player.setMp(player.getMaxMp());
      int type = this.readByte();
      switch(type) {
      case 1:
         EnterMap.playerEnterMap(player);
         break;
      case 2:
      case 3:
         TransferMap.playerSwitchMapDefault(player, player.getMapID(), player.getX(), player.getY());
      }

   }
}
