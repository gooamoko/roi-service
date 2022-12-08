package ru.gooamoko.roiservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.gooamoko.roiservice.es.document.PetitionDocument;
import ru.gooamoko.roiservice.model.*;
import ru.gooamoko.roiservice.repository.PetitionDocumentRepository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class PetitionDocumentService {
    private final static Logger log = LoggerFactory.getLogger(PetitionDocumentService.class);
    private final PetitionDocumentRepository documentRepository;


    public PetitionDocumentService(PetitionDocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }


    public void savePetition(PetitionDocumentModel petitionDocumentModel) {
        try {
            PetitionDocument document = convertToDocument(petitionDocumentModel);
            documentRepository.save(document);
        } catch (Exception e) {
            log.error("Petition save error.", e);
        }
    }

    public void clear() {
        documentRepository.deleteAll();
    }

    public List<PetitionDocumentLinkModel> findPetitions(String searchText) {
        List<PetitionDocument> petitions = documentRepository.findPetitions(searchText);
        return petitions == null || petitions.isEmpty() ? Collections.emptyList() :
                petitions
                .stream()
                .map(petition -> new PetitionDocumentLinkModel(petition.getTitle(), petition.getLink()))
                .toList();
    }


    private PetitionDocument convertToDocument(PetitionDocumentModel petitionDocumentModel) {
        PetitionDocument document = new PetitionDocument();
        document.setId(String.valueOf(petitionDocumentModel.getId()));
        document.setCode(petitionDocumentModel.getCode());
        document.setTitle(petitionDocumentModel.getTitle());
        document.setLink(petitionDocumentModel.getUrl());
        document.setDescription(petitionDocumentModel.getDescription());
        document.setProspective(petitionDocumentModel.getProspective());

        GeoModel geo = petitionDocumentModel.getGeo();
        if (geo != null) {
            document.setArea(geo.getArea());
            document.setRegion(geo.getRegion());
            document.setMunicipality(geo.getMunicipality());
        }

        LevelModel level = petitionDocumentModel.getLevel();
        if (level != null) {
            document.setLevel(level.getTitle());
        }

        StatusModel status = petitionDocumentModel.getStatus();
        if (status != null) {
            document.setLevel(status.getTitle());
        }

        ResultModel result = petitionDocumentModel.getResult();
        if (result != null) {
            document.setLevel(result.getTitle());
        }

        List<CategoryModel> categories = petitionDocumentModel.getCategories();
        if (categories != null && !categories.isEmpty()) {
            List<String> categoryNames = categories
                    .stream()
                    .filter(Objects::nonNull)
                    .map(CategoryModel::getTitle)
                    .filter(Objects::nonNull)
                    .toList();
            document.setCategories(categoryNames);
        }
        return document;
    }
}
