package com.mu.io.game.packet;

public abstract class Packet {
   private int opcode = -1;

   public Packet(int opcode) {
      this.opcode = opcode;
   }

   public final int getOpcode() {
      return this.opcode;
   }

   public abstract void destroy();
}
