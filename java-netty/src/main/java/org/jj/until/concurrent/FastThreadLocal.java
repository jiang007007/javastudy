package org.jj.until.concurrent;

import org.jj.until.internal.InternalThreadLocalMap;

import java.util.Set;

public class FastThreadLocal<V> {

    private final int index;

    public FastThreadLocal() {
        index = InternalThreadLocalMap.nextVariableIndex();
    }

    public int getIndex() {
        return index;
    }

    public static void removeAll() {
        InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.getIfSet();

        if (threadLocalMap == null) {
            return;
        }

      try {
          Object v = threadLocalMap.indexedVariable(InternalThreadLocalMap.VARIABLES_TO_REMOVE_INDEX);
          if (v != null && v != InternalThreadLocalMap.UNSET) {
              @SuppressWarnings("unchecked")
              Set<FastThreadLocal<?>> variableToRemove = (Set<FastThreadLocal<?>>) v;
              FastThreadLocal<?>[] variableToRemoveArray = variableToRemove.toArray(new FastThreadLocal[0]);
              for (FastThreadLocal<?> tlv : variableToRemoveArray) {
                  tlv.remove(threadLocalMap);
              }
          }
      }finally {
          InternalThreadLocalMap.remove();
      }

    }


    public final void remove(InternalThreadLocalMap threadLocalMap) {
        if (threadLocalMap == null) {
            return;
        }
        Object v = threadLocalMap.removeIndexedVariable(index);
        if (v != InternalThreadLocalMap.UNSET) {

        }
    }


    private static void removeFromVariablesToRemove(
            InternalThreadLocalMap threadLocalMap, FastThreadLocal<?> variable
    ) {
        Object v = threadLocalMap.indexedVariable(InternalThreadLocalMap.VARIABLES_TO_REMOVE_INDEX);
        if (v == InternalThreadLocalMap.UNSET || v == null){
            return;
        }
        @SuppressWarnings("unchecked")
        Set<FastThreadLocal<?>> variablesToRemove = (Set<FastThreadLocal<?>>) v;
        variablesToRemove.remove(variable);
    }

}
