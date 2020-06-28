package com.application.community.controllers;

import com.application.community.models.Announcement;
import com.application.community.services.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/announcement")
public class AnnouncementsController {
    private static final Integer DEFAULT_PAGE_SIZE = 20;

    @Autowired
    private AnnouncementService announcementService;

    @GetMapping
    public List<Announcement> getAnnouncements(@RequestParam(value = "searchString", required = false) String searchString,
            @RequestParam(value = "startTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date start,
            @RequestParam(value = "endTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date end,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size) {
        searchString = searchString == null ? "" : searchString;
        start = start == null ? new Date(0) : start;
        end = end == null ? new Date(System.currentTimeMillis()) : end;
        page = page == null ? 0 : page;
        size = size == null ? DEFAULT_PAGE_SIZE : size;
        return announcementService.getAllAnnouncements(searchString, start, end, page, size);
    }

    @PostMapping
    public Announcement publishAnnouncement(@RequestParam("content") String content) {
        return announcementService.publishAnnouncement(content);
    }
}
