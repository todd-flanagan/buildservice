package buildservice;

import java.util.List;
import java.util.Arrays;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class BuildService{

  @Autowired
      JdbcTemplate jdbcTemplate;

  private static final Logger log = LoggerFactory.getLogger(BuildService.class);

    public List<Build> listQueuedBuilds(){
      log.info("getting builds");

      List<Build> builds = new ArrayList<Build>();


      jdbcTemplate.query(
              "SELECT id, toolbox, ctf FROM builds",
              (rs, rowNum) -> new Build(rs.getLong("id"), rs.getString("toolbox"), rs.getString("ctf"))
      ).forEach(build -> builds.add(build));

      return builds;
    }

    public List<Build> listCompletedBuilds(){
      log.info("getting builds");

      List<Build> builds = new ArrayList<Build>();


      jdbcTemplate.query(
              "SELECT id, toolbox, ctf FROM completedbuilds",
              (rs, rowNum) -> new Build(rs.getLong("id"), rs.getString("toolbox"), rs.getString("ctf"))
      ).forEach(build -> builds.add(build));

      return builds;
    }

}
