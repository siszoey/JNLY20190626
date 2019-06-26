
package com.lib.bandaid.utils;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * view树帮助类
 */
final public class ViewTreeUtil {
    private ViewTreeUtil() {
    }

    public static List<View> find(ViewGroup root, Object tag) {
        FinderByTag finderByTag = new FinderByTag(tag);
        LayoutTraverser.build(finderByTag).traverse(root);
        return finderByTag.getViews();
    }

    public static <T extends View> List<T> findT(ViewGroup root, Object tag) {
        FinderByTag finderByTag = new FinderByTag(tag);
        LayoutTraverser.build(finderByTag).traverse(root);
        List<View> views = finderByTag.getViews();
        List<T> res = new ArrayList<>();
        View view;
        for (int i = 0; i < views.size(); i++) {
            view = views.get(i);
            try {
                res.add((T) view);
            } catch (Exception e) {
                e.getMessage();
            }
        }
        return res;
    }


    public static <T extends View> List<T> find(ViewGroup root, Class<T> type) {
        FinderByType<T> finderByType = new FinderByType<T>(type);
        LayoutTraverser.build(finderByType).traverse(root);
        return finderByType.getViews();
    }

    private static class FinderByTag implements LayoutTraverser.Processor {
        private final Object searchTag;
        private final List<View> views = new ArrayList<>();

        private FinderByTag(Object searchTag) {
            this.searchTag = searchTag;
        }

        @Override
        public void process(View view) {
            Object viewTag = view.getTag();

            if (viewTag != null && viewTag.equals(searchTag)) {
                views.add(view);
            }
        }

        private List<View> getViews() {
            return views;
        }
    }

    private static class FinderByType<T extends View> implements LayoutTraverser.Processor {
        private final Class<T> type;
        private final List<T> views;

        private FinderByType(Class<T> type) {
            this.type = type;
            views = new ArrayList<T>();
        }

        @Override
        @SuppressWarnings("unchecked")
        public void process(View view) {
            if (type.isInstance(view)) {
                views.add((T) view);
            }
        }

        public List<T> getViews() {
            return views;
        }
    }

    //===================================================================================
    public static List<View> getAllChildViews(View view) {
        List<View> allChildren = new ArrayList<>();
        if (view instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup) view;
            for (int i = 0; i < vp.getChildCount(); i++) {
                View viewChild = vp.getChildAt(i);
                allChildren.add(viewChild);
                allChildren.addAll(getAllChildViews(viewChild));
            }
        }
        return allChildren;
    }


    public static boolean hasView(View parentView, View view) {
        View _view = null;
        boolean flag = false;
        if (parentView instanceof ViewGroup) {
            try {
                int id = view.getId();
                _view = parentView.findViewById(id);
                if (_view == null) {
                    flag = false;
                } else {
                    flag = true;
                }
            } catch (Exception e) {
                List<View> views = getAllChildViews(parentView);
                if (views != null && views.contains(view)) {
                    flag = true;
                } else {
                    flag = false;
                }
            }
        }
        return flag;
    }

}
