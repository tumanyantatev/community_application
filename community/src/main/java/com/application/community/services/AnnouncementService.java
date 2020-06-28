package com.application.community.services;

import com.application.community.models.Announcement;
import com.application.community.repositories.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementRepository announcementRepository;

    public List<Announcement> getAllAnnouncements(String searchString, Date start, Date end, Integer page, Integer size) {
        return announcementRepository.findAnnouncements(searchString, start, end, PageRequest.of(page, size, Sort.by("timestamp")));
    }

    public Announcement publishAnnouncement(String content) {
        Announcement announcement = new Announcement();
        announcement.setContent(content);
        announcement.setTimestamp(new Date(System.currentTimeMillis()));
        Announcement createdAnnouncement = announcementRepository.saveAndFlush(announcement);
        return createdAnnouncement;
    }
}
