package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntryController {

    private TimeEntryRepository timeEntryRepository;

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {

        this.timeEntryRepository = timeEntryRepository;
    }

    @PostMapping("/time-entries")
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry newTimeEntry = this.timeEntryRepository.create(timeEntryToCreate);
        return new ResponseEntity(newTimeEntry, HttpStatus.CREATED);
    }

    @GetMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable("id") Long timeEntryId) {
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
        return new ResponseEntity<List<TimeEntry>>(this.timeEntryRepository.list(), HttpStatus.OK);
    }

    @PutMapping("/time-entries/{id}")
    public ResponseEntity update(@PathVariable("id") long timeEntryId, @RequestBody TimeEntry newTimeEntry) {

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

        this.timeEntryRepository.delete(timeEntryId);
        return new ResponseEntity<TimeEntry>(HttpStatus.NO_CONTENT);
    }
}
