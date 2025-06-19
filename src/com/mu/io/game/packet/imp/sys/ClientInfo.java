package com.mu.io.game.packet.imp.sys;

import com.mu.db.manager.GameDBManager;
import com.mu.game.CenterManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.utils.concurrent.ThreadCachedPoolManager;

public class ClientInfo extends ReadAndWritePacket {
   public ClientInfo(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      final long rid = player.getID();
      final String system = this.readUTF();
      final String flashVersion = this.readUTF();
      final String resolution = this.readUTF();
      final String browser = this.readUTF();
      final String ip = CenterManager.getIpByChannel(this.getChannel());
      ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
         public void run() {
            GameDBManager.saveClientInfo(rid, system, flashVersion, resolution, browser, ip);
         }
      });
   }
}
