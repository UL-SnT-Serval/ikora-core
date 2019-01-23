package org.ukwikora.export.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.ukwikora.analytics.EvolutionResults;
import org.ukwikora.model.*;

import java.io.IOException;

public class KeywordsNamesSerializer extends JsonSerializer<EvolutionResults> {
    @Override
    public void serialize(EvolutionResults results, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartArray();

        for(Project project: results.getProjects()){
            jsonGenerator.writeStartObject();

            jsonGenerator.writeStringField("commit ID", project.getCommitId());
            jsonGenerator.writeStringField("time", project.getDateTime().toString());

            jsonGenerator.writeArrayFieldStart("keywords");
            writeKeywords(jsonGenerator, project.getElements(UserKeyword.class));
            writeKeywords(jsonGenerator, project.getElements(TestCase.class));
            jsonGenerator.writeEndArray();

            jsonGenerator.writeEndObject();
        }

        jsonGenerator.writeEndArray();
    }

    private void writeKeywords(JsonGenerator jsonGenerator, ElementTable<? extends KeywordDefinition> keywords) throws IOException {
        for(KeywordDefinition keyword: keywords){
            jsonGenerator.writeStartObject();

            jsonGenerator.writeStringField("type", keyword.getClass().getSimpleName());
            jsonGenerator.writeNumberField("depth", keyword.getLevel());
            jsonGenerator.writeStringField("name", keyword.getName().toString());

            jsonGenerator.writeEndObject();
        }
    }
}