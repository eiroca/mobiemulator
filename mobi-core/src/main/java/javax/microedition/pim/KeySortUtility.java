package javax.microedition.pim;

// ~--- JDK imports ------------------------------------------------------------

import java.util.Vector;

class KeySortUtility {

  KeySortUtility() {
  }

  static void store(final Vector keyList, final Vector valueList, final long key, final Object value) {
    int lowerBound = 0;
    for (int upperBound = keyList.size(); lowerBound != upperBound;) {
      final int index = lowerBound + ((upperBound - lowerBound) / 2);
      final long indexKey = ((Long)keyList.elementAt(index)).longValue();
      if (indexKey > key) {
        if (index == upperBound) {
          upperBound--;
        }
        else {
          upperBound = index;
        }
      }
      else if (index == lowerBound) {
        lowerBound++;
      }
      else {
        lowerBound = index;
      }
    }
    keyList.insertElementAt(new Long(key), lowerBound);
    valueList.insertElementAt(value, lowerBound);
  }
}
