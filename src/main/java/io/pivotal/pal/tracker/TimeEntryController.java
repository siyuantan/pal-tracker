package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntryController {

    private TimeEntryRepository timeEntryRepository;
    private Counter actionCounter;
    private DistributionSummary summary;

    public TimeEntryController(TimeEntryRepository timeEntryRepository, MeterRegistry registry) {
        actionCounter = registry.counter("timeEntry.actionCounter");
        summary = registry.summary("timeEntry.summary");

        this.timeEntryRepository = timeEntryRepository;
    }

    @PostMapping("/time-entries")
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {
        actionCounter.increment();

        TimeEntry newTimeEntry = this.timeEntryRepository.create(timeEntryToCreate);
        return new ResponseEntity(newTimeEntry, HttpStatus.CREATED);
    }

    @GetMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable("id") Long timeEntryId) {
        actionCounter.increment();

        TimeEntry readResult = this.timeEntryRepository.find(timeEntryId);
        ResponseEntity<TimeEntry> responseToBeReturn;
        if (readResult == null) {
            responseToBeReturn = new ResponseEntity<TimeEntry>(HttpStatus.NOT_FOUND);
        } else {
            responseToBeReturn = new ResponseEntity<TimeEntry>(readResult, HttpStatus.OK);
        }
        return responseToBeReturn;
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        actionCounter.increment();

        return new ResponseEntity<List<TimeEntry>>(this.timeEntryRepository.list(), HttpStatus.OK);
    }

    @PutMapping("/time-entries/{id}")
    public ResponseEntity update(@PathVariable("id") long timeEntryId, @RequestBody TimeEntry newTimeEntry) {
        actionCounter.increment();

        TimeEntry updatedTimeEntry = this.timeEntryRepository.update(timeEntryId, newTimeEntry);
        if(updatedTimeEntry == null) {
            return new ResponseEntity<TimeEntry>(HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<TimeEntry>(updatedTimeEntry, HttpStatus.OK);
        }
    }

    @DeleteMapping("/time-entries/{id}")
    public ResponseEntity delete(@PathVariable("id") long timeEntryId) {
        actionCounter.increment();

        this.timeEntryRepository.delete(timeEntryId);
        return new ResponseEntity<TimeEntry>(HttpStatus.NO_CONTENT);
    }
}
