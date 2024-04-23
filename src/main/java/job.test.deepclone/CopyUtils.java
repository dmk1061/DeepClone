package job.test.deepclone;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

public class CopyUtils {
    private static Map<Object, Object> copiedObjects = new IdentityHashMap<>();
    // ключ в мапе мутабельный - объекты не должны меняться во время копирования в паралельных потоках

    public static <T> T deepCopyInit(final T object) throws IllegalAccessException, InstantiationException {
        copiedObjects = new IdentityHashMap<>();
        return deepCopy(object);
    }

    public static <T> T deepCopy(final T object) throws IllegalAccessException, InstantiationException {
        if (object == null) {
            return null;
        }
        // Проверяем, был ли объект уже скопирован
        if (copiedObjects.containsKey(object)) {
            return (T) copiedObjects.get(object);
        }
        // Создаем новый экземпляр объекта
        Class<?> clazz = object.getClass();
        T copy = (T) clazz.newInstance();
        // Добавляем объект в список скопированных
        copiedObjects.put(object, copy);
        // Рекурсивно копируем поля объекта
        copyFields(object, copy);

        return copy;
    }

    private static void copyFields(final Object source, final Object destination) throws IllegalAccessException, InstantiationException {
        // Получаем все поля класса, включая приватные
        Class cl = source.getClass();
        while (cl != null) {
            final Field[] fields = cl.getDeclaredFields();
            for (Field field : fields) {
              field.setAccessible(true);
              copyField(field, source, destination);
            }
            cl = cl.getSuperclass();
        }
    }

    private static void copyField (Field field, Object source, final Object destination) throws IllegalAccessException, InstantiationException {
        // Получаем значение поля source
        final Object value = field.get(source);
        if (value != null) {
            // Если поле является массивом
            if (value.getClass().isArray()) {
                Object newArray =deepCopyArray(value, field.getType());
                field.set(destination, newArray);
            }
            // Если поле является коллекцией
            else if (value instanceof Collection) {
                Collection destinationCollection = deepCopyCollection(value, field.getType());
                field.set(destination, destinationCollection);
            }
            // Если поле является объектом
            else if (!field.getType().isPrimitive() && !(value instanceof String)) {
                // Рекурсивно копируем объект
                field.set(destination, deepCopy(value));
            } else {
                // Просто копируем значение поля
                field.set(destination, value);
            }
        }
    }

    private static Object deepCopyArray(Object value, Class<?>type) throws IllegalAccessException, InstantiationException {
        int length = Array.getLength(value);
        final Object newArray = Array.newInstance(value.getClass().getComponentType(), length);
        for (int i = 0; i < length; i++) {
            final Object arrayElement = Array.get(value, i);
            if (arrayElement != null && (!type.isPrimitive() && !(arrayElement instanceof String) && !(arrayElement instanceof Number) )) {
                // Если элемент массива является массивом, рекурсивно копируем егоu
                try {
                    Array.set(newArray, i, deepCopy(arrayElement));
                }catch (Exception e) {
                    int a=0;
                }
            } else {
                // Иначе просто копируем элемент
                Array.set(newArray, i, arrayElement);
            }
        }
        return newArray;
    }

    private static Collection deepCopyCollection (Object value, Class<?>type) throws IllegalAccessException, InstantiationException {
        final Collection<?> sourceCollection = (Collection<?>) value;
        final Collection<Object> destinationCollection = createNewCollectionInstance(value);
        for (Object element : sourceCollection) {
            if (element != null && (!type.isPrimitive() && !(element instanceof String) && !(element instanceof Number))) {
                 //Если элемент коллекции является массивом, рекурсивно копируем его
                destinationCollection.add(deepCopy(element));

            } else {
                // Иначе просто копируем элемент
                destinationCollection.add(element);
            }
        }
        return destinationCollection;
    }

    private static Collection<Object> createNewCollectionInstance(final Object value) {
        if (value instanceof List) {
            if (value instanceof LinkedList) {
                return new LinkedList<>();
            } else if (value instanceof Vector) {
                return new Vector<>();
            } else if (value instanceof Stack) {
                return new Stack<>();
            } else {
                return new ArrayList<>();
            }
        } else if (value instanceof Set) {
            if (value instanceof SortedSet) {
                if (value instanceof NavigableSet) {
                    return new TreeSet<>(((NavigableSet<Object>) value).comparator());
                } else {
                    return new TreeSet<>(((SortedSet<Object>) value).comparator());
                }
            } else {
                return new HashSet<>();
            }
        } else if (value instanceof Queue) {
            if (value instanceof Deque) {
                if (value instanceof LinkedList) {
                    return new LinkedList<>();
                } else if (value instanceof ArrayDeque) {
                    return new ArrayDeque<>();
                } else {
                    return new LinkedList<>();
                }
            } else {
                return new PriorityQueue<>();
            }
        } else {
            // Если тип коллекции неизвестен, просто возвращаем ArrayList
            return new ArrayList<>();
        }
    }
}