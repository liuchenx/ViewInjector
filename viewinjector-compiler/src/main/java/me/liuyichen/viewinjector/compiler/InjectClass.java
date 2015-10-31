package me.liuyichen.viewinjector.compiler;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by liuchen on 15/10/30.
 * and ...
 */
public class InjectClass {


    protected String targetType;
    protected String classPackage;
    protected String className;

    public Set<InjectField> fields = new LinkedHashSet<>();


    public static class InjectField {

        public int id;
        public String type;
        public String name;

    }
}
