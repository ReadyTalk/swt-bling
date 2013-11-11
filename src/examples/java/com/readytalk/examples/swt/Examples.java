package com.readytalk.examples.swt;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.logging.Logger;

public class Examples {
  private static final Logger logger = Logger.getLogger("com.readytalk.examples.Example");

  public static void main(String[] args) {
    if (args.length <= 0) {
      logger.severe("You must pass a parameter to the Examples class");
      return;
    }

    String exampleToRun = args[0];

    Reflections reflections = new Reflections(new ConfigurationBuilder().
                                                  setUrls(ClasspathHelper.forPackage("com.readytalk.examples")).
                                                  setScanners(new MethodAnnotationsScanner()));
    Set<Method> methods = reflections.getMethodsAnnotatedWith(RunnableExample.class);

    for (Method method : methods) {
      RunnableExample annotation = method.getAnnotation(RunnableExample.class);
      if (annotation.name().equals(exampleToRun)) {
        try {
          method.invoke(null);
          return;
        } catch (IllegalAccessException e) {
          logger.severe("Caught IllegalAccessException");
          e.printStackTrace();
        } catch (InvocationTargetException e) {
          logger.severe("Caught InvocationTargetException");
          e.printStackTrace();
        }
      }
    }

    logger.severe("Coudn't find requested example " + exampleToRun);
    StringBuilder validChoices = new StringBuilder();
    validChoices.append("Valid choices are:\n");
    for (Method method : methods) {
      RunnableExample annotation = method.getAnnotation(RunnableExample.class);
      validChoices.append("    * " + annotation.name() + "\n");
    }
    logger.info(validChoices.toString());
  }
}
