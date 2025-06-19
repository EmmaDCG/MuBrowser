package com.mu.io.client2gateway.server;

import com.mu.config.Global;
import com.mu.game.CenterManager;
import com.mu.io.game.packet.imp.account.DisconnectByClient;
import com.mu.io.game2gateway.server.GatewaySocketClient;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.DefaultSocketChannelConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientUpwardHandler extends SimpleChannelUpstreamHandler {
   private static final Logger logger = LoggerFactory.getLogger(ClientUpwardHandler.class);

   public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
      Channel channel = CenterManager.getG2sChannel(e.getChannel());
      if (channel != null) {
         channel.write(e.getMessage());
      }
   }

   public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
      Channel channel = e.getChannel();
      DefaultSocketChannelConfig config = (DefaultSocketChannelConfig)channel.getConfig();
      config.setTcpNoDelay(true);
      config.setKeepAlive(true);
      config.setConnectTimeoutMillis(120000);
      GatewaySocketClient.connect(ctx.getChannel(), 1, "127.0.0.1", Global.getGamePort(), 1);
   }

   public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
      if (logger.isInfoEnabled()) {
         logger.info("client to gateway channel be colosed");
      }

      Channel c2gChannel = ctx.getChannel();
      Channel g2sChannel = CenterManager.removeG2sChannel(c2gChannel);
      if (g2sChannel != null) {
         CenterManager.removeC2gChannel(g2sChannel);
         if (g2sChannel.isWritable()) {
            DisconnectByClient dc = new DisconnectByClient();
            g2sChannel.write(dc.toBuffer());
            dc.destroy();
         }
      }

   }

   public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
      if (logger.isDebugEnabled()) {
         logger.debug(e.getCause().getMessage());
      }

   }
}
