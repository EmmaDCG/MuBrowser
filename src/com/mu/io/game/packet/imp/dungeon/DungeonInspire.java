package com.mu.io.game.packet.imp.dungeon;

import com.mu.config.MessageText;
import com.mu.game.dungeon.DungeonPlayerInfo;
import com.mu.game.dungeon.DungeonTemplate;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import java.util.ArrayList;
import java.util.Iterator;

public class DungeonInspire extends ReadAndWritePacket {
   public DungeonInspire(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public DungeonInspire() {
      super(12007, (byte[])null);
   }

   public static void pushInspireInfo(DungeonTemplate template, DungeonPlayerInfo info, Player player) {
      if (template != null && template.isCanInspire()) {
         try {
            int hLevel = info.getHurtInspireLevel();
            int lLevel = info.getLifeInspireLevel();
            ArrayList list = new ArrayList();
            if (lLevel > 0) {
               list.add(MessageText.getText(14023).replace("%s%", lLevel * 10 + "%"));
            }

            if (hLevel > 0) {
               list.add(MessageText.getText(14024).replace("%s%", hLevel * 10 + "%"));
            }

            DungeonInspire di = new DungeonInspire();
            di.writeByte(template.getTemplateID());
            di.writeByte(list.size());
            Iterator var8 = list.iterator();

            while(var8.hasNext()) {
               String s = (String)var8.next();
               di.writeUTF(s);
            }

            di.writeUTF(template.getMoneyInspireDes());
            di.writeUTF(template.getIngotInspireDes());
            player.writePacket(di);
            list.clear();
            list = null;
            di.destroy();
            di = null;
         } catch (Exception var9) {
            var9.printStackTrace();
         }

      }
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      if (player.isInDungeon()) {
         int type = this.readByte();
         int result = player.getDungeonMap().getDungeon().inspire(player, type);
         if (result != 1) {
            SystemMessage.writeMessage(player, result);
         } else {
            DungeonPlayerInfo info = player.getDungeonMap().getDungeon().getDungeonPlayerInfo(player.getID());
            pushInspireInfo(player.getDungeonMap().getDungeon().getTemplate(), info, player);
         }

      }
   }
}
