package com.application.community.services;

import com.application.community.controllers.AnnouncementsController;
import com.application.community.models.Announcement;
import com.application.community.repositories.AnnouncementRepository;
import com.application.community.services.AnnouncementService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AnnouncementTest {

    @Autowired
    private AnnouncementsController announcementsController;

    @Autowired
    private AnnouncementService announcementService;


    @Autowired
    private AnnouncementRepository announcementRepository;

    @AfterEach
    public void deleteAllUsers() {
        announcementRepository.deleteAll();
    }

    @Test
    public void testPublishAnnouncement() {
        Date now  = new Date(System.currentTimeMillis());
        String content = "Announcement content";
        Announcement publishedAnnouncement  = announcementsController.publishAnnouncement(content);
        //assertEquals(1L, publishedAnnouncement.getId());
        assertEquals(content, publishedAnnouncement.getContent());
        assertTrue(publishedAnnouncement.getTimestamp().getTime() >= now.getTime());
    }

    @Test
    public void testGetAnnouncementsNoParam() {
        announcementsController.publishAnnouncement("content1");
        announcementsController.publishAnnouncement("content2");

        List<Announcement> allAnnouncements = announcementsController.getAnnouncements(null, null, null, null, null);

        assertEquals(2, allAnnouncements.size());
    }

    @Test
    public void testGetAnnouncementsByTimeRangeParam() {
        long now = System.currentTimeMillis();
        Announcement announcement1 = new Announcement();
        announcement1.setContent("content1");
        announcement1.setTimestamp(new Date(now - 1000));
        announcementRepository.saveAndFlush(announcement1);

        Announcement announcement2 = new Announcement();
        announcement2.setContent("content2");
        announcement2.setTimestamp(new Date(now + 1000));
        announcementRepository.saveAndFlush(announcement2);

        List<Announcement> allAnnouncements = announcementsController.getAnnouncements(null, new Date(now), new Date(now + 2000), null, null);

        assertEquals(1, allAnnouncements.size());
    }

    @Test
    public void testGetAnnouncementsByContentParam() {
        announcementsController.publishAnnouncement("content1");
        announcementsController.publishAnnouncement("content2");

        List<Announcement> allAnnouncements = announcementsController.getAnnouncements("2", null, null, null, null);

        assertEquals(1, allAnnouncements.size());
    }

    @Test
    public void testGetAnnouncementsByPageParam() {
        int numberOfAnnouncements = 10;
        for (int i = 0; i < numberOfAnnouncements; ++i) {
            announcementsController.publishAnnouncement("content" + i);
        }

        List<Announcement> allAnnouncements = announcementsController.getAnnouncements(null, null, null, 0, 5);
        assertEquals(5, allAnnouncements.size());
    }
}
