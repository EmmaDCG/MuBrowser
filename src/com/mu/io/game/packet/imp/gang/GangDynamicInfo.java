package com.mu.io.game.packet.imp.gang;

import com.mu.game.model.chat.SimpleChatInfo;
import com.mu.game.model.chat.newlink.NewChatLink;
import com.mu.game.model.gang.Gang;
import com.mu.game.model.gang.GangMember;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class GangDynamicInfo extends ReadAndWritePacket {
   public GangDynamicInfo(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public GangDynamicInfo() {
      super(10623, (byte[])null);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int para = this.readByte();
      Gang gang = player.getGang();
      if (gang != null) {
         this.writeByte(para);
         CopyOnWriteArrayList list = gang.getDynamicMsgList();
         if (list.size() > 0) {
            this.writeByte(list.size());
            Iterator var6 = list.iterator();

            while(var6.hasNext()) {
               SimpleChatInfo info = (SimpleChatInfo)var6.next();
               writeSimpleInfo(this, info);
            }
         } else {
            this.writeByte(0);
         }

         player.writePacket(this);
      }
   }

   private static void writeSimpleInfo(WriteOnlyPacket packet, SimpleChatInfo info) throws Exception {
      packet.writeUTF(info.getTime());
      packet.writeUTF(info.getMsg());
      NewChatLink[] links = info.getLinks();
      if (links != null && links.length > 0) {
         packet.writeByte(links.length);
         NewChatLink[] var6 = links;
         int var5 = links.length;

         for(int var4 = 0; var4 < var5; ++var4) {
            NewChatLink link = var6[var4];
            link.writeDetail(packet);
         }
      } else {
         packet.writeByte(0);
      }

   }

   public static void pushOneMsg(Gang gang, SimpleChatInfo info) {
      if (gang != null) {
         try {
            GangDynamicInfo gi = new GangDynamicInfo();
            gi.writeByte(0);
            gi.writeByte(1);
            writeSimpleInfo(gi, info);
            Iterator it = gang.getMemberMap().values().iterator();

            while(it.hasNext()) {
               GangMember member = (GangMember)it.next();
               Player player = member.getPlayer();
               if (player != null) {
                  player.writePacket(gi);
               }
            }

            gi.destroy();
            gi = null;
         } catch (Exception var6) {
            var6.printStackTrace();
         }

      }
   }
}
