package ru.mail.polis.homework.reflection;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Необходимо реализовать метод reflectiveToString, который для произвольного объекта
 * возвращает его строковое описание в формате:
 * <p>
 * {field_1: value_1, field_2: value_2, ..., field_n: value_n}
 * <p>
 * где field_i - имя поля
 * value_i - его строковое представление (String.valueOf),
 * за исключением массивов, для которых value формируется как:
 * [element_1, element_2, ..., element_m]
 * где element_i - строковое представление элемента (String.valueOf)
 * элементы должны идти в том же порядке, что и в массиве.
 * <p>
 * Все null'ы следует представлять строкой "null".
 * <p>
 * Порядок полей
 * Сначала следует перечислить в алфавитном порядке поля, объявленные непосредственно в классе объекта,
 * потом в алфавитном порядке поля объявленные в родительском классе и так далее по иерархии наследования.
 * Примеры можно посмотреть в тестах.
 * <p>
 * Какие поля выводить
 * Необходимо включать только нестатические поля. Также нужно пропускать поля, помеченные аннотацией @SkipField
 * <p>
 * Упрощения
 * Чтобы не усложнять задание, предполагаем, что нет циклических ссылок, inner классов, и transient полей
 * <p>
 * Реализация
 * В пакете ru.mail.polis.homework.reflection можно редактировать только этот файл
 * или добавлять новые (не рекомендуется, т.к. решение вполне умещается тут в несколько методов).
 * Редактировать остальные файлы нельзя.
 * <p>
 * Баллы
 * В задании 3 уровня сложности, для каждого свой набор тестов:
 * Easy - простой класс, нет наследования, массивов, статических полей, аннотации SkipField (4 балла)
 * Easy + Medium - нет наследования, массивов, но есть статические поля и поля с аннотацией SkipField (6 баллов)
 * Easy + Medium + Hard - нужно реализовать все требования задания (10 баллов)
 * <p>
 * Итого, по заданию можно набрать 10 баллов
 * Баллы могут снижаться за неэффективный или неаккуратный код
 */
public class ReflectionToStringHelper {

    public static String reflectiveToString(Object object) {
        // TODO: implement
        if (object == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder("{");
        Class<?> clazz = object.getClass();
        while (clazz != null) {
            Field[] FieldsArray = Arrays.stream(clazz.getDeclaredFields())
                    .filter(field -> !Modifier.isStatic(field.getModifiers())
                            && !field.isAnnotationPresent(SkipField.class))
                    .sorted(Comparator.comparing(Field::getName))
                    .toArray(Field[]::new);
            try {
                for (Field field : FieldsArray) {
                    sb.append(field.getName()).append(": ");
                    field.setAccessible(true);
                    if (field.getType().isArray()) {
                        parseArrayToString(field.get(object), sb);
                    } else {
                        sb.append(field.get(object));
                    }
                    sb.append(", ");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            clazz = clazz.getSuperclass();
        }
        int length = sb.length();
        if (length >= 2) {
            sb.delete(length - 2, length);
        }
        sb.append("}");
        return sb.toString();
    }

    private static void parseArrayToString(Object array, StringBuilder sb) {
        if (array == null) {
            sb.append("null");
            return;
        }
        sb.append("[");
        int length = Array.getLength(array);
        for (int i = 0; i < length - 1; i++) {
            sb.append(Array.get(array, i)).append(", ");
        }
        if (length != 0) {
            sb.append(Array.get(array, length - 1));
        }
        sb.append("]");
    }
}
