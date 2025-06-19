package com.mu.io.game.packet.imp.buff;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.buff.model.BuffModel;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DeleteBuff extends WriteOnlyPacket {
   public DeleteBuff(Creature owner, List buffIDs) {
      super(31001);

      try {
         this.writeByte(owner.getType());
         this.writeDouble((double)owner.getID());
         this.writeByte(buffIDs.size());
         Iterator var4 = buffIDs.iterator();

         while(var4.hasNext()) {
            Integer buffID = (Integer)var4.next();
            this.writeInt(buffID.intValue());
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public static void sendToClient(Creature owner, int buffID) {
      if (needToSend(owner)) {
         List buffIDs = new ArrayList();
         buffIDs.add(buffID);
         DeleteBuff db = new DeleteBuff(owner, buffIDs);
         boolean sendToOther = BuffModel.showForOther(owner, buffID);
         if (owner.getType() == 1) {
            Player player = (Player)owner;
            if (player.isEnterMap() && sendToOther) {
               owner.getMap().sendPacketToAroundPlayer(db, player, true);
            } else {
               player.writePacket(db);
            }
         } else {
            owner.getMap().sendPacketToAroundPlayer(db, owner.getActualPosition());
         }

         buffIDs.clear();
         buffIDs = null;
         db.destroy();
         db = null;
      }
   }

   public static boolean needToSend(Creature owner) {
      return owner.getType() != 2 || !owner.isDie();
   }

   public static void sendToClient(Creature owner, List buffIDs) {
      if (needToSend(owner)) {
         DeleteBuff db = new DeleteBuff(owner, buffIDs);
         if (owner.getType() == 1) {
            Player player = (Player)owner;
            if (player.isEnterMap()) {
               owner.getMap().sendPacketToAroundPlayer(db, player, true);
            } else {
               player.writePacket(db);
            }
         } else {
            owner.getMap().sendPacketToAroundPlayer(db, owner.getActualPosition());
         }

         db.destroy();
         db = null;
      }
   }
}
