package com.q25.inputhelper.util;

import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public final class AccessibilityNodes {
    private AccessibilityNodes() {
    }

    public static boolean hasExactlyOneNode(AccessibilityNodeInfo root, String viewId) {
        List<AccessibilityNodeInfo> nodes = root.findAccessibilityNodeInfosByViewId(viewId);
        try {
            return nodes.size() == 1;
        } finally {
            recycleAll(nodes);
        }
    }

    public static AccessibilityNodeInfo findSingleNode(AccessibilityNodeInfo root, String viewId) {
        List<AccessibilityNodeInfo> nodes = root.findAccessibilityNodeInfosByViewId(viewId);
        if (nodes.size() != 1) {
            recycleAll(nodes);
            return null;
        }

        AccessibilityNodeInfo result = nodes.get(0);
        for (int i = 1; i < nodes.size(); i++) {
            nodes.get(i).recycle();
        }

        return result;
    }

    public static boolean hasPackage(AccessibilityNodeInfo node, String packageName) {
        CharSequence nodePackage = node.getPackageName();
        return nodePackage != null && packageName.contentEquals(nodePackage);
    }

    public static boolean isDescendantOf(AccessibilityNodeInfo node, AccessibilityNodeInfo ancestor) {
        AccessibilityNodeInfo parent = node.getParent();
        while (parent != null) {
            boolean matches = parent.equals(ancestor);
            AccessibilityNodeInfo nextParent = matches ? null : parent.getParent();
            parent.recycle();
            if (matches) return true;
            parent = nextParent;
        }

        return false;
    }

    private static void recycleAll(List<AccessibilityNodeInfo> nodes) {
        for (AccessibilityNodeInfo node : nodes) {
            node.recycle();
        }
    }
}
