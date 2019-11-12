package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {
    private Map<Long, TimeEntry> map = new HashMap<>();
    private Long counter = 0L;

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        counter += 1L;
        timeEntry.setId(counter);
        map.put(counter, timeEntry);
        return map.get(counter);
    }

    @Override
    public TimeEntry find(long id) {
        return map.get(id);
    }

    @Override
    public List<TimeEntry> list() {
        return new ArrayList<>(map.values());
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        timeEntry.setId(id);
        map.replace(id, timeEntry);
        return map.get(id);
    }

    @Override
    public void delete(long id) {
        map.remove(id);
    }
}
