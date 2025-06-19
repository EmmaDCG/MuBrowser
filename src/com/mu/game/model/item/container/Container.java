package com.mu.game.model.item.container;

import com.mu.game.model.item.Item;
import java.util.List;

public abstract class Container {
   public abstract int getVacantSize();

   public abstract boolean putToContainerBySlot(Item var1, int var2);

   public abstract int getType();

   public abstract boolean isFull();

   public abstract int reduceItemCount(Item var1, int var2);

   public abstract List getItemsByModelID(int var1, boolean var2);

   public abstract boolean hasItem(long var1);

   public abstract Item getItemBySlot(int var1);

   public abstract int getNextSlot();

   public abstract void moveAwayfromContainer(Item var1);

   public abstract void loadItem(Item var1);

   public abstract void destroy();
}
