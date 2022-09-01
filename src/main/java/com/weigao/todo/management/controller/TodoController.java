package com.weigao.todo.management.controller;

import com.weigao.todo.management.entity.TodoItem;
import com.weigao.todo.management.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/todomanagement")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping("/admin/getAll")
    public List<TodoItem> getAllItems(){
        return todoService.getAllItems();
    }

    @GetMapping("/admin/getItem/{id}")
    public Optional<TodoItem> getItem(@PathVariable Long id){
        return todoService.getItem(id);
    }

    // admin to see past to-do items and the person assigned to it
    @GetMapping("/admin/pastItems")
    public List<TodoItem> getPastItems(){
        return todoService.getPastItems();
    }

    // admin to see pending todo items
    @GetMapping("/admin/pendingItems")
    public List<TodoItem> getPendingItems(){
        return todoService.getPendingItems();
    }

    // admin to create new to-do items and assign it to a user
    @PostMapping("/admin/newItem/user/{name}")
    public TodoItem createItem(@RequestBody TodoItem item,
                               @PathVariable String name){
        return todoService.createItem(item);
    }

    // admin to delete a todo item
    @DeleteMapping("/admin/delete/{id}")
    public String deleteItemById(@PathVariable Long id){
        return todoService.deleteItemById(id);
    }

    // admin to assign a pending TO-DO item to other person
    @PutMapping("/admin/assignUser")
    public TodoItem assignUser(@RequestParam Long id,
                               @RequestParam String name){
        return todoService.assignUser(id, name);
    }

    //Every user should only be able
    // to see its own TO-DO items.
    @GetMapping("/user/getItems/{name}")
    public List<TodoItem> userGetItems(@PathVariable String name){
        List<TodoItem> list = todoService.userGetItems(name);
        return list;
    }

    //Every user should only be able
    // to complete its own TO-DO items.
    @PutMapping("/user/{username}/resolveItem/{id}")
    public TodoItem userResolveItem(@PathVariable String username,
                                    @PathVariable Long id){
        return todoService.userResolveItem(username, id);
    }

    //The user can only check it as completed and doing
    // so will remove the item from his list (ie, change the item to be a past item)
    @PutMapping("/user/checkItem")
    public TodoItem userCheckItem(@RequestParam String name,
                              @RequestParam Long id){
        return todoService.userCheckItem(name, id);
    }
}
