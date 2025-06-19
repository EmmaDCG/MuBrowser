package com.mu.io.game.packet.imp.player;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.User;
import com.mu.io.game.packet.ReadAndWritePacket;
import org.jboss.netty.channel.Channel;

public class UserSystemSetup extends ReadAndWritePacket {
   public UserSystemSetup(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public UserSystemSetup() {
      super(10020, (byte[])null);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      User user = player.getUser();
      user.setShadow(this.readByte());
      user.setAntiAliasing(this.readBoolean());
      user.setPerspective(this.readByte());
      user.setMute(this.readBoolean());
      user.setMusic(this.readByte());
      user.setSound(this.readByte());
      pushSetup(user, player.getChannel());
   }

   public static void pushSetup(User user, Channel channel) {
      try {
         UserSystemSetup us = new UserSystemSetup();
         us.writeByte(user.getShadow());
         us.writeBoolean(user.isAntiAliasing());
         us.writeByte(user.getPerspective());
         us.writeBoolean(user.isMute());
         us.writeByte(user.getMusic());
         us.writeByte(user.getSound());
         channel.write(us.toBuffer());
         us.destroy();
         us = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }
}
