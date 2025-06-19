package com.mu.executor.imp.pet;

import com.mu.db.manager.PetDBManager;
import com.mu.executor.Executable;
import com.mu.game.model.pet.PlayerPetManager;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class ReplacePetExecutor extends Executable {
   public ReplacePetExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      long roleId = packet.readLong();
      int rank = packet.readByte();
      int level = packet.readShort();
      long exp = packet.readLong();
      int luck = packet.readInt();
      boolean show = packet.readBoolean();
      long diedTime = packet.readLong();
      long rankTime = packet.readLong();
      PetDBManager.replacePet(roleId, rank, level, exp, luck, show, diedTime, rankTime);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      try {
         PlayerPetManager ppm = (PlayerPetManager)obj[0];
         packet.writeLong(ppm.getOwner().getID());
         packet.writeByte(ppm.getRank().getRank());
         packet.writeShort(ppm.getLevel());
         packet.writeLong(ppm.getExp());
         packet.writeInt(ppm.getLuck());
         packet.writeBoolean(ppm.isShow());
         packet.writeLong(ppm.getDiedTime());
         packet.writeLong(ppm.getRankTime());
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
