package com.mu.io.game2gateway.server;

import com.mu.game.CenterManager;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.DefaultSocketChannelConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GatewayDownwardHandler extends SimpleChannelUpstreamHandler {
   private static final Logger logger = LoggerFactory.getLogger(GatewayDownwardHandler.class);

   public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
      Game2GatewayPacket packet = (Game2GatewayPacket)e.getMessage();
      Channel g2sChannel = ctx.getChannel();
      Channel c2gChannel = CenterManager.getC2gChannel(g2sChannel);

      try {
         packet.setC2gChannel(c2gChannel);
         packet.setG2sChannel(g2sChannel);
         packet.process();
      } catch (Exception var10) {
         var10.printStackTrace();
      } finally {
         packet.destroy();
      }

   }

   public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
      Channel channel = e.getChannel();
      DefaultSocketChannelConfig config = (DefaultSocketChannelConfig)channel.getConfig();
      config.setTcpNoDelay(true);
      config.setKeepAlive(true);
      config.setConnectTimeoutMillis(120000);
      if (logger.isDebugEnabled()) {
         logger.debug("connect to gameserver successful...");
      }

   }

   public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
      if (logger.isInfoEnabled()) {
         logger.info("gameserver to gateway channel closed...");
      }

      Channel c2gChannel = CenterManager.removeC2gChannel(e.getChannel());
      if (c2gChannel != null) {
         CenterManager.removeG2sChannel(c2gChannel);
         if (c2gChannel.isConnected()) {
            c2gChannel.close();
         }
      }

   }

   public void exceptionCaught(ChannelHandlerContext arg0, ExceptionEvent e) throws Exception {
      e.getCause().printStackTrace();
   }
}
