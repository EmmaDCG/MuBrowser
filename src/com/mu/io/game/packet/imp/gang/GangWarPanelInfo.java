package com.mu.io.game.packet.imp.gang;

import com.mu.game.dungeon.DungeonManager;
import com.mu.game.dungeon.imp.luolan.Luolan;
import com.mu.game.dungeon.imp.luolan.LuolanManager;
import com.mu.game.dungeon.imp.luolan.LuolanMap;
import com.mu.game.dungeon.imp.luolan.LuolanTemplate;
import com.mu.game.model.gang.Gang;
import com.mu.game.model.gang.GangManager;
import com.mu.game.model.gang.GangMember;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.Profession;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.utils.Time;

public class GangWarPanelInfo extends ReadAndWritePacket {
   public GangWarPanelInfo(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public GangWarPanelInfo() {
      super(10625, (byte[])null);
   }

   public void process() throws Exception {
      pushPanel(this.getPlayer());
   }

   public static void pushPanel(Player player) {
      Gang gang = GangManager.getGang(GangManager.getWarVictorGang());
      GangWarPanelInfo packet = new GangWarPanelInfo();
      if (gang == null) {
         writeNoneVictory(packet);
      } else {
         writeHasVictory(player, gang, packet);
      }

      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }

   private static void writeNoneVictory(WriteOnlyPacket packet) {
      try {
         packet.writeDouble(0.0D);
         packet.writeUTF("");
         packet.writeByte(0);
         LuolanManager manager = DungeonManager.getLuolanManager();
         Luolan luolan = manager.getLuoLan();
         boolean canEnter = false;
         if (luolan != null && !luolan.getLuolanMap().isEnd()) {
            canEnter = true;
         }

         packet.writeBoolean(canEnter);
         if (!canEnter) {
            packet.writeUTF(Time.getTimeStr(manager.getOpenDate().getTime()));
         } else {
            packet.writeUTF("");
         }

         packet.writeBoolean(false);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   private static void writeHasVictory(Player player, Gang gang, WriteOnlyPacket packet) {
      try {
         LuolanManager manager = DungeonManager.getLuolanManager();
         LuolanTemplate template = manager.getTemplate();
         packet.writeDouble((double)gang.getId());
         packet.writeUTF(gang.getName());
         GangMember[] members = gang.getWarPanelList();
         packet.writeByte(members.length);

         for(int i = 0; i < members.length; ++i) {
            GangMember member = members[i];
            String wpName = "";
            if (i == 0) {
               wpName = template.getWarPostName(1);
            } else if (i == 1) {
               wpName = template.getWarPostName(2);
            } else {
               wpName = template.getWarPostName(3);
            }

            packet.writeByte(i + 1);
            packet.writeUTF(wpName);
            if (member != null) {
               packet.writeBoolean(true);
               packet.writeDouble((double)member.getId());
               packet.writeUTF(member.getName());
               int profession = member.getProfession();
               int proType = Profession.getProfession(profession).getProType();
               int[] models = template.getPanelModel(proType, member.getWarPost());
               packet.writeShort(models[0]);
               packet.writeByte(models[1]);
            } else {
               packet.writeBoolean(false);
            }
         }

         Luolan luolan = manager.getLuoLan();
         boolean canEnter = false;
         if (luolan != null) {
            LuolanMap map = luolan.getLuolanMap();
            if (map != null && !map.isEnd()) {
               canEnter = true;
            }
         }

         packet.writeBoolean(canEnter);
         if (!canEnter) {
            packet.writeUTF(Time.getTimeStr(manager.getOpenDate().getTime()));
         } else {
            packet.writeUTF("");
         }

         if (player.getGangId() == gang.getId()) {
            packet.writeBoolean(true);
            if (GangManager.hasWarRerceive(player.getID())) {
               packet.writeByte(2);
            } else {
               packet.writeByte(1);
            }
         } else {
            packet.writeBoolean(false);
         }

         members = null;
      } catch (Exception var12) {
         var12.printStackTrace();
      }

   }
}
