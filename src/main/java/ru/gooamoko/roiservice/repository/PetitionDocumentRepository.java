package ru.gooamoko.roiservice.repository;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import ru.gooamoko.roiservice.es.document.PetitionDocument;

import java.util.List;

public interface PetitionDocumentRepository extends ElasticsearchRepository<PetitionDocument, String> {

    @Query("{\"multi_match\":{\"query\":\"?0\",\"fields\":[\"title\",\"description\"]}}")
    List<PetitionDocument> findPetitions(String searchText);
}
