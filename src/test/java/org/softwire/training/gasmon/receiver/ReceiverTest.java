package org.softwire.training.gasmon.receiver;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.BatchResultErrorEntry;
import com.amazonaws.services.sqs.model.DeleteMessageBatchRequestEntry;
import com.amazonaws.services.sqs.model.DeleteMessageBatchResult;
import com.amazonaws.services.sqs.model.DeleteMessageBatchResultEntry;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.fasterxml.jackson.core.io.JsonStringEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReceiverTest {

    private static final String QUEUE_URL = "Mock Queue URL";
    private static final String MESSAGE = "{" +
            "   \"locationId\": \"4887f60d-d65c-4594-9087-aec8373b3de0\", " +
            "   \"eventId\": \"d8d00d6a-8e91-44dd-926d-2b389c436d45\", " +
            "   \"timestamp\": 123456789, " +
            "   \"value\": 2" +
            "}";

    private AmazonSQS sqs;
    private Receiver receiver;

    @BeforeEach
    public void setUp() {
        sqs = mock(AmazonSQS.class);
        when(sqs.deleteMessageBatch(eq(QUEUE_URL), anyListOf(DeleteMessageBatchRequestEntry.class)))
                .thenReturn(new DeleteMessageBatchResult().withSuccessful(new DeleteMessageBatchResultEntry()));
        receiver = new Receiver(sqs, QUEUE_URL);
    }

    @Test
    public void deletesMessagesFromQueue() {
        when(sqs.receiveMessage(any(ReceiveMessageRequest.class))).thenReturn(buildMessages(Collections.singletonList(MESSAGE)));

        receiver.getMessages();

        verify(sqs, times(1)).deleteMessageBatch(eq(QUEUE_URL), anyListOf(DeleteMessageBatchRequestEntry.class));
    }

    @Test
    public void ignoresErrorsWhenDeletingMessages() {
        DeleteMessageBatchResult result = new DeleteMessageBatchResult().withFailed(new BatchResultErrorEntry().withMessage("Oops"));
        when(sqs.deleteMessageBatch(anyString(), anyListOf(DeleteMessageBatchRequestEntry.class))).thenReturn(result);

        when(sqs.receiveMessage(any(ReceiveMessageRequest.class))).thenReturn(buildMessages(Collections.singletonList(MESSAGE)));

        assertEquals(1, receiver.getMessages().size());
    }

    private ReceiveMessageResult buildMessages(List<String> messageBodies) {
        JsonStringEncoder jsonStringEncoder = new JsonStringEncoder();
        String sqsEnvelope = "{\"Message\": \"%s\"}";
        return new ReceiveMessageResult().withMessages(
                messageBodies
                        .stream()
                        .map(body -> new Message().withBody(String.format(sqsEnvelope, new String(jsonStringEncoder.quoteAsString(body)))))
                        .collect(Collectors.toList())
        );
    }

}
