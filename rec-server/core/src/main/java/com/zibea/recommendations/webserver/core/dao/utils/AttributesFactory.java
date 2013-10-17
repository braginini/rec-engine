package com.zibea.recommendations.webserver.core.dao.utils;

import com.google.protobuf.InvalidProtocolBufferException;
import com.zibea.recommendations.webserver.core.dao.exception.AttributesMapException;
import com.zibea.recommendations.webserver.core.dao.proto.AttributesMapProto;
import com.zibea.recommendations.webserver.core.dao.utils.bodybuilder.BodyReader;
import com.zibea.recommendations.webserver.core.dao.utils.bodybuilder.BodyReaderException;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mikhail Bragin
 */
public class AttributesFactory {

    private static final byte PARSER_VERSION = 0;


    /**
     * Encodes attributes via Protobuf
     *
     * @param map
     * @return
     * @throws AttributesMapException
     */
    public static byte[] encodeAttributesMap(@NotNull Map<String, String> map) throws AttributesMapException {

        /*Preconditions.checkNotNull(map);

        AttributesMapProto.Map.Builder mapBuilder = AttributesMapProto.Map.newBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String code = entry.getKey();
            String attribute = entry.getValue();

            if (attribute != null) {
                AttributesMapProto.Map.KeyValue kv = AttributesMapProto.Map.KeyValue.newBuilder()
                        .setKey(code)
                        .setValue(entry.getValue()).build();
                mapBuilder.addNodes(kv);
            }
        }

        AttributesMapProto.Map mapProto = mapBuilder.build();

        AttributesMapProto.AttributesMap protoAction = AttributesMapProto.AttributesMap.newBuilder()
                .setAttributes(mapProto)
                .build();

        try {

            BodyBuilder bodyBuilder = new BodyBuilder();
            bodyBuilder.putByte(PARSER_VERSION);
            bodyBuilder.putBytes(protoAction.toByteArray());

            return bodyBuilder.getBody();
        } catch (BodyBuilderException e) {
            throw new AttributesMapException("Error while writing bytes to output stream [attributes=" + map + "] " +
                    e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }*/
        return new byte[0];
    }

    public static Map<String, String> decodeAttributesMap(byte[] body) throws AttributesMapException {

        try {

            BodyReader bodyReader = new BodyReader(body);

            byte version = bodyReader.readByte();
            byte[] rawMap = bodyReader.readBytes(body.length - 1);

            AttributesMapProto.AttributesMap actionProto = AttributesMapProto.AttributesMap.parseFrom(rawMap);

            Map<String, String> map = new HashMap<>();

            /*if (actionProto.hasAttributes()) {

                List<AttributesMapProto.Map.KeyValue> rawAttributes = actionProto.getAttributes().getNodesList();

                for (AttributesMapProto.Map.KeyValue kv : rawAttributes) {
                    map.put(kv.getKey(), kv.getValue());
                }
            }*/
            return map;

        } catch (BodyReaderException | InvalidProtocolBufferException e) {
            throw new AttributesMapException("Error while writing bytes to output stream " + e.getMessage());
        }
    }
}
