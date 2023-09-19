package adeo.leroymerlin.cdp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * Gets all the events
     * @return the list of all the events in database
     */
    public List<Event> getEvents() {
        return eventRepository.findAllBy();
    }

    /**
     * Deletes an event by Id
     * @param id id of the event
     */
    public void delete(Long id) {
        eventRepository.delete(id);
    }

    /**
     * Updates the events
     * @param id id of the event
     * @param updatedEvent the updatedEvent
     */
    public void update(Long id, Event updatedEvent) {

        // Getting the event by Id in database
        final Event event = eventRepository.findOne(id);

        // Updating its values by the new ones
        event.setNbStars(updatedEvent.getNbStars());
        event.setComment(updatedEvent.getComment());

        eventRepository.save(event);
    }

    /**
     * Filters the events based on band member names
     * @param query to check it is contained in member names
     * @return the list of events after filtering
     */
    public List<Event> getFilteredEvents(String query) {
        List<Event> events = eventRepository.findAllBy();

        // Browsing events list
        for (Event event : events) {

            // 1. Removes members from bands if their names does not contain the query
            event.getBands()
                    .forEach(band -> band.getMembers()
                            .removeIf(member -> !member.getName()
                                    .contains(query)));

            // 2. Removes from the event bands without members after first step
            event.getBands()
                    .removeIf(b -> CollectionUtils.isEmpty(b.getMembers()));

            // 3. Adds the members number after each band name
            event.getBands().forEach(band -> {
                if (!CollectionUtils.isEmpty(band.getMembers())) {
                    band.setName(band.getName().concat( "[").concat(String.valueOf(band.getMembers().size())).concat("]"));
                }
            });

            // 4. Adds the bands number after the event title
            if (!CollectionUtils.isEmpty(event.getBands())) {
                event.setTitle(event.getTitle().concat("[".concat(String.valueOf(event.getBands().size())).concat("]")));
            }
        }

        // 5. Removes the events without any band after filtering
        events.removeIf(event -> CollectionUtils.isEmpty(event.getBands()));

        return events;
    }
}
