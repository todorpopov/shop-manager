package com.shop_manager.storage_engine;

import com.shop_manager.exceptions.ConstraintViolationException;
import com.shop_manager.models.interfaces.Identifiable;
import com.shop_manager.storage_engine.annotations.*;
import com.shop_manager.storage_engine.enums.Operation;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;

public final class EntityValidator {
    public static <T> void validate(T entity, Collection<T> existingRows, Operation op) throws ConstraintViolationException {
        Objects.requireNonNull(entity, "entity");

        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);

            Object value;
            try {
                value = f.get(entity);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            if (f.isAnnotationPresent(NotNull.class)) {
                if (value == null) {
                    throw new ConstraintViolationException("[" + entity.getClass().getSimpleName() + "." + f.getName() + "] must not be null");
                }
            }

            Length length = f.getAnnotation(Length.class);
            if (length != null && value != null) {
                if (!(value instanceof String s)) {
                    throw new ConstraintViolationException("[" + entity.getClass().getSimpleName() + "." + f.getName() + "] @Length can only be used on String");
                }
                if (s.length() < length.min() || s.length() > length.max()) {
                    throw new ConstraintViolationException("[" + entity.getClass().getSimpleName() + "." + f.getName() + "] length must be between " + length.min() + " and " + length.max());
                }
            }

            Min min = f.getAnnotation(Min.class);
            if (min != null && value != null) {
                checkMin(entity, f, value, min.value());
            }

            Max max = f.getAnnotation(Max.class);
            if (max != null && value != null) {
                checkMax(entity, f, value, max.value());
            }

            Unique unique = f.getAnnotation(Unique.class);
            if (unique != null && value != null) {
                for (T row : existingRows) {
                    if (shouldIgnoreSameRow(entity, row, op)) continue;

                    Object otherValue = readField(row, f.getName());
                    if (Objects.equals(value, otherValue)) {
                        throw new ConstraintViolationException("[" + entity.getClass().getSimpleName() + "." + f.getName() + "] " + unique.message());
                    }
                }
            }
        }
    }

    private static <T> boolean shouldIgnoreSameRow(T entity, T row, Operation op) {
        if (op != Operation.UPDATE) return false;

        if (entity instanceof Identifiable && row instanceof Identifiable) {
            Object id1 = ((Identifiable) entity).getId();
            Object id2 = ((Identifiable) row).getId();
            return Objects.equals(id1, id2);
        }
        return false;
    }

    private static Object readField(Object target, String fieldName) {
        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.get(target);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private static void checkMin(Object entity, Field f, Object value, long min) {
        String message = "[" + entity.getClass().getSimpleName() + "." + f.getName() + "] must be >= " + min;
        if (value instanceof BigDecimal bd) {
            if (bd.compareTo(BigDecimal.valueOf(min)) < 0) {
                throw new ConstraintViolationException(message);
            }
            return;
        }
        if (value instanceof Number n) {
            if (n.doubleValue() < min) {
                throw new ConstraintViolationException(message);
            }
            return;
        }
        throw new ConstraintViolationException("[" + entity.getClass().getSimpleName() + "." + f.getName() + "] @Min can only be used on Number/BigDecimal");
    }

    private static void checkMax(Object entity, Field f, Object value, long max) {
        String message = "[" + entity.getClass().getSimpleName() + "." + f.getName() + "] must be <= " + max;
        if (value instanceof BigDecimal bd) {
            if (bd.compareTo(BigDecimal.valueOf(max)) > 0) {
                throw new ConstraintViolationException(message);
            }
            return;
        }
        if (value instanceof Number n) {
            if (n.doubleValue() > max) {
                throw new ConstraintViolationException(message);
            }
            return;
        }
        throw new ConstraintViolationException("[" + entity.getClass().getSimpleName() + "." + f.getName() + "] @Max can only be used on Number/BigDecimal");
    }
}
