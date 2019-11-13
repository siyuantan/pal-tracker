package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JdbcTimeEntryRepository implements TimeEntryRepository {
    private JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(DataSource datasource) {
        jdbcTemplate = new JdbcTemplate(datasource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO time_entries (project_id, user_id, date, hours) " +
                            "VALUES (?, ?, ?, ?)",
                    RETURN_GENERATED_KEYS
            );

            statement.setLong(1, timeEntry.getProjectId());
            statement.setLong(2, timeEntry.getUserId());
            statement.setDate(3, Date.valueOf(timeEntry.getDate()));
            statement.setInt(4, timeEntry.getHours());

            return statement;
        }, generatedKeyHolder);

        return find(generatedKeyHolder.getKey().longValue());
    }

    @Override
    public TimeEntry find(long id) {
        return jdbcTemplate.query(
                "SELECT id, project_id, user_id, date, hours FROM time_entries WHERE id = ?",
                new Object[]{id}, eachRowExtractor);
    }

    private final RowMapper<TimeEntry> rowMapper = (rs, rowNum) -> new TimeEntry(
            rs.getLong("id"),
            rs.getLong("project_id"),
            rs.getLong("user_id"),
            rs.getDate("date").toLocalDate(),
            rs.getInt("hours")
    );

    private final ResultSetExtractor<TimeEntry> eachRowExtractor =
            (rs) -> rs.next() ? rowMapper.mapRow(rs, 1) : null;

    @Override
    public List<TimeEntry> list() {
        return jdbcTemplate.query(
                "SELECT id, project_id, user_id, date, hours FROM time_entries",
                new Object[]{}, rowMapper);
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "Update time_entries set project_id = ?, user_id=?,date=?,hours=? where id=?"
            );
            statement.setLong(1, timeEntry.getProjectId());
            statement.setLong(2, timeEntry.getUserId());
            statement.setDate(3, Date.valueOf(timeEntry.getDate()));
            statement.setInt(4, timeEntry.getHours());
            statement.setLong(5, id);

            return statement;
        });

        return find(id);
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM time_entries WHERE ID=?"
            );
            statement.setLong(1, id);

            return statement;
        });
    }
}
