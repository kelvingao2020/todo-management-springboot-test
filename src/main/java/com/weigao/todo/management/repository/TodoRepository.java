package com.weigao.todo.management.repository;

import com.weigao.todo.management.entity.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<TodoItem, Long> {

    List<TodoItem> findAllByDoerName(String name);
}
