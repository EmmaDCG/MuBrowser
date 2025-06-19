package com.mu.io.game.server;

import com.mu.config.Global;
import com.mu.game.CenterManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.DefaultSocketChannelConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameServerHandler extends SimpleChannelUpstreamHandler {
   private static final Logger logger = LoggerFactory.getLogger(GameServerHandler.class);

   public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
      if (!Global.serverIsDown()) {
         ReadAndWritePacket packet = (ReadAndWritePacket)e.getMessage();
         Channel channel = ctx.getChannel();
         Player player = CenterManager.getPlayerByGameChannel(channel);
         packet.setChannel(channel);
         packet.setPlayer(player);
         if (packet.isProcessImmediately()) {
            try {
               packet.process();
            } catch (Exception var10) {
               logger.debug("packet error,id is {},IP is {}", packet.getOpcode(), CenterManager.getIpByChannel(channel));
               var10.printStackTrace();
            } finally {
               packet.destroy();
            }
         } else {
            if (player == null) {
               logger.error("player is null, opcode = {},channel = {}", packet.getOpcode(), channel.toString() + "\t" + System.currentTimeMillis());
               channel.close();
               return;
            }

            player.waitToProcess(packet);
         }

      }
   }

   public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
      if (logger.isDebugEnabled()) {
         logger.debug(e.getCause().getMessage());
      }

   }

   public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
      Channel channel = e.getChannel();
      DefaultSocketChannelConfig config = (DefaultSocketChannelConfig)channel.getConfig();
      config.setTcpNoDelay(true);
      config.setKeepAlive(true);
      config.setConnectTimeoutMillis(120000);
      if (logger.isDebugEnabled()) {
         logger.debug("channel connected success...");
      }

   }

   public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
      if (logger.isInfoEnabled()) {
         logger.info("gateway to gameserver channel closed ...");
      }

      CenterManager.removeUploadRecord(e.getChannel());
      CenterManager.removeIp(e.getChannel());
   }
}
