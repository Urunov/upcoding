package com.urunov.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;

import java.util.Arrays;
import java.util.List;

/**
 * User: hamdamboy
 * Project: TinyURL
 * Github: @urunov
 */

@Configuration
@EnableAutoConfiguration
@PropertySource({"classpath:/configuration/cassandra.properties"})
public class CassandraConfig extends AbstractCassandraConfiguration {

    @Autowired
    private Environment env;

    protected String getKeyspaceName()
    {
        return env.getProperty("spring.data.cassandra.keyspace-name");
    }

    @Override
    public String[] getEntityBasePackages()
    {
        return new String[]{
                "com.urunov"
        };
    }

    public CassandraConfig() {
        super();
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations(){
        if(CreateKeyspaceSpecification.createKeyspace(getKeyspaceName()).getIfNotExists())
            return Arrays.asList(CreateKeyspaceSpecification.createKeyspace(getKeyspaceName()).with(KeyspaceOption.DURABLE_WRITES, true));
        else
            return Arrays.asList(CreateKeyspaceSpecification.createKeyspace(getKeyspaceName()).ifNotExists());
    }






}
