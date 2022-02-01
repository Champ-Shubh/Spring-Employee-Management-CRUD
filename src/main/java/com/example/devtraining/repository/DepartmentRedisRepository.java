package com.example.devtraining.repository;

import com.example.devtraining.model.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

@Repository
public class DepartmentRedisRepository {

    private static final String REDIS_ENTITY = "department";

    private final RedisTemplate<String, Department> redisTemplate;

    private HashOperations<String, Long, Department> hashOperations;

    @Autowired
    public DepartmentRedisRepository(RedisTemplate<String, Department> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    public void addDepartment(Department department) {
        hashOperations.put(REDIS_ENTITY, department.getDepartmentId(), department);
    }

    public void deleteDepartment(Long id) {
        hashOperations.delete(REDIS_ENTITY, id);
    }

    public void updateDepartment(Long id, Department newDepartment) {
        hashOperations.put(REDIS_ENTITY, id, newDepartment);
    }

    public Department getDepartmentById(Long id) {
        return hashOperations.get(REDIS_ENTITY, id);
    }
}
