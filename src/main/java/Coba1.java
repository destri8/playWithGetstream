import client.StreamClientImpl;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.getstream.client.StreamClient;
import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.feeds.Feed;
import io.getstream.client.model.filters.FeedFilter;
import io.getstream.client.service.FlatActivityService;
import io.getstream.client.service.FlatActivityServiceImpl;
import io.getstream.client.service.UserActivityService;
import io.getstream.client.service.UserActivityServiceImpl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Destri on 6/26/15.
 */
public class Coba1 {
    public static void main(String args[]) throws IOException, StreamClientException {
        ClientConfiguration streamConfig = new ClientConfiguration();
        StreamClient streamClient = new StreamClientImpl(streamConfig, "vh3f6vwb72tg", "azjxbgmc36a94eudfg9uygfwfarq2ykcamgmpsxhgtx3kdgrdjd8fvtj9jky9k7d");

//        testFollow(streamClient);
        testPostActivity(streamClient);
//        testGetActivity(streamClient);
    }

    private static void testFollow(StreamClient streamClient) throws IOException, StreamClientException {
        Feed feed1 = streamClient.newFeed("user", "4402");
        Feed feed2 = streamClient.newFeed("destri", "4402");

        feed1.follow("destri", "4402");

        FlatActivityServiceImpl<SimpleActivity> flatActivityService =
                feed2.newFlatActivityService(SimpleActivity.class);

        flatActivityService.addActivity(build("actorA3", "object A3", "kerja", "value A3"));


    }

    private static void testGetActivity(StreamClient streamClient) throws IOException, StreamClientException {
        Feed feed1 = streamClient.newFeed("user", "4402");
        System.out.println("feed id " + feed1.getId());

        UserActivityServiceImpl <SimpleActivity> userActivityService = feed1.newUserActivityService(SimpleActivity.class);

        FeedFilter filter = new FeedFilter.Builder().build();
//        FeedFilter filter = new FeedFilter.Builder().withIdGreaterThan("fdc580c6-1be8-11e5-8080-80001f7b2c3a").build();


        List<SimpleActivity> list = userActivityService.getActivities(filter).getResults();

        System.out.println("result [" + list.size() + "] " + list.toString());

    }

    private static void testPostActivity(StreamClient streamClient) throws IOException, StreamClientException {
        Feed feed1 = streamClient.newFeed("user", "4402");

        UserActivityServiceImpl <SimpleActivity> service = feed1.newUserActivityService(SimpleActivity.class);

        SimpleActivity activity = build("actorA1", "object A1", "kerja", "value A1");
        activity.setTo(Arrays.asList("destri:4402"));
        SimpleActivity response = service.addActivity(activity);

        System.out.println("Response " + response);

    }

    private static SimpleActivity build(String actor, String object, String verb, String customField) {
        SimpleActivity activity = new SimpleActivity();
        activity.setActor(actor);
        activity.setObject(object);
        activity.setVerb(verb);
        activity.setCustomField(customField);
        return activity;
    }

    static class SimpleActivity extends BaseActivity {

        private String customField;

        public String getCustomField() {
            return customField;
        }

        @JsonProperty("custom_field")
        public void setCustomField(String customField) {
            this.customField = customField;
        }

        @Override
        public String toString() {
            return super.toString() +
                    " custom_field[" + customField + "]";
        }
    }
}
