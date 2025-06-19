package com.mu.io.game.packet.imp.player;

import com.mu.io.game.packet.ReadAndWritePacket;

public class PlayerZening extends ReadAndWritePacket {
   static final int[] items = new int[]{1101131, 1103161, 1104181, 1106151, 1203211, 1303031, 6005, 1001};
   static final String[] msgs = new String[]{"这是一个底部打印的测试,看看能打印多长的文字....", "看看超长的文字是个什么效果,是这样还是那样,到底是什么样,我其实也不知道,试试看再说....", "这次来个短点的字", "再次测试文字的长度的显示效果"};
   static final int[] ids = new int[]{1, 2, 3, 5, 7, 8};

   public PlayerZening(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
   }
}
