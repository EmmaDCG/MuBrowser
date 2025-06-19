package com.mu.io.game2gateway.server;

import com.mu.game.CenterManager;
import com.mu.game.RemoteChannelInfo;
import com.mu.io.game.packet.imp.account.ConnectionSuccess;
import com.mu.io.game.packet.imp.player.PrepareMoveToRemoteServer;
import com.mu.io.game2gateway.packet.imp.sys.GS_AskIp;
import com.mu.utils.Tools;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

public class GatewayConnectionFutureListener implements ChannelFutureListener {
   private Channel c2gChannel;
   private int counter = 1;
   private String host;
   private int port;
   private int initType = 1;

   public GatewayConnectionFutureListener(Channel channel, int counter, String host, int port, int initType) {
      this.c2gChannel = channel;
      this.counter = counter;
      this.host = host;
      this.port = port;
      this.initType = initType;
   }

   public void operationComplete(ChannelFuture future) throws Exception {
      try {
         if (this.c2gChannel != null) {
            future.removeListener(this);
            Channel g2sChannel = future.getChannel();
            if (future.isSuccess()) {
               switch(this.initType) {
               case 1:
                  this.doConnectServer(g2sChannel);
                  return;
               case 2:
               case 3:
                  this.doSwitchServer(g2sChannel);
               }
            } else {
               if (this.counter >= 3) {
                  this.c2gChannel.close();
               } else {
                  GatewaySocketClient.connect(this.c2gChannel, this.counter + 1, this.host, this.port, this.initType);
               }

               return;
            }

            return;
         }
      } catch (Exception var6) {
         var6.printStackTrace();
         return;
      } finally {
         this.destroy();
      }

   }

   private void doSwitchServer(Channel g2sChannel) {
      Channel oldG2sChannel = CenterManager.getG2sChannel(this.c2gChannel);
      if (oldG2sChannel != null) {
         RemoteChannelInfo info = new RemoteChannelInfo(this.c2gChannel, g2sChannel);
         CenterManager.addRemoteChannelInfo(info);
         PrepareMoveToRemoteServer ps = PrepareMoveToRemoteServer.prepareMoveToRemoteServer(this.initType);
         oldG2sChannel.write(ps.toBuffer());
         ps.destroy();
         ps = null;
      }

   }

   private void doConnectServer(Channel g2sChannel) {
      CenterManager.addG2sChannelByC2gChannel(this.c2gChannel, g2sChannel);
      CenterManager.addC2gChannelByG2sChannel(g2sChannel, this.c2gChannel);
      ConnectionSuccess.writeConnectionSuccess(this.c2gChannel);
      GS_AskIp.tellIp(g2sChannel, Tools.getIP(this.c2gChannel));
   }

   private void destroy() {
      this.host = null;
      this.c2gChannel = null;
   }
}
