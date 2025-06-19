package com.mu.game.model.unit.ai.handler;

import com.mu.game.model.unit.ai.AI;
import com.mu.game.model.unit.ai.AIState;

public interface IStateHandler {
   String getName();

   AIState getState();

   void begin(AI var1, Object... var2);

   void detect(AI var1);

   void finish(AI var1);
}
