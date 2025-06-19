package com.mu.io.game.packet.imp.buff;

import com.mu.executor.imp.buff.SaveBuffWhenOffLineExecutor;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.buff.Buff;
import com.mu.game.model.unit.buff.model.BuffModel;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AddBuff extends WriteOnlyPacket {
   public AddBuff(Creature creature, List buffs) {
      super(31003);

      try {
         this.writeByte(creature.getType());
         this.writeDouble((double)creature.getID());
         writeBuffs(buffs, this);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void writeBuffs(List buffs, WriteOnlyPacket packet) {
      try {
         if (buffs == null) {
            packet.writeByte(0);
         } else {
            long now = System.currentTimeMillis();
            packet.writeByte(buffs.size());
            Iterator var5 = buffs.iterator();

            while(var5.hasNext()) {
               Buff buff = (Buff)var5.next();
               setBuffDetail(buff, now, packet);
            }
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   private static void setBuffDetail(Buff buff, long now, WriteOnlyPacket packet) throws Exception {
      packet.writeInt(buff.getModelID());
      packet.writeByte(buff.getOverlap());
      int time = SaveBuffWhenOffLineExecutor.getRemainTimeForClient(buff, now);
      packet.writeInt(time);
      packet.writeBoolean(buff.isHasEffect());
      writeDyDatas(buff, packet);
   }

   public static void writeDyDatas(Buff buff, WriteOnlyPacket packet) {
      try {
         List dyDatas = buff.getDynamicData();
         packet.writeByte(dyDatas.size());
         Iterator var4 = dyDatas.iterator();

         while(var4.hasNext()) {
            String data = (String)var4.next();
            packet.writeUTF(data);
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public static void sendToClient(Creature owner, Buff buff) {
      List buffs = new ArrayList();
      buffs.add(buff);
      AddBuff ab = new AddBuff(owner, buffs);
      boolean showForOther = BuffModel.showForOther(owner, buff.getModelID());
      if (owner.getType() == 1) {
         Player player = (Player)owner;
         player.writePacket(ab);
         if (showForOther && player.isEnterMap()) {
            player.getMap().sendPacketToAroundPlayer(ab, player, false);
         }
      } else {
         owner.getMap().sendPacketToAroundPlayer(ab, owner.getActualPosition());
      }

      ab.destroy();
      ab = null;
      buffs.clear();
      buffs = null;
   }

   public static void sendToSelf(Creature owner, List buffs) {
      if (owner.getType() == 1) {
         AddBuff ab = new AddBuff(owner, buffs);
         ((Player)owner).writePacket(ab);
         ab.destroy();
         ab = null;
      }
   }
}
