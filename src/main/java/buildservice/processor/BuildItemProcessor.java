package buildservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;


public class BuildItemProcessor implements ItemProcessor<Build, Build> {

    private static final Logger log = LoggerFactory.getLogger(BuildItemProcessor.class);
    private static final String DELETE_BUILD = "delete from builds where id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Build process(final Build build) throws Exception {
        final long ID = build.getID();
        final String toolbox = build.getToolbox().toUpperCase();
        final String ctf = build.getCtf().toUpperCase();

        final Build transformedBuild = new Build(ID, toolbox, ctf);

        log.info("Converting (" + build + ") into (" + transformedBuild + ")");

        int updated = jdbcTemplate.update(DELETE_BUILD,build.getID());
        log.info("deleted (" + updated + ") records");

        return transformedBuild;
    }

}
