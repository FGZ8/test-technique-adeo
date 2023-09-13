package adeo.leroymerlin.cdp;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface EventRepository extends Repository<Event, Long> {

    void delete(Long eventId);

    List<Event> findAllBy();

    void save(Event event);

    Event findOne(Long id);
}
