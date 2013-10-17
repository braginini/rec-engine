package com.zibea.recommendations.services.partner.dao.impl.proto;

import com.google.protobuf.InvalidProtocolBufferException;
import com.zibea.recommendations.common.hbase.bodybuilder.BodyBuilder;
import com.zibea.recommendations.common.hbase.bodybuilder.BodyBuilderException;
import com.zibea.recommendations.common.hbase.bodybuilder.BodyReader;
import com.zibea.recommendations.common.hbase.bodybuilder.BodyReaderException;
import com.zibea.recommendations.common.model.ItemParam;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikhail Bragin
 */
public class ItemParametersParser {

    private static final byte PARSER_VERSION = 1;

    @NotNull
    public static byte[] encode(@NotNull List<ItemParam> parameters) throws ParameterParserException {

        ItemParametersProto.Parameters.Builder parametersBuilder =
                ItemParametersProto.Parameters.newBuilder();

        for (ItemParam param : parameters) {

            ItemParametersProto.Parameters.Parameter.Builder parameterBuilder =
                    ItemParametersProto.Parameters.Parameter.newBuilder();

            parameterBuilder.setKey(param.getName());
            parameterBuilder.setValue(param.getValue());

            if (param.hasUnit())
                parameterBuilder.setUnit(param.getUnit());

            parametersBuilder.addParam(parameterBuilder.build());
        }

        ItemParametersProto.Parameters encodedParameters = parametersBuilder.build();

        try {

            BodyBuilder bodyBuilder = new BodyBuilder();
            bodyBuilder.putByte(PARSER_VERSION);
            bodyBuilder.putBytes(encodedParameters.toByteArray());

            return bodyBuilder.getBody();
        } catch (BodyBuilderException e) {
            throw new ParameterParserException("Error while writing bytes to output stream", e);
        }
    }

    @NotNull
    public static List<ItemParam> decode(@NotNull byte[] body) throws ParameterParserException {

        BodyReader br = new BodyReader(body);

        try {

            byte parserVersion = br.readByte();

            byte[] rawBody = br.readBytes(body.length - 1);
            ItemParametersProto.Parameters rawParameters = ItemParametersProto.Parameters.parseFrom(rawBody);

            List<ItemParam> parameters = new ArrayList<>();

            for (ItemParametersProto.Parameters.Parameter rawParam : rawParameters.getParamList()) {

                ItemParam parameter = new ItemParam(rawParam.getKey(), rawParam.getValue());
                if (rawParam.hasUnit())
                    parameter.setUnit(rawParam.getUnit());

                parameters.add(parameter);
            }

            return parameters;

        } catch (BodyReaderException e) {
            throw new ParameterParserException("Error reading input stream", e);
        } catch (InvalidProtocolBufferException e) {
            throw new ParameterParserException("Error while parsing encoded data", e);
        }

    }
}
