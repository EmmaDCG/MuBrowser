package com.mu.game.model.map;

public class AstarNode {
   public final boolean isblock;
   public final int x;
   public final int y;
   public double g;
   public double h;
   public double f;
   public AstarNode parent;
   public int openIndex = -1;

   public AstarNode(int x, int y, boolean isblock) {
      this.x = x;
      this.y = y;
      this.isblock = isblock;
   }

   public void updateF() {
      this.f = this.g + this.h;
   }
}
