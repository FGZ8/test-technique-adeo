package adeo.leroymerlin.cdp;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;
import java.util.stream.Collectors;

@RunWith(MockitoJUnitRunner.class)
public class EventServiceTest {

    private static final String QUERY_1 = "D";

    private static final String QUERY_2 = "X";

    private static final Long ID_1 = 1L;

    private static final String COMMENT_1 = "I loved it!";

    @InjectMocks
    public EventService eventService;

    @Mock
    public EventRepository eventRepository;

    /**
     * Tested method {@link EventService#getFilteredEvents(String)}
     */
    @Test
    public void event_must_be_updated_when_update() {
        // Arrange the values
        final Event updatedEvent = new Event();
        updatedEvent.setNbStars(5);
        updatedEvent.setComment(COMMENT_1);

        Event originalEvent = buildEvent1();
        Mockito.doReturn(originalEvent).when(eventRepository).findOne(ID_1);

        // Act
        eventService.update(1L, updatedEvent);

        // Assert
        Assert.assertEquals(Integer.valueOf(5), originalEvent.getNbStars());
        Assert.assertEquals(COMMENT_1, originalEvent.getComment());
    }

    /**
     * Tested method {@link EventService#getFilteredEvents(String)}
     */
    @Test
    public void some_events_are_present_after_filtering_when_getFilteredEvents() {
        // Arrange
        final List<Event> events = new ArrayList<>();
        events.add(buildEvent1());
        events.add(buildEvent2());
        Mockito.doReturn(events).when(eventRepository).findAllBy();

        // Act
        final List<Event> filteredEvents = eventService.getFilteredEvents(QUERY_1);

        // Assert
        Assert.assertNotNull(filteredEvents);
        Assert.assertEquals(1, filteredEvents.size());

        final Set<Band> filteredBands = filteredEvents
                .stream()
                .flatMap(event -> event.getBands()
                        .stream())
                .collect(Collectors.toSet());
        Assert.assertEquals(2, filteredBands.size());
    }

    /**
     * Tested method {@link EventService#getFilteredEvents(String)}
     */
    @Test
    public void all_events_are_absent_after_filtering_when_getFilteredEvents() {
        // Arrange
        final List<Event> events = new ArrayList<>();
        events.add(buildEvent1());
        events.add(buildEvent2());
        Mockito.doReturn(events).when(eventRepository).findAllBy();

        // Act
        final List<Event> filteredEvents = eventService.getFilteredEvents(QUERY_2);

        // Assert
        Assert.assertEquals(0, filteredEvents.size());
    }

    private Event buildEvent1() {
        final Event event = new Event();
        final Set<Band> bands = new HashSet<>();

        event.setId(ID_1);
        event.setTitle("Main Square Festival");
        bands.add(buildBand1());
        event.setBands(bands);
        event.setNbStars(2);
        event.setComment("I don't like it.");

        return event;
    }

    private Event buildEvent2() {
        final Event event = new Event();
        final Set<Band> bands = new HashSet<>();

        event.setId(2L);
        event.setTitle("TomorrowLand");
        bands.add(buildBand2());
        bands.add(buildBand3());
        event.setBands(bands);
        event.setNbStars(4);
        event.setComment("Great.");

        return event;
    }

    private Band buildBand1() {
        final Band band = new Band();
        final Set<Member> members = new HashSet<>();

        band.setName("Band 1");
        members.add(buildMember("Member 1 A"));
        members.add(buildMember("Member 2 B"));
        members.add(buildMember("Member 3 C"));
        band.setMembers(members);

        return band;
    }

    private Band buildBand2() {
        final Band band = new Band();
        final Set<Member> members = new HashSet<>();

        band.setName("Band 2");
        members.add(buildMember("Member 4 D"));
        members.add(buildMember("Member 5 E"));
        members.add(buildMember("Member 6 F"));
        band.setMembers(members);

        return band;
    }

    private Band buildBand3() {
        final Band band = new Band();
        final Set<Member> members = new HashSet<>();

        band.setName("Band 3");
        members.add(buildMember("Member 7 D"));
        members.add(buildMember("Member 8 E"));
        members.add(buildMember("Member 9 F"));
        band.setMembers(members);

        return band;
    }

    private Member buildMember(final String name) {
        final Member member = new Member();
        member.setName(name);
        return member;
    }
}