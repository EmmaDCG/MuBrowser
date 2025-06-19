package com.mu.io.game.packet.imp.friend;

import com.mu.game.CenterManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class AddFriend extends ReadAndWritePacket {
   public AddFriend(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public AddFriend() {
      super(11204, (byte[])null);
   }

   public void process() throws Exception {
      long fid = (long)this.readDouble();
      Player other = CenterManager.getPlayerByRoleID(fid);
      Player player = this.getPlayer();
      if (other == null) {
         SystemMessage.writeMessage(player, 1021);
      } else {
         player.getFriendManager().addFriend(other);
      }
   }

   public static void addSuccess(Player player, long fid, int type) {
      AddFriend af = new AddFriend();

      try {
         af.writeDouble((double)fid);
         af.writeByte(type);
         player.writePacket(af);
         af.destroy();
         af = null;
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }
}
