package com.application.community.repositories;

import com.application.community.models.Chat;
import com.application.community.models.Message;
import com.application.community.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long>, ChatRepositoryCustom {

    @Query("SELECT u.chats FROM users u WHERE u.userId = :userId")
    List<Chat> findChatsForUser(@Param("userId") Long userId);

    @Query("SELECT c.users FROM chats c WHERE c.chatId = :chatId")
    List<User> findUserInChat(@Param("chatId") Long chatId);

    @Query("SELECT m FROM messages m WHERE m.chat.chatId = :chatId AND m.content like %:searchString%")
    List<Message> findMessages(@Param("chatId") Long chatId, @Param("searchString") String searchString);

}
