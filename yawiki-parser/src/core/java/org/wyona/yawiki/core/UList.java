package org.wyona.yawiki.core;

import org.apache.log4j.Category;

import java.io.InputStream;
import java.util.Vector;

/**
 *
 */
public class UList extends Element {
    private Category log = Category.getInstance(UList.class);

    /**
     *
     */
    public Vector getChildren() {
        Vector children = new Vector();
        children.addElement(new UListItem());
        //children.addElement(new UList());
        return children;
    }

    /**
     *
     */
    boolean startsWith(char ch, int position) {
        if (position == 0) {
            if (ch == '*') return true;
            return false;
        } else {
            return true;
        }
    }

    /**
     *
     */
    boolean endsWith(char ch, int position) {
        if (ch == '\n') return true;
        return false;
    }

    /**
     *
     */
    String getName() {
        return "ul";
    }
}
