package com.mu.io.game.packet.imp.account;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.Profession;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.utils.RndNames;

public class GetRandomName extends ReadAndWritePacket {
   public GetRandomName(int code, byte[] readBuf) {
      super(code, readBuf);
      this.processImmediately = true;
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int proType = this.readByte();
      int sex = Profession.getProfession(Profession.getProID(proType, 0)).getGender();
      String name = RndNames.getName(player.getUserName(), sex);
      if (name.equals("")) {
         this.writeBoolean(false);
      } else {
         this.writeBoolean(true);
         this.writeUTF(name);
      }

      player.writePacket(this);
   }
}
