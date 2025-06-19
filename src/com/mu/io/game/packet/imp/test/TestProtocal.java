package com.mu.io.game.packet.imp.test;

import com.mu.io.game.packet.ReadAndWritePacket;
import java.net.InetAddress;

public class TestProtocal extends ReadAndWritePacket {
   public TestProtocal(int code, byte[] readData) {
      super(code, readData);
      this.processImmediately = true;
   }

   public void process() throws Exception {
      String txt = this.readUTF();
      String ip = InetAddress.getLocalHost().getHostAddress().toString();
      this.writeUTF("从" + ip + "返回:" + txt);
      this.getChannel().write(this.toBuffer());
   }
}
