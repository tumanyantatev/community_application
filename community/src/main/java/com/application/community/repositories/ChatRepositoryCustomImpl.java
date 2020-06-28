package com.application.community.repositories;

import com.application.community.models.Chat;
import com.application.community.models.Message;
import com.application.community.models.User;
import com.application.community.repositories.ChatRepositoryCustom;
import org.springframework.data.util.Pair;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

public class ChatRepositoryCustomImpl implements ChatRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Boolean addUsersToChat(List<Pair<Long, Long>> userChatPairs) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO user_chats(chat_id, user_id) VALUES ");
        for (int i = 0; i < userChatPairs.size(); ++i) {
            Pair<Long, Long> userChatPair = userChatPairs.get(i);
            sb.append("(");
            sb.append(userChatPair.getFirst());
            sb.append(", ");
            sb.append(userChatPair.getSecond());
            sb.append(")");
            if (i != userChatPairs.size() - 1) {
                sb.append(",");
            }
        }

        Query query = entityManager.createNativeQuery(sb.toString());
        int i = query.executeUpdate();
        return i == userChatPairs.size();
    }

    @Override
    @Transactional
    public void deleteAll() {
        List<Long> allChatIds = entityManager.createQuery("SELECT chatId FROM chats").getResultList();
        if (!allChatIds.isEmpty()) {
            String ids = generateInList(allChatIds);
            int i = entityManager.createNativeQuery("DELETE FROM user_chats uc WHERE uc.chat_id in " + ids).executeUpdate();
        }
        entityManager.createQuery("DELETE FROM chats");
    }

    @Transactional
    @Override
    public Boolean deleteUsersFromChat(Long chatId, List<Long> userIds) {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM user_chats uc WHERE uc.chat_id = ");
        sb.append(chatId);
        sb.append("AND uc.user_id in ");
        String ids = generateInList(userIds);
        sb.append(ids);
        Query query = entityManager.createNativeQuery(sb.toString());
        int i = query.executeUpdate();
        return i == userIds.size();
    }

    @Override
    public void addMessageToChat(Long chatId, String content, Long senderUserId) {
        Query query = entityManager.createQuery("SELECT c FROM chats c WHERE c.chatId = " + chatId);
        Chat chat = (Chat) query.getSingleResult();
        Message message = new Message();
        message.setContent(content);
        User user = new User();
        user.setUserId(senderUserId);
        message.setTimestamp(new Date(System.currentTimeMillis()));
        message.setUser(user);
        message.setChat(chat);
        chat.getMessages().add(message);
        entityManager.persist(chat);
    }

    private String generateInList(List<Long> idList) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 0; i < idList.size(); ++i) {
            Long id = idList.get(i);
            sb.append(id);
            if (i != idList.size() - 1) {
                sb.append(",");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
