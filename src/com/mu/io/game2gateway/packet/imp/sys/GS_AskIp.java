package com.mu.io.game2gateway.packet.imp.sys;

import com.mu.game.CenterManager;
import com.mu.io.game.packet.ReadAndWritePacket;
import org.jboss.netty.channel.Channel;

public class GS_AskIp extends ReadAndWritePacket {
   public GS_AskIp() {
      super(116, (byte[])null);
      this.processImmediately = true;
   }

   public GS_AskIp(int code, byte[] readBuf) {
      super(code, readBuf);
      this.processImmediately = true;
   }

   public static void tellIp(Channel channel, String ip) {
      try {
         GS_AskIp ai = new GS_AskIp();
         ai.writeUTF(ip);
         channel.write(ai.toBuffer());
         ai.destroy();
         ai = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void process() throws Exception {
      CenterManager.addChannelIp(this.getChannel(), this.readUTF());
   }
}
