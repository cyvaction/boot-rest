package net.robi42.boot.rest;

import lombok.val;
import net.robi42.boot.domain.MessageDto;
import net.robi42.boot.util.IntegrationTestBase;
import org.junit.Test;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class MessageResourceTest extends IntegrationTestBase {

    @Test
    public void respondsSuccessfully() throws Exception {
        val response = restApi.path(MessageResource.BASE_PATH).request().get();

        assertThat(response.getStatusInfo().getFamily()).isEqualTo(Response.Status.Family.SUCCESSFUL);
        response.close();
    }

    @Test
    public void respondsWithMessages() throws Exception {
        val messages = restApi.path(MessageResource.BASE_PATH).request().get(new GenericType<List<MessageDto>>() {});

        assertThat(messages.size()).isEqualTo(3);
        assertThat(messages.stream().map(MessageDto::getBody).collect(toList()).get(0)).isEqualTo("Baz");
    }

    @Test
    public void findsMessageViaSearch() throws Exception {
        val messages = restApi.path(MessageResource.BASE_PATH + "/search")
                .queryParam("q", "bar").request()
                .get(new GenericType<List<MessageDto>>() {});
        assertThat(messages).hasSize(1);

        val message = messages.get(0);
        assertThat(message.getId()).isNotEmpty();
        assertThat(message.getBody()).isEqualTo("Bar");
    }
}
