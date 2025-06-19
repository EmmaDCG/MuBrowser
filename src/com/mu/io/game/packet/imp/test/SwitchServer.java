package com.mu.io.game.packet.imp.test;

import com.mu.io.game.packet.ReadAndWritePacket;

public class SwitchServer extends ReadAndWritePacket {
   public SwitchServer(int code, byte[] readBuf) {
      super(code, readBuf);
      this.processImmediately = true;
   }

   public void process() throws Exception {
      String host = this.readUTF();
      int port = this.readUnsignedShort();
      NotifySwitchServer nss = new NotifySwitchServer(host, port);
      this.getChannel().write(nss.toBuffer());
      nss.destroy();
   }
}
