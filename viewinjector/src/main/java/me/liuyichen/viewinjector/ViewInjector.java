package me.liuyichen.viewinjector;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Method;

/**
 * Created by liu on 2015/10/29.
 */
public class ViewInjector {


    public static void inject(Object target, Activity activity) {

        injectType(target, activity, Finder.ACTIVITY);
    }

    public static void inject(Object target, View  v) {

        injectType(target, v, Finder.VIEW);
    }

    private static void injectType(Object target, Object source, Finder finder) {
        Class<?> targetClass = target.getClass();
        try {
            Class<?> injector = Class.forName(targetClass.getName() + "$$ViewInjector");
            Method method = injector.getMethod("bind", targetClass, Object.class, Finder.class);
            System.out.println(method.invoke(null, target, source, finder));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
