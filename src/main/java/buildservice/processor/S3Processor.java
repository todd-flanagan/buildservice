package buildservice.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import buildservice.Build;

public class S3Processor implements ItemProcessor<Build, Build> {

    private static final Logger log = LoggerFactory.getLogger(S3Processor.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Build process(final Build build) throws Exception {


        return build;
    }

}
