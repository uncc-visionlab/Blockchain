/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agent;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

/**
 *
 * @author arwillis
 */
public class ByteArraySerializer extends JsonSerializer<byte[]> {

    @Override
    public void serialize(byte[] bytes, JsonGenerator jgen,
            SerializerProvider provider) throws IOException,
            JsonProcessingException {

        StringBuilder builder = new StringBuilder();
        for (final byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                builder.append('0');
            }
            builder.append(hex);
        }
        jgen.writeString(builder.toString());
        
//        jgen.writeStartArray();
//
//        for (byte b : bytes) {
//            jgen.writeNumber(unsignedToBytes(b));
//        }
//
//        jgen.writeEndArray();

    }

    private static int unsignedToBytes(byte b) {
        return b & 0xFF;
    }

}
