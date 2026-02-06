package com.shop_manager.storage_engine;

import com.shop_manager.exceptions.AlreadyExistsException;
import com.shop_manager.exceptions.ConstraintViolationException;
import com.shop_manager.exceptions.NotFoundException;
import com.shop_manager.models.interfaces.Identifiable;
import com.shop_manager.storage_engine.enums.Operation;
import com.shop_manager.utils.IdGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class InMemoryTable<T extends Identifiable> {
    private final IdGenerator idGenerator = new IdGenerator();
    private final Map<Long, T> rows = new HashMap<>();

    public T get(Long id) throws NotFoundException {
        T value = rows.get(id);
        if (value == null) {
            throw new NotFoundException("Entity with id " + id + " not found");
        }
        return value;
    }

    public List<T> all() {
        return List.copyOf(rows.values());
    }

    public void insert(T entity) throws AlreadyExistsException, ConstraintViolationException {
        EntityValidator.validate(entity, rows.values(), Operation.INSERT);

        Long id = entity.getId();
        if (id == null) {
            entity.setId(this.idGenerator.nextId());
            id = entity.getId();
        }
        T prev = rows.putIfAbsent(id, entity);
        if (prev != null) {
            throw new AlreadyExistsException("Entity with id " + id + " already exists");
        }
    }

    public void update(T entity) throws NotFoundException, ConstraintViolationException {
        Long id = entity.getId();
        if (!rows.containsKey(id)) {
            throw new NotFoundException("Entity with id " + id + " not found");
        }

        EntityValidator.validate(entity, rows.values(), Operation.UPDATE);
        rows.put(id, entity);
    }

    public void delete(Long id) throws NotFoundException {
        if (!rows.containsKey(id)) {
            throw new NotFoundException("Entity with id " + id + " not found");
        }
        rows.remove(id);
    }

    public void clear() {
        rows.clear();
        idGenerator.reset();
    }
}
