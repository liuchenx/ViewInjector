package me.liuyichen.viewinjector.compiler;

import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import me.liuyichen.viewinjector.annotation.InjectView;

/**
 * Created by liuchen on 15/10/30.
 * and ...
 */
public class InjectorProcessor extends AbstractProcessor {

    private static final String INJECTOR_CLASS_SUFFIX = "$$ViewInjector";
    private static final String INJECTOR_FINDER = "me.liuyichen.viewinjector.Finder";


    private Messager messager;
    private Elements elementUtils;
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotataions = new LinkedHashSet<String>();
        annotataions.add(InjectView.class.getCanonicalName());
        return annotataions;
    }


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        Map<TypeElement, InjectClass> injectionsClass = new LinkedHashMap<>();

        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(InjectView.class)) {

            TypeElement enclosingElement = (TypeElement) annotatedElement.getEnclosingElement();
            InjectView inject = annotatedElement.getAnnotation(InjectView.class);
            int id = inject.value();
            if (injectionsClass.containsKey(enclosingElement)) {

                InjectClass injectClass = injectionsClass.get(enclosingElement);
                Set<InjectClass.InjectField> fields = injectClass.fields;
                InjectClass.InjectField field = new InjectClass.InjectField();
                field.id = id;
                field.name = annotatedElement.getSimpleName().toString();
                field.type = annotatedElement.asType().toString();

                fields.add(field);
            } else {
                String targetType = enclosingElement.getQualifiedName().toString();
                String classPackage = getPackageName(enclosingElement);
                String className = getClassName(enclosingElement, classPackage) + INJECTOR_CLASS_SUFFIX;

                InjectClass injectClass = new InjectClass();
                injectClass.targetType = targetType;
                injectClass.className = className;
                injectClass.classPackage = classPackage;

                Set<InjectClass.InjectField> fields = injectClass.fields;
                InjectClass.InjectField field = new InjectClass.InjectField();
                field.id = id;
                field.name = annotatedElement.getSimpleName().toString();
                field.type = annotatedElement.asType().toString();

                fields.add(field);
                injectionsClass.put(enclosingElement, injectClass);
            }
        }

        Iterator iterator = injectionsClass.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            InjectClass injectClass = (InjectClass)entry.getValue();

            generateCode(injectClass);
        }
        return true;
    }

    /*
    * package me.liuyichen.demo.sample
    *
    * public class MainActivity$$ViewInjector {
    *
    *     public static void inject(Object o, me.liuyichen.viewinjector.Finder finder) {
    *
    *         o.v = (XXX)finder.findById(id);
    *     }
    * }
    * */
    private void generateCode(InjectClass c) {

        StringBuilder builder = new StringBuilder("package " + c.classPackage + ";\n");
        builder.append("public class " + c.className +   " {\n");

        builder.append("public static void bind(" + c.targetType + " target, " + "Object source, " + INJECTOR_FINDER + " finder) {\n");

        Iterator<InjectClass.InjectField> iterator = c.fields.iterator();
        while (iterator.hasNext()) {
            InjectClass.InjectField field = iterator.next();
            builder.append("target." + field.name + "=(" + field.type + ")finder.findById(source, "  + field.id + ");\n");
        }
        builder.append("}\n");
        builder.append("}\n");

        try {
            JavaFileObject jfo = filer.createSourceFile(c.classPackage + "." + c.className);
            Writer writer = jfo.openWriter();
            writer.write(builder.toString());
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen).replace('.', '$');
    }

    private String getPackageName(TypeElement type) {
        return elementUtils.getPackageOf(type).getQualifiedName().toString();
    }

    private void error(Element element, String message, Object... args) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, String.format(message, args), element);
    }

    private void note(Element element, String message, Object... args) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, String.format(message, args), element);
    }
}
