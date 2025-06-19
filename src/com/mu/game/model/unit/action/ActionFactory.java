package com.mu.game.model.unit.action;

import com.mu.game.model.unit.action.imp.EnterPlotAcion;
import com.mu.game.model.unit.action.imp.EquipAction;
import com.mu.game.model.unit.action.imp.LearnSkillAction;
import com.mu.game.model.unit.action.imp.OpenPanelAction;
import com.mu.game.model.unit.action.imp.PointMenuAction;
import com.mu.game.model.unit.action.imp.PushArrowAction;
import com.mu.game.model.unit.action.imp.TaskTransferAction;
import com.mu.game.model.unit.action.imp.UpdateMenuAction;
import com.mu.game.model.unit.action.imp.XmlAction;
import org.jdom.Element;

public class ActionFactory {
   public static XmlAction createAction(int id, Element element) throws Exception {
      String type = element.getAttributeValue("type").trim();
      String value = element.getAttributeValue("value").trim();
      XmlAction action = null;
      switch(type.hashCode()) {
      case 101491:
         if (type.equals("fly")) {
            action = new TaskTransferAction(id);
         }
         break;
      case 3347807:
         if (type.equals("menu")) {
            action = new UpdateMenuAction(id);
         }
         break;
      case 3443937:
         if (type.equals("plot")) {
            action = new EnterPlotAcion(id);
         }
         break;
      case 93090825:
         if (type.equals("arrow")) {
            action = new PushArrowAction(id);
         }
         break;
      case 96757808:
         if (type.equals("equip")) {
            action = new EquipAction(id);
         }
         break;
      case 961336941:
         if (type.equals("learnSkill")) {
            action = new LearnSkillAction(id);
         }
         break;
      case 1531924954:
         if (type.equals("openPanel")) {
            action = new OpenPanelAction(id);
         }
         break;
      case 1564317455:
         if (type.equals("pointMenu")) {
            action = new PointMenuAction(id);
         }
      }

      if (action == null) {
         throw new Exception("action is null,type = " + type);
      } else {
         ((XmlAction)action).initAction(value);
         return (XmlAction)action;
      }
   }
}
