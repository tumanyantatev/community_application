package com.application.community.repositories;

import org.springframework.data.util.Pair;

import java.util.List;

public interface ChatRepositoryCustom {

    Boolean addUsersToChat(List<Pair<Long, Long>> userChatPairs);
    Boolean deleteUsersFromChat(Long chatId, List<Long> userIds);
    void addMessageToChat(Long chatId, String content, Long senderUserId);
    void deleteAll();
}
