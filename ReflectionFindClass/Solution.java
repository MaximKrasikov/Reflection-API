package com.javarush.task.task36.task3606;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/* 
Осваиваем ClassLoader и Reflection

Аргументом для класса Solution является абсолютный путь к пакету.
Имя пакета может содержать File.separator.
В этом пакете кроме скомпилированных классов (.class) могут находиться и другие файлы (например: .java).
Известно, что каждый класс имеет конструктор без параметров и реализует интерфейс HiddenClass.
Считай все классы с файловой системы, создай фабрику - реализуй метод getHiddenClassObjectByKey.
Примечание: в пакете может быть только один класс, простое имя которого начинается с String key без учета регистра.
*/
public class Solution {
    private List<Class> hiddenClasses = new ArrayList<>();// массив классов
    private String packageName;// имя пакета

    public Solution(String packageName) {
        this.packageName = packageName;
    }

    public static void main(String[] args) throws ClassNotFoundException {
        Solution solution = new Solution(Solution.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "com/javarush/task/task36/task3606/data/second");
        solution.scanFileSystem();
        System.out.println(solution.getHiddenClassObjectByKey("hiddenclassimplse"));
        System.out.println(solution.getHiddenClassObjectByKey("hiddenclassimplf"));
        System.out.println(solution.getHiddenClassObjectByKey("packa"));
    }
    /*должен добавлять в поле hiddenClasses найденные классы.*/
    public void scanFileSystem() throws ClassNotFoundException {
        File[] files = new File(packageName).listFiles();// получение массива файлов в пакете
        ClassLoaderFromPath classLoader= new ClassLoaderFromPath();
        for (File f: files){
            Class<?> clazz= classLoader.load(f.toPath());// загружаем информацию о классе
            if (clazz!= null){// если путь  указан
                hiddenClasses.add(clazz);
            }
        }
    }

    /*должен создавать объект класса согласно условию задачи.*/
    public HiddenClass getHiddenClassObjectByKey(String key) {
        for (Class<?> clazz : hiddenClasses) {
            if (clazz.getSimpleName().toLowerCase().startsWith(key.toLowerCase())) {
                try {
                    Constructor[] constructors = clazz.getDeclaredConstructors();
                    for (Constructor constructor : constructors) {
                        if (constructor.getParameterTypes().length == 0) {
                            constructor.setAccessible(true);
                            return (HiddenClass) constructor.newInstance(null);
                        }
                    }
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return null;
    }
    /*класс предназначен для чтения из файла содержимого, в любом случае возврашает null*/
    public static class ClassLoaderFromPath extends ClassLoader {
        public Class<?> load(Path path) {// загрузчик классов
                try {
                    if (path.getFileName().toString().lastIndexOf(".class") == -1)// поиск файла по окончанию
                        return null;

                    byte[] b = Files.readAllBytes(path);// поткок для чтения из файла
                    return defineClass(null,b,0,b.length);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return  null;
        }
    }
}

