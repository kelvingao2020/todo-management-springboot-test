package com.weigao.todo.management.service;

import com.weigao.todo.management.entity.TodoItem;
import com.weigao.todo.management.entity.TodoStatus;
import com.weigao.todo.management.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @PostConstruct
    public void addItems2DB() {
        List<TodoItem> items = new ArrayList<>();
        items.add(new TodoItem(1L, "Chris", TodoStatus.PENDING, "exercise everyday", false));
        items.add(new TodoItem(2L, "Kelvin", TodoStatus.PENDING, "get up early", false));
        items.add(new TodoItem(3L, "Kelvin", TodoStatus.PENDING, "exercise everyday", false));
        items.add(new TodoItem(4L, "Tony", TodoStatus.PENDING, "shopping tomorrow", false));
        items.add(new TodoItem(5L, "Tony", TodoStatus.PENDING, "exercise everyday", false));
        items.add(new TodoItem(6L, "Chris", TodoStatus.PENDING, "quit smoking from tomorrow", false));
        items.add(new TodoItem(7L, "Kelvin", TodoStatus.PENDING, "exercise everyday", false));
        items.add(new TodoItem(8L, "Tony", TodoStatus.PENDING, "clean rooms this weekend", false));
        todoRepository.saveAll(items);
    }

    public List<TodoItem> getAllItems() {
        return todoRepository.findAll();
    }

    public Optional<TodoItem> getItem(Long id)  {
        return todoRepository.findById(id);
    }

    public List<TodoItem> getPastItems() {

        List<TodoItem> list = todoRepository.findAll();
        List<TodoItem> result = new ArrayList<>();
        for(TodoItem item : list){
            if(item.getIfPastItem()){
                result.add(item);
            }
        }
        return result;
    }

    public List<TodoItem> getPendingItems() {

        List<TodoItem> list = todoRepository.findAll();
        List<TodoItem> result = new ArrayList<>();
        for(TodoItem item : list){
            if(item.getStatus().equals(TodoStatus.PENDING)){
                result.add(item);
            }
        }
        return result;
    }

    public TodoItem createItem(TodoItem item){

        return todoRepository.save(item);
    }

    public String deleteItemById(Long id) {
        todoRepository.deleteById(id);
        return "The item with id: " + id + " is deleted!";
    }

    public TodoItem assignUser(Long id, String name) {

        Optional<TodoItem> item = todoRepository.findById(id);
        TodoItem existItem;
        if(item.isPresent()){
            existItem = item.get();
            if(existItem.getStatus().equals(TodoStatus.PENDING)){
                existItem.setDoerName(name);
            } else {
                throw new IllegalArgumentException("Wrong id for pending status.");
            }
        } else {
            throw new IllegalArgumentException("No such item exist with this id.");
        }
        return todoRepository.save(existItem);
    }

    public List<TodoItem> userGetItems(String name) {

        List<TodoItem> list = todoRepository.findAllByDoerName(name);
        return list;
    }

    public TodoItem userResolveItem(String name, Long id) {

        List<TodoItem> items = todoRepository.findAllByDoerName(name);
        for(TodoItem item : items){
            if(item.getId() == id){
                if(item.getStatus().equals(TodoStatus.PENDING)){
                    item.setStatus(TodoStatus.RESOLVED);
                }
            }
            return todoRepository.save(item);
        }
        return null;
    }

    //The user can only check it as completed and doing
    // so will remove the item from his list (without reloading the page)
    public TodoItem userCheckItem(String name, Long id) {

        List<TodoItem> list = todoRepository.findAllByDoerName(name);
        for(TodoItem item : list){
            if(item.getId() == id){
                if(item.getStatus().equals(TodoStatus.RESOLVED)){
                    item.setIfPastItem(true);
                    todoRepository.save(item);
                    return item;
                }
            }
        }
        return null;
    }
}
