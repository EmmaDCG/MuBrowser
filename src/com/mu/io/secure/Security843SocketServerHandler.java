package com.mu.io.secure;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.DefaultSocketChannelConfig;

public class Security843SocketServerHandler extends SimpleChannelUpstreamHandler {
   private static final String xml = "<cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"*\"/></cross-domain-policy>\u0000";

   public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
      super.handleUpstream(ctx, e);
   }

   public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
      e.getChannel().write("<cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"*\"/></cross-domain-policy>\u0000");
      e.getChannel().close();
   }

   public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
   }

   public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
      DefaultSocketChannelConfig config = (DefaultSocketChannelConfig)e.getChannel().getConfig();
      config.setTcpNoDelay(true);
   }

   public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
      e.getCause().printStackTrace();
   }
}
