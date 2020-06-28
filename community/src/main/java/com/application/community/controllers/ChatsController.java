package com.application.community.controllers;

import com.application.community.models.Chat;
import com.application.community.models.Message;
import com.application.community.models.User;
import com.application.community.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatsController {

    @Autowired
    private ChatService chatService;

    @PostMapping
    public Chat createChat() {
        return chatService.createChat();
    }

    @GetMapping
    public List<Chat> getChatsForUser(@RequestParam("userId") Long userId) {
        return chatService.getChatsForUser(userId);
    }

    @GetMapping
    @RequestMapping("{chatId}/users")
    public List<User> getUsersInChat(@PathVariable("chatId") Long chatId) {
        return chatService.getUsersInChat(chatId);
    }

    @PostMapping
    @RequestMapping("{chatId}/addusers")
    public Boolean addUsersToChat(@PathVariable("chatId") Long chatId, @RequestParam("userIds") List<Long> userIds) {
        return chatService.addUsersToChat(chatId, userIds);
    }

    @DeleteMapping
    @RequestMapping("{chatId}/removeusers")
    public Boolean removeUsersFromChat(@PathVariable("chatId") Long chatId, @RequestParam("userIds") List<Long> userIds) {
        return chatService.removeUsersFromChat(chatId, userIds);
    }

    @PostMapping
    @RequestMapping("{chatId}/postmessage")
    public void postMessageToChat(@PathVariable("chatId") Long chatId, @RequestParam("content") String content, @RequestParam("sender") Long senderUserId) {
        chatService.postMessage(chatId, content, senderUserId);
    }

    @GetMapping
    @RequestMapping("{chatId}/messages")
    public List<Message> getMessagesInChat(@PathVariable("chatId") Long chatId,
            @RequestParam(value = "searchString", required = false) String searchString,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size) {
        searchString = searchString == null ? "" : searchString;
        return chatService.findMessages(chatId, searchString);
    }

}
