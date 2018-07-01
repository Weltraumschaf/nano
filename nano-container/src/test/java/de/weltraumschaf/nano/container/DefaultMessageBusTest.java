package de.weltraumschaf.nano.container;

import de.weltraumschaf.commons.testing.DelayedRepeater;
import de.weltraumschaf.nano.api.messaging.Message;
import de.weltraumschaf.nano.api.messaging.MessageSubscriber;
import de.weltraumschaf.nano.api.messaging.MessageTopic;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Tests for {@link DefaultMessageBus}.
 */
public class DefaultMessageBusTest {
    private final DefaultMessageBus sut = new DefaultMessageBus();

    @Test(expected = NullPointerException.class)
    public void subscribe_topicNotNull() {
        sut.subscribe(null, mock(MessageSubscriber.class));
    }

    @Test(expected = NullPointerException.class)
    public void subscribe_subscriberNotNull() {
        sut.subscribe(mock(MessageTopic.class), null);
    }

    @Test(expected = NullPointerException.class)
    public void unsubscribe_subscriberNotNull() {
        sut.unsubscribe(null);
    }

    @Test(expected = NullPointerException.class)
    public void publish_messageNotNull() {
        sut.publish(null);
    }

    @Test
    public void allSubscribersReceiverMessagesForTheirTopics() throws InterruptedException {
        final MessageSubscriber subscriberOne = mock(MessageSubscriber.class);
        final MessageSubscriber subscriberTwo = mock(MessageSubscriber.class);

        sut.subscribe(TestTopics.TOPIC_ONE, subscriberOne);
        sut.subscribe(TestTopics.TOPIC_ONE, subscriberTwo);
        sut.subscribe(TestTopics.TOPIC_TWO, subscriberTwo);

        final Message messageOne = new Message(TestTopics.TOPIC_ONE,"message one");
        final Message messageTwo = new Message(TestTopics.TOPIC_TWO,"message two");
        final Message messageThree = new Message(TestTopics.TOPIC_THREE,"message three");

        sut.publish(messageOne);
        sut.publish(messageTwo);
        sut.publish(messageThree);

        DelayedRepeater.create(50, 3).execute(() -> {
            verify(subscriberOne, times(1)).receive(messageOne);
            verify(subscriberOne, never()).receive(messageTwo);
            verify(subscriberOne, never()).receive(messageThree);

            verify(subscriberTwo, times(1)).receive(messageOne);
            verify(subscriberTwo, times(1)).receive(messageTwo);
            verify(subscriberTwo, never()).receive(messageThree);

            return null;
        });
    }

    @Test
    public void unsubscribedDoesNotReceiveMessageAnymore() throws InterruptedException {
        final MessageSubscriber subscriberOne = mock(MessageSubscriber.class);
        final MessageSubscriber subscriberTwo = mock(MessageSubscriber.class);

        sut.subscribe(TestTopics.TOPIC_ONE, subscriberOne);
        sut.subscribe(TestTopics.TOPIC_TWO, subscriberOne);
        sut.subscribe(TestTopics.TOPIC_ONE, subscriberTwo);
        sut.subscribe(TestTopics.TOPIC_TWO, subscriberTwo);

        final Message messageOne = new Message(TestTopics.TOPIC_ONE, "message one");
        final Message messageTwo = new Message(TestTopics.TOPIC_TWO,"message two");
        final Message messageThree = new Message(TestTopics.TOPIC_ONE, "message three");
        final Message messageFour = new Message(TestTopics.TOPIC_TWO, "message four");

        sut.publish(messageOne);
        sut.publish(messageTwo);

        sut.unsubscribe(subscriberOne);

        sut.publish(messageThree);
        sut.publish(messageFour);

        DelayedRepeater.create(50, 3).execute(() -> {
            verify(subscriberOne, times(1)).receive(messageOne);
            verify(subscriberOne, times(1)).receive(messageTwo);
            verify(subscriberOne, never()).receive(messageThree);
            verify(subscriberOne, never()).receive(messageFour);

            verify(subscriberTwo, times(1)).receive(messageOne);
            verify(subscriberTwo, times(1)).receive(messageTwo);
            verify(subscriberTwo, times(1)).receive(messageThree);
            verify(subscriberTwo, times(1)).receive(messageFour);
           return null;
        });
    }

    private enum TestTopics implements MessageTopic {
        TOPIC_ONE, TOPIC_TWO, TOPIC_THREE
    }
}