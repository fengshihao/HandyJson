package com.fengshihao.handyjson;

import com.google.auto.service.AutoService;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class HandyJsonProcessor extends AbstractProcessor {
  private Filer mFiler;
  private Messager mMessager;
  private Elements mElementUtils;

  @Override
  public synchronized void init(ProcessingEnvironment processingEnvironment) {
    super.init(processingEnvironment);
    mFiler = processingEnvironment.getFiler();
    mMessager = processingEnvironment.getMessager();
    mElementUtils = processingEnvironment.getElementUtils();
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    Set<String> annotations = new LinkedHashSet<>();
    annotations.add(HandyJson.class.getCanonicalName());
    return annotations;
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override
  public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
    Set<? extends Element> allModelClass = roundEnvironment.getElementsAnnotatedWith(HandyJson.class);
    for (Element element : allModelClass) {
      if (element.getKind() != ElementKind.CLASS) {
        log("dont use HandyJson on a non-class object " + element.getSimpleName());
        return false;
      }
      String dataCalssName = element.getSimpleName().toString();

      //1.获取包名
      PackageElement packageElement = mElementUtils.getPackageOf(element);

      String pkName = packageElement.getQualifiedName().toString();
      CodeGenerator code = new CodeGenerator(pkName, dataCalssName);

      log(String.format("package=%s class=%s", pkName, dataCalssName));

      List<? extends Element> enclosed = element.getEnclosedElements();
      for (Element m : enclosed) {
        String name = m.getSimpleName().toString();
        if (m.getKind() != ElementKind.FIELD) {
          continue;
        }
        VariableElement kel = (VariableElement) m;

        Set<Modifier> modifiers = kel.getModifiers();
        if (modifiers == null) {
          continue;
        }

        if (modifiers.contains(Modifier.STATIC)
            || modifiers.contains(Modifier.TRANSIENT)) {
          continue;
        }

        Exclude exclude = kel.getAnnotation(Exclude.class);
        if (exclude != null) {
          continue;
        }

        if (modifiers.contains(Modifier.PRIVATE)) {
          loge("this filed must not be private for serialization or deserialization");
          continue;
        }

        String jsonKey = null;
        JsonKey jk = kel.getAnnotation(JsonKey.class);
        if (jk != null) {
          log("name=%s, json key= %s", name, jk.value());
          jsonKey = jk.value();
        }

        log("name=%s, %s %s", name, m.getKind(), kel.getSimpleName());

        ClassField cf = new ClassField();
        cf.name = name;
        cf.key = jsonKey != null ? jsonKey : name;
        code.fields.add(cf);

//                List<String> plist = new LinkedList<>();
//                ExecutableElement em = (ExecutableElement) m;
//                List<? extends VariableElement> parameters = em.getParameters();
//                for (VariableElement p: parameters) {
//                    //log("p=" + p.getSimpleName() + " " + p.asType());
//                    plist.add(p.asType().toString());
//                    plist.add(p.getSimpleName().toString());
//                }
        //model.methods.put(methodName, plist);
      }
      try {
        code.write(mFiler);
      } catch (IOException e) {
        loge(e.toString());
      }
    }
    return true;
  }

  private void loge(String msg) {
    mMessager.printMessage(Diagnostic.Kind.ERROR, msg);
  }


  private void log(String msg) {
    mMessager.printMessage(Diagnostic.Kind.OTHER, msg);
  }

  private void log(String format, Object... args) {
    mMessager.printMessage(Diagnostic.Kind.OTHER, String.format(format, args));
  }
}
