package com.javarush.task.task36.task3602;

import com.sun.org.apache.xpath.internal.operations.Mod;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;

/* 
Найти класс по описанию
*/
/*Описание класса:
1. Реализует интерфейс List; +
2. Является приватным статическим классом внутри популярного утилитного класса; +
3. Доступ по индексу запрещен - кидается исключение IndexOutOfBoundsException.
Используя рефлекшн (метод getDeclaredClasses), верни подходящий тип в методе getExpectedClass.


Требования:
1. Метод getExpectedClass должен использовать метод getDeclaredClasses подходящего утилитного класса.
2. Метод getExpectedClass должен вернуть правильный тип.
3. Метод main должен вызывать метод getExpectedClass.
4. Метод main должен вывести полученный класс на экран.*/
public class Solution {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        System.out.println(getExpectedClass());
    }

    public static Class getExpectedClass() throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        Class[] classes = Collections.class.getDeclaredClasses();// получаем массив для работы с классами
        for (Class c : classes){// проверяем каждый класс

            if(Modifier.isPrivate(c.getModifiers()))// если у класса модификатор доступа приватный
                if(Modifier.isStatic(c.getModifiers()))// если у класса модификатор статик
                {
                    if(List.class.isAssignableFrom(c))// если класс реализует интерфейс лист
                    {
                        try{
                            Constructor constructor = c.getDeclaredConstructor();// получаем конструктор у класса
                            constructor.setAccessible(true);// устанавливаем разрешение на изменение конструктора
                            /*Метод newInstance() возвращает объет обобщенного типа Object,
                             поэтому в последней строке мы приводим возвращенный объект к тому типу, который нам нужен.
                             */
                            List list = (List) constructor.newInstance();// создаем объект лист для работы с конструктором
                            list.get(0);// получаем первый экземляр у листа, чтобы поймать исключение
                            /*Доступ по индексу запрещен - кидается исключение IndexOutOfBoundsException.*/
                        }catch (IndexOutOfBoundsException e){
                            // вернуть класс
                            return c;
                        } catch (NoSuchMethodException e) {
                            //e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            //e.printStackTrace();
                        }
                    }
                }
        }
        return null;
    }
}
