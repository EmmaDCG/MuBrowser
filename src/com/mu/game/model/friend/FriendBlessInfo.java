package com.mu.game.model.friend;

public class FriendBlessInfo implements Comparable {
   private long time;
   private long id;
   private String name;

   public long getTime() {
      return this.time;
   }

   public void setTime(long time) {
      this.time = time;
   }

   public long getId() {
      return this.id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int compareTo(FriendBlessInfo o) {
      return this.time < o.getTime() ? 1 : -1;
   }

   @Override
   public int compareTo(Object o) {
      return 0;
   }
}
