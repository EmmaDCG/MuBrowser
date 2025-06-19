package com.mu.io.game2gateway.server;

import com.mu.io.game2gateway.packet.Game2GatewayPacketDecoder;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

public class GatewayClientPipelineFactory implements ChannelPipelineFactory {
   public ChannelPipeline getPipeline() throws Exception {
      ChannelPipeline pipeline = Channels.pipeline();
      pipeline.addLast("decoder", new Game2GatewayPacketDecoder());
      pipeline.addLast("handler", new GatewayDownwardHandler());
      return pipeline;
   }
}
