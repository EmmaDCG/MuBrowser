package com.mu.executor.imp.pet;

import com.mu.db.manager.PetDBManager;
import com.mu.executor.Executable;
import com.mu.game.model.pet.PetAttribute;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class ReplacePetAttributeExecutor extends Executable {
   public ReplacePetAttributeExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      long roleId = packet.readLong();
      int statId = packet.readShort();
      int level = packet.readInt();
      int value = packet.readInt();
      PetDBManager.replaceAttribute(roleId, statId, level, value);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      try {
         Player player = (Player)obj[0];
         PetAttribute attribute = (PetAttribute)obj[1];
         packet.writeLong(player.getID());
         packet.writeShort(attribute.getId());
         packet.writeInt(attribute.getLevel());
         packet.writeInt(attribute.getValue());
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
