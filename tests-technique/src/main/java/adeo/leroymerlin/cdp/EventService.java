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

        final List<Event> eventsToRemove = new ArrayList<>();

        // Browsing events list
        for (Event event : events) {
            final Set<Band> bandsToRemove = new HashSet<>();

            checkMembersName(query, event, bandsToRemove);

            // Removes from the original bands set, the bands without band member having a name containing the query
            bandsToRemove.forEach(event.getBands()::remove);

            if (!CollectionUtils.isEmpty(event.getBands()) && event.getBands().size() != 0) {
                // If there is at least one band still having members after filtering
                // Modifies Event title to add the number of bands with kept members
                event.setTitle(event.getTitle().concat("[".concat(String.valueOf(event.getBands().size())).concat("]")));
            } else {
                // Otherwise, the event is added in the list of the event to be removed
                eventsToRemove.add(event);
            }
        }

        // Removes the events without band members having a name containing the query
        events.removeAll(eventsToRemove);

        return events;
    }

    /**
     * Checks if the members names contains the query
     * @param query to be checked if it is contained in member name
     * @param event the event checked in this iteration
     * @param bandsToRemove the list of bands to be removed from the events
     */
    private void checkMembersName(String query, Event event, Set<Band> bandsToRemove) {
        // Browsing bands set
        for (Band band : event.getBands()) {

            // For each band member, we collect in a new set, only the ones with names containing the query
            Set<Member> matchedMembers = band.getMembers()
                    .stream()
                    .filter(Objects::nonNull)
                    .filter(member -> member.getName()
                            .contains(query))
                    .collect(Collectors.toSet());

            if (!CollectionUtils.isEmpty(matchedMembers)) {
                // If there is at least one member having a name containing the query

                // Removes all members then adds only kept members
                band.getMembers().clear();
                band.setMembers(matchedMembers);

                // Modifies Band name to add the number of band members having a name containing the query
                band.setName(band.getName().concat( "[").concat(String.valueOf(band.getMembers().size())).concat("]"));

            } else {
                // Removing the band from the band set without members after filtering
                bandsToRemove.add(band);
            }
        }
    }
}
