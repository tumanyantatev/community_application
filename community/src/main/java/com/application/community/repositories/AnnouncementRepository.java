package com.application.community.repositories;

import com.application.community.models.Announcement;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    @Query("SELECT a FROM announcements a WHERE content like %:searchString% AND posting_date between :startTime AND :endTime")
    List<Announcement> findAnnouncements(@Param("searchString") String searchString,
                                        @Param("startTime") Date start, @Param("endTime") Date end, Pageable page);

    @Query("SELECT a FROM announcements a WHERE content like %:searchString%")
    List<Announcement> findAnnouncements(@Param("searchString") String searchString);
}
