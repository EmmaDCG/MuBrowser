package com.mu.game.dungeon.imp.luolan;

import com.mu.game.dungeon.DungeonMonster;
import com.mu.game.model.map.BigMonsterGroup;

public class LuolanMonster extends DungeonMonster {
   public LuolanMonster(BigMonsterGroup md, LuolanMap map) {
      super(md, map);
      this.setBossRank(2);
   }

   public LuolanMap getLuoLanMap() {
      return (LuolanMap)this.getMap();
   }
}
