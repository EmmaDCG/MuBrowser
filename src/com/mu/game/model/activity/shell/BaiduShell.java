package com.mu.game.model.activity.shell;

import com.mu.game.model.activity.ActivityShell;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.jdom.Element;

public class BaiduShell extends ActivityShell {
   private HashMap buffMap = new HashMap();

   public int getMenuId() {
      return 20;
   }

   public int getShellId() {
      return 5;
   }

   public void init(InputStream in) throws Exception {
   }

   private void initBuff(Element element) throws Exception {
      List list = element.getChildren("element");
      Iterator var4 = list.iterator();

      while(var4.hasNext()) {
         Element e = (Element)var4.next();
         int level = e.getAttribute("level").getIntValue();
         int buffId = e.getAttribute("buffId").getIntValue();
         int buffLevel = e.getAttribute("buffLevel").getIntValue();
         this.buffMap.put(level, new int[]{buffId, buffLevel});
      }

   }

   public int[] getBuff(int level) {
      int[] buff = (int[])this.buffMap.get(level);
      return buff == null ? (int[])this.buffMap.get(Integer.valueOf(0)) : buff;
   }
}
