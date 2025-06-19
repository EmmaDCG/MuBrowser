package com.mu.game.model.map.event;

import com.mu.game.model.map.Map;
import java.awt.geom.Ellipse2D;
import org.jdom.Element;

public class AreaCheckEvent extends MapEvent {
   private Ellipse2D ellipse = null;

   public AreaCheckEvent(Map map, Element element) {
      super(map);
      this.initEvent(element);
   }

   public void check(long now) {
      if (now - this.lastCheckTime >= this.checkRate) {
         this.lastCheckTime = now;
      }
   }

   public int getEventType() {
      return MapEvent.Evevt_AreaCheck;
   }

   public void initEvent(Element element) {
   }
}
