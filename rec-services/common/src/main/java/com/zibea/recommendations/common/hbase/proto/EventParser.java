package com.zibea.recommendations.common.hbase.proto;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zibea.recommendations.common.hbase.bodybuilder.BodyBuilder;
import com.zibea.recommendations.common.hbase.bodybuilder.BodyBuilderException;
import com.zibea.recommendations.common.hbase.bodybuilder.BodyReader;
import com.zibea.recommendations.common.hbase.bodybuilder.BodyReaderException;
import com.zibea.recommendations.common.model.event.Event;
import com.zibea.recommendations.common.model.event.ItemViewEvent;
import com.zibea.recommendations.common.model.event.PurchaseEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Mikhail Bragin
 */
public class EventParser {

    private static final byte PARSER_VERSION = 1;

    public static byte[] encode(Event event) throws EventParserException {

        EventProto.Attributes encodedAttributes = encodeAttributes(event);

        switch (event.getType()) {

            case ITEM_VIEW:

                EventProto.ItemViewEventProto iv = EventProto.ItemViewEventProto.newBuilder()
                        .setAttributes(encodedAttributes)
                        .setItemId(((ItemViewEvent) event).getParameter())
                        .build();

                return build(iv);

            case PURCHASE:

                EventProto.PurchaseEventProto p = EventProto.PurchaseEventProto.newBuilder()
                        .setAttributes(encodedAttributes)
                        .addAllItemIds(((PurchaseEvent) event).getParameter())
                        .build();

                return build(p);

        }

        throw new EventParserException("Unknown event type " + event.getType());

    }

    private static EventProto.Attributes encodeAttributes(Event event) {
        EventProto.Attributes.Builder attributesBuilder = EventProto.Attributes.newBuilder();
        //Set<Map.Entry<String, String>> should be here otherwise compilation error see  Java Language Specification, section 4.8:
        for (Map.Entry<String, String> entry : (Set<Map.Entry<String, String>>) event.getAttributes().entrySet()) {

            EventProto.Attributes.KeyValue kv = EventProto.Attributes.KeyValue.newBuilder()
                    .setKey(entry.getKey())
                    .setValue(entry.getValue())
                    .build();

            attributesBuilder.addNodes(kv);

        }

        return attributesBuilder.build();
    }

    private static byte[] build(GeneratedMessage eventBody) throws EventParserException {
        try {

            BodyBuilder bodyBuilder = new BodyBuilder();
            bodyBuilder.putByte(PARSER_VERSION);
            bodyBuilder.putBytes(eventBody.toByteArray());

            return bodyBuilder.getBody();
        } catch (BodyBuilderException e) {
            throw new EventParserException("Error while writing bytes to output stream", e);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Event decode(short type, String apiKey, String ruId, Long timestamp, byte[] rawBody) throws EventParserException {

        Event.EventType eventType = Event.EventType.lookup(type);

        if (eventType == null) {
            throw new EventParserException("Unknown event type " + type);
        }

        BodyReader br = new BodyReader(rawBody);

        try {

            byte parserVersion = br.readByte();
            byte[] rawEvent = br.readBytes(rawBody.length - 1);

            switch (eventType) {

                case ITEM_VIEW:

                    EventProto.ItemViewEventProto iv = EventProto.ItemViewEventProto.parseFrom(rawEvent);
                    return new ItemViewEvent(apiKey, ruId, timestamp,
                            decodeAttributes(iv.getAttributes()), iv.getItemId());

                case PURCHASE:

                    EventProto.PurchaseEventProto p = EventProto.PurchaseEventProto.parseFrom(rawEvent);
                    return new PurchaseEvent(apiKey, ruId, timestamp,
                            decodeAttributes(p.getAttributes()), p.getItemIdsList());

                default:
                    throw new EventParserException("Unknown event type " + type);
            }

        } catch (BodyReaderException e) {
            throw new EventParserException("Error reading input stream", e);
        } catch (InvalidProtocolBufferException e) {
            throw new EventParserException("Error while parsing encoded data", e);
        }

    }

    /**
     * his method is used when u don't need any information about event except {@link Event#parameter}
     * Used in map reduce jobs.
     *
     * WARN! NOT FOR REGULAR USE!
     *
     * @param type
     * @param rawBody
     * @return
     * @throws EventParserException
     */
    public static Event decode(short type, byte[] rawBody) throws EventParserException {
        return decode(type, null, null, null, rawBody);
    }

    @NotNull
    private static Map<String, String> decodeAttributes(EventProto.Attributes attributes) {

        if (attributes == null || attributes.getNodesList().isEmpty())
            return Collections.EMPTY_MAP;

        Map<String, String> attrs = new HashMap<>(attributes.getNodesCount());

        for (EventProto.Attributes.KeyValue kv : attributes.getNodesList()) {
            attrs.put(kv.getKey(), kv.getValue());
        }

        return attrs;
    }
}
