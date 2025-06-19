package com.mu.io.game.packet.imp.gang;

import com.mu.game.model.gang.Gang;
import com.mu.game.model.gang.GangMember;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.utils.Time;

public class PushGangAnnouncement extends WriteOnlyPacket {
   public PushGangAnnouncement() {
      super(10605);
   }

   public PushGangAnnouncement(String des, String name, long time) {
      super(10605);

      try {
         this.writeUTF(des);
         this.writeUTF(name);
         this.writeUTF(Time.getTimeStr(time, "MM-dd HH:mm"));
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public static void pushAnnouncement(Player player) {
      Gang gang = player.getGang();
      if (gang != null) {
         try {
            GangMember editor = gang.getDesEditor();
            String editorName = "";
            long editTime = gang.getCreateTime();
            if (editor != null) {
               editorName = editor.getName();
               editTime = gang.getDesEditTime();
            }

            PushGangAnnouncement pa = new PushGangAnnouncement(gang.getDescription(), editorName, editTime);
            player.writePacket(pa);
            pa.destroy();
            pa = null;
         } catch (Exception var7) {
            var7.printStackTrace();
         }

      }
   }
}
