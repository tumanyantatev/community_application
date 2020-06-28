package com.application.community.services;

import com.application.community.controllers.ChatsController;
import com.application.community.models.Chat;
import com.application.community.models.Message;
import com.application.community.models.User;
import com.application.community.repositories.ChatRepository;
import com.application.community.repositories.MessageRepository;
import com.application.community.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.application.community.helper.UsersHelper.getUser;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class ChatTest {

    private static final int NUMBER_OF_USERS = 10;
    private static final List<Long> userIds = new ArrayList<>(10);

    @Autowired
    private ChatsController chatController;

    @Autowired
    private ChatService announcementService;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @BeforeAll
    public void createUsers() {
        for (int i = 0; i < NUMBER_OF_USERS; ++i) {
            User user = userRepository.saveAndFlush(getUser(i));
            userIds.add(user.getUserId());
        }
    }

    @BeforeEach
    public void deleteAllChats() {
        chatRepository.deleteAll();
    }

    @Test
    public void testCreateChat() {
        Chat chat = chatController.createChat();
        assertNotNull(chat.getChatId());
    }

    @Test
    public void testAddUsersToChatAndGetUsersInChat() {
        Chat chat = chatController.createChat();
        assertNotNull(chat.getChatId());

        Boolean added = chatController.addUsersToChat(chat.getChatId(), userIds);
        assertTrue(added);

        List<User> usersInChat = chatController.getUsersInChat(chat.getChatId());
        assertEquals(NUMBER_OF_USERS, usersInChat.size());
    }

    @Test
    public void testDeleteUsersFromChat() {
        Chat chat = chatController.createChat();
        assertNotNull(chat.getChatId());

        Boolean added = chatController.addUsersToChat(chat.getChatId(), userIds);
        assertTrue(added);

        List<User> usersInChat = chatController.getUsersInChat(chat.getChatId());
        assertEquals(NUMBER_OF_USERS, usersInChat.size());

        Boolean removed = chatController.removeUsersFromChat(chat.getChatId(), Arrays.asList(userIds.get(0), userIds.get(1)));
        assertTrue(removed);

        usersInChat = chatController.getUsersInChat(chat.getChatId());
        assertEquals(NUMBER_OF_USERS - 2, usersInChat.size());
    }

    @Test
    public void testGetChatsForUser() {
        Chat chat1 = chatController.createChat();
        Chat chat2 = chatController.createChat();

        chatController.addUsersToChat(chat1.getChatId(), Collections.singletonList(userIds.get(0)));
        chatController.addUsersToChat(chat2.getChatId(), Collections.singletonList(userIds.get(0)));

        List<Chat> chatsForUser = chatController.getChatsForUser(userIds.get(0));
        assertEquals(2, chatsForUser.size());
    }

    @Test
    public void testPostMessageToChat() {
        Chat chat = chatController.createChat();
        assertNotNull(chat.getChatId());

        chatController.addUsersToChat(chat.getChatId(), Arrays.asList(userIds.get(0), userIds.get(1)));

        chatController.postMessageToChat(chat.getChatId(), "hello", userIds.get(0));
        chatController.postMessageToChat(chat.getChatId(), "world", userIds.get(1));

        List<Message> messagesInChat = chatController.getMessagesInChat(chat.getChatId(), null, null, null);
        assertEquals(2, messagesInChat.size());
    }
}
