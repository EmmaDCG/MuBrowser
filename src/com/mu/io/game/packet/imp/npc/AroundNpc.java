package com.mu.io.game.packet.imp.npc;

import com.mu.game.model.equip.external.ExternalEntry;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.stats.StatList2Client;
import com.mu.game.model.unit.npc.Npc;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.player.AroundPlayer;
import com.mu.io.game.packet.imp.player.PlayerAttributes;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AroundNpc extends WriteOnlyPacket {
   public AroundNpc(Npc npc) {
      super(10400);

      try {
         this.writeShort(1);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public AroundNpc(List npcList) {
      super(10400);

      try {
         if (npcList == null || npcList.isEmpty()) {
            this.writeShort(0);
            return;
         }

         this.writeShort(npcList.size());
         Iterator var3 = npcList.iterator();

         while(var3.hasNext()) {
            Npc npc = (Npc)var3.next();
            this.writeNpc(npc);
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   private void writeNpc(Npc npc) throws IOException {
      this.writeDouble((double)npc.getID());
      this.writeUTF(npc.getName());
      this.writeInt(npc.getX());
      this.writeInt(npc.getY());
      this.writeShort(npc.getTemplate().getModelId());
      this.writeByte(npc.getFace()[0]);
      this.writeByte(npc.getFace()[1]);
      ArrayList list = npc.getTemplate().getEquipEntry();
      this.writeByte(list.size());
      Iterator var4 = list.iterator();

      while(var4.hasNext()) {
         ExternalEntry entry = (ExternalEntry)var4.next();
         this.writeByte(entry.getType());
         this.writeShort(entry.getModelID());
         this.writeShort(entry.getEffectID());
      }

      List stats = StatList2Client.getMonsterStats();
      this.writeShort(stats.size());
      Iterator var5 = stats.iterator();

      while(var5.hasNext()) {
         StatEnum stat = (StatEnum)var5.next();
         int value = npc.getStatValue(stat);
         PlayerAttributes.writeStat(stat, (long)value, this);
      }

      AroundPlayer.writeBuffs(npc, false, this);
   }
}
