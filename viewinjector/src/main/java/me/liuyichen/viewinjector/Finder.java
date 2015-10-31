package me.liuyichen.viewinjector;

import android.app.Activity;
import android.view.View;

/**
 * Created by liuchen on 15/10/30.
 * and ...
 */
public enum Finder {


    VIEW {
        @Override public View findById(Object source, int id) {
            return ((View) source).findViewById(id);
        }
    },
    ACTIVITY {
        @Override public View findById(Object source, int id) {
            return ((Activity) source).findViewById(id);
        }
    };
    
    public abstract View findById(Object source, int id);
}
