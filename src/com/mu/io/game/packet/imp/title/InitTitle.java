package com.mu.io.game.packet.imp.title;

import com.mu.db.manager.TitleDBManager;
import com.mu.executor.Executor;
import com.mu.game.dungeon.DungeonManager;
import com.mu.game.model.gang.Gang;
import com.mu.game.model.gang.GangManager;
import com.mu.game.model.gang.GangMember;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.title.Title;
import com.mu.game.model.unit.player.title.TitleManager;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.ArrayList;
import java.util.Iterator;

public class InitTitle extends ReadAndWritePacket {
   public InitTitle(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public InitTitle() {
      super(15001, (byte[])null);
   }

   public static InitTitle createInitTitle(long rid) {
      InitTitle it = new InitTitle();
      ArrayList list = TitleDBManager.getTitleList(rid);

      try {
         it.writeByte(list.size());
         Iterator var5 = list.iterator();

         while(var5.hasNext()) {
            Title title = (Title)var5.next();
            it.writeByte(title.getId());
            it.writeLong(title.getExpiredTime());
            it.writeBoolean(title.isEquip());
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      return it;
   }

   private boolean checkGangWarTitle(Player player, int titleId) {
      if (!DungeonManager.getLuolanManager().getTemplate().isGangWarTitle(titleId)) {
         return false;
      } else {
         TitleManager manager = player.getTitleManager();
         Gang gang = player.getGang();
         if (gang != null && gang.isWinner()) {
            GangMember member = GangManager.getMember(player.getID());
            if (member == null) {
               manager.deleteFromDb(titleId);
               return true;
            } else {
               int titlePost = DungeonManager.getLuolanManager().getTemplate().getWarPostByTitle(titleId);
               if (titlePost != member.getWarPost()) {
                  manager.deleteFromDb(titleId);
                  return true;
               } else {
                  return false;
               }
            }
         } else {
            manager.deleteFromDb(titleId);
            return true;
         }
      }
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long now = System.currentTimeMillis();
      int size = this.readUnsignedByte();

      for(int i = 0; i < size; ++i) {
         int id = this.readUnsignedByte();
         long time = this.readLong();
         boolean isEuyip = this.readBoolean();
         Title title;
         if (time > 0L && time < now) {
            WriteOnlyPacket packet = Executor.DeleteTitle.toPacket(player.getID(), Integer.valueOf(id));
            player.writePacket(packet);
            packet.destroy();
            title = null;
         } else if (!this.checkGangWarTitle(player, id)) {
            title = new Title(id);
            title.setExpiredTime(time);
            title.setEquip(isEuyip);
            player.getTitleManager().addTitle(title);
            if (title.isEquip()) {
               player.getTitleManager().setEquipId(title.getId());
            }
         }
      }

      player.getTitleManager().checkVipTitleOnEterGame();
      player.getTitleManager().resetProperties();
   }
}
