package com.mu.io.client2gateway.server;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

public class ClientServerPipelineFactory implements ChannelPipelineFactory {
   public ChannelPipeline getPipeline() throws Exception {
      ChannelPipeline pipeline = Channels.pipeline();
      pipeline.addLast("handler", new ClientUpwardHandler());
      return pipeline;
   }
}
