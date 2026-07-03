package org.example.ducksocialnetworkui.repository;

import org.example.ducksocialnetworkui.event.Event;

import java.util.List;

public interface EventRepository extends PagedRepository<Long, Event>{
    void addSubscriber(long eventId, long userId);
    void removeSubscriber(long eventId, long userId);
    List<Long> getSubscribers(long eventId);
    void saveResults(Long eventId,String results);
    void saveDuckToRace(Long eventId,Long duckId);
    void deleteDuckFromRace(Long eventId,Long duckId);
    String findResultsOfRace(Long eventId);
    List<Event> findEventsByStatus(String status);
    void updateStatus(Long eventId, String status);
    boolean findIfUserIsSubscribed(Long eventID, Long userID);
}
