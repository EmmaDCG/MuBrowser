package com.mu.io.game.server;

import com.mu.io.game.packet.PacketDecoder;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

public class GameServerPipelineFactory implements ChannelPipelineFactory {
   public ChannelPipeline getPipeline() throws Exception {
      ChannelPipeline pipeline = Channels.pipeline();
      pipeline.addLast("decoder", new PacketDecoder());
      pipeline.addLast("handler", new GameServerHandler());
      return pipeline;
   }
}
