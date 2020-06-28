package com.application.community.services;

import com.application.community.models.Chat;
import com.application.community.models.Message;
import com.application.community.models.User;
import com.application.community.repositories.ChatRepository;
import com.application.community.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MessageRepository messageRepository;

    public List<Chat> getChatsForUser(Long userId) {
        return chatRepository.findChatsForUser(userId);
    }

    public List<User> getUsersInChat(Long chatId) {
        return chatRepository.findUserInChat(chatId);
    }

    public Boolean addUsersToChat(Long chatId, List<Long> userIds) {
        List<Pair<Long, Long>> userChatPairs = new ArrayList<>(userIds.size());
        for (Long userId : userIds) {
            userChatPairs.add(Pair.of(chatId, userId));
        }
        return chatRepository.addUsersToChat(userChatPairs);
    }

    public List<Message> findMessages(Long chatId, String searchString) {
        return chatRepository.findMessages(chatId, searchString);
    }

    public Boolean removeUsersFromChat(Long chatId, List<Long> userIds) {
        return chatRepository.deleteUsersFromChat(chatId, userIds);
    }

    @Transactional
    public void postMessage(Long chatId, String content, Long senderUserId) {
        chatRepository.addMessageToChat(chatId, content, senderUserId);
    }

    public Chat createChat() {
        Chat chat = new Chat();
        return chatRepository.saveAndFlush(chat);
    }
}
