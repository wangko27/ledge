package org.objectledge.web.rest;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * CompositeJacksonMapper provides ObjectMappers for Jersey. If you want to do some specific
 * configuration of JSON mapping then you should extend JacksonMapper interface and append your
 * component in container to sequence of mappers. CompositeJacksonMapper works with default
 * configuration for any type out of the box so you don't do anything specific like wrapping root
 * object then you do not have to do anything.
 * 
 * <pre>
 * Register it in container like so:
 * {@code
 * <component class="org.objectledge.web.rest.CompositeJacksonMapper">
 *   <sequence>
 *   </sequence>
 * </component>
 * }
 * </pre>
 * 
 * @author Marek Lewandowski
 * @since 20130-02-19
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class CompositeJacksonMapper
    implements ContextResolver<ObjectMapper>
{
    private final List<JacksonMapper> mappers;

    private final JacksonMapper defaultMapper = new DefaultJacksonMapper();

    public CompositeJacksonMapper(JacksonMapper[] mappers)
    {
        final List<JacksonMapper> listed = Arrays.asList(mappers);
        this.mappers = listed;
    }

    @Override
    public ObjectMapper getContext(Class<?> type)
    {
        for(JacksonMapper mapper : mappers)
        {
            if(mapper.providesFor(type))
            {
                return mapper.getMapper();
            }
        }
        return defaultMapper.getMapper();
    }

}
