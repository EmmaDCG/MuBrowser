package com.mu.executor.imp.sys;

import com.mu.executor.Executable;
import com.mu.game.CenterManager;
import com.mu.game.RemoteChannelInfo;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game.packet.imp.sys.TransferPlayerBaseInfo;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class SwitchToRemoteServer extends Executable {
   public SwitchToRemoteServer(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      try {
         String userName = packet.readUTF();
         long rid = packet.readLong();
         boolean needAntiAddiction = packet.readBoolean();
         int serverID = packet.readShort();
         int initType = packet.readByte();
         RemoteChannelInfo info = CenterManager.removeRemoteChannelInfo(packet.getC2gChannel());
         if (info == null) {
            packet.getC2gChannel().close();
         } else {
            CenterManager.removeC2gChannel(packet.getG2sChannel());
            CenterManager.removeG2sChannel(packet.getC2gChannel());
            CenterManager.addC2gChannelByG2sChannel(info.getG2rChannel(), info.getC2gChannel());
            CenterManager.addG2sChannelByC2gChannel(info.getC2gChannel(), info.getG2rChannel());
            TransferPlayerBaseInfo.writeBaseInfo(info.getG2rChannel(), rid, userName, needAntiAddiction, serverID, initType);
            if (initType == 2) {
               CenterManager.addInterChannel(userName, serverID, info.getG2rChannel());
            }

            if (packet.getG2sChannel().isOpen()) {
               packet.getG2sChannel().close();
            }
         }
      } catch (Exception var9) {
         var9.printStackTrace();
      }

   }

   public void toPacket(ExecutePacket packet, Object... obj) {
      Player player = (Player)obj[0];
      int initType = ((Integer)obj[1]).intValue();

      try {
         packet.writeUTF(player.getUserName());
         packet.writeLong(player.getID());
         packet.writeBoolean(player.getUser().isNeedAntiAddiction());
         packet.writeShort(player.getUser().getServerID());
         packet.writeByte(initType);
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }
}
