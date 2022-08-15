package com.usergems.meetingenrichment.calendar.repository;

import com.usergems.meetingenrichment.calendar.model.Meeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;


public interface MeetingRepository extends PagingAndSortingRepository<Meeting, Long>, JpaRepository<Meeting, Long> {
    Page<Meeting> findAllByAccepted_Email(String email, Pageable pageable);

    Page<Meeting> findAllByAccepted_EmailAndStartAfter(String email, LocalDateTime after, Pageable pageable);

    @Query(value = "SELECT acc.email userMail, cust_acc.email customerEmail, sum(1) customerMeetingCount " +
            "FROM USER_ACCEPTED_MEETINGS acc " +
            "join USER_ACCEPTED_MEETINGS cust_acc " +
            "where acc.email = ?1 " +
            "and acc.meeting_id = cust_acc.meeting_id " +
            "and not cust_acc.email like '%usergems%' " +
            "group by acc.email, cust_acc.email", nativeQuery = true)
    List<MeetingCountProjection> findCustomerMeetingCount(String userMail);

    interface MeetingCountProjection {
        String getUserMail();

        String getCustomerEmail();

        int getCustomerMeetingCount();
    }

}
