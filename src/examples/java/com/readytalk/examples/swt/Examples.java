package com.readytalk.examples.swt;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Constructor;
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
    Set<Constructor> constructors = reflections.getConstructorsAnnotatedWith(RunnableExample.class);

    for (Constructor constructor : constructors) {
      RunnableExample annotation = (RunnableExample) constructor.getAnnotation(RunnableExample.class);
      if (annotation.name().equals(exampleToRun)) {
        try {
          SwtBlingExample example = (SwtBlingExample) constructor.newInstance();
          example.run();
          return;
        } catch (InstantiationException e) {
          logger.severe("Caught InstantiationException");
          e.printStackTrace();
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
    for (Constructor constructor : constructors) {
      RunnableExample annotation = (RunnableExample) constructor.getAnnotation(RunnableExample.class);
      validChoices.append("    * " + annotation.name() + "\n");
    }
    logger.info(validChoices.toString());
  }
}
