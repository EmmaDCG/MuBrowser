package com.mu.io.game.packet.imp.gang;

import com.mu.game.model.gang.Gang;
import com.mu.game.model.gang.GangLevelData;
import com.mu.game.model.gang.GangManager;
import com.mu.game.model.gang.GangMember;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.utils.Time;
import java.util.ArrayList;
import java.util.Iterator;

public class GangBaseInfo extends ReadAndWritePacket {
   public GangBaseInfo(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public GangBaseInfo() {
      super(10600, (byte[])null);
   }

   public void process() throws Exception {
      boolean showOffline = this.readBoolean();
      Player player = this.getPlayer();
      pushGangInfo(player.getGang(), player, showOffline, this);
      player.writePacket(this);
   }

   public static void writeMemberInfo(GangMember member, WriteOnlyPacket packet) throws Exception {
      packet.writeDouble((double)member.getId());
      packet.writeUTF(member.getName());
      int[] icons = member.getBlueIcons();
      packet.writeShort(icons[0]);
      packet.writeShort(icons[1]);
      packet.writeShort(member.getLevel());
      packet.writeByte(member.getPost());
      packet.writeByte(member.getProfession());
      packet.writeInt(member.getCurContribution());
      packet.writeInt(member.getHisContribution());
      boolean isOnline = member.isOnline();
      packet.writeBoolean(isOnline);
      if (!isOnline) {
         packet.writeDouble((double)member.getOfflineTime());
      }

   }

   public static void pushGangInfo(Gang gang, Player player, boolean showOffline, WriteOnlyPacket packet) {
      try {
         if (gang == null) {
            packet.writeBoolean(false);
         } else {
            packet.writeBoolean(true);
            packet.writeDouble((double)gang.getId());
            packet.writeUTF(gang.getName());
            packet.writeByte(gang.getLevel());
            GangLevelData gd = GangManager.getLevelData(gang.getLevel());
            packet.writeShort(gang.getFlagId());
            packet.writeDouble((double)gang.getContribution());
            packet.writeDouble((double)gang.getHisContribution());
            packet.writeDouble((double)gang.getMasterId());
            packet.writeUTF(gang.getMasterName());
            packet.writeUTF(gang.getDescription());
            packet.writeUTF(gang.getEditorName());
            packet.writeUTF(Time.getTimeStr(gang.getDesEditTime(), "MM-dd HH:mm"));
            packet.writeByte(gd.getMaxMember());
            packet.writeByte(gang.getMemberSize());
            ArrayList list = new ArrayList();
            Iterator it = gang.getMemberMap().values().iterator();

            while(it.hasNext()) {
               GangMember member = (GangMember)it.next();
               if (showOffline) {
                  list.add(member);
               } else if (member.isOnline()) {
                  list.add(member);
               }
            }

            packet.writeByte(list.size());
            Iterator var10 = list.iterator();

            while(var10.hasNext()) {
               GangMember member = (GangMember)var10.next();
               writeMemberInfo(member, packet);
            }

            list.clear();
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }

   public static void pushBaseInfo(Player player) {
      Gang gang = player.getGang();
      GangBaseInfo info = new GangBaseInfo();

      try {
         if (gang == null) {
            info.writeBoolean(false);
         } else {
            info.writeBoolean(true);
            info.writeDouble((double)gang.getId());
            info.writeUTF(gang.getName());
            info.writeByte(gang.getLevel());
            info.writeUTF(GangManager.getLevelData(gang.getLevel()).getName());
            info.writeShort(GangManager.getLevelData(gang.getLevel()).getFlag());
            info.writeShort(gang.getFlagId());
            info.writeDouble((double)gang.getMasterId());
            info.writeUTF(gang.getMasterName());
            info.writeByte(gang.getMember(player.getID()).getPost());
            info.writeByte(GangManager.getLevelData(gang.getLevel()).getMaxMember());
            info.writeBoolean(GangManager.hasReceiveWelfare(player.getID()));
         }

         player.writePacket(info);
         info.destroy();
         info = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void pushLostGang(Player player) {
      try {
         GangBaseInfo info = new GangBaseInfo();
         info.writeBoolean(false);
         player.writePacket(info);
         info.destroy();
         info = null;
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }
}
