package ru.gooamoko.roiservice.service.process;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import ru.gooamoko.roiservice.client.RoiClient;
import ru.gooamoko.roiservice.es.document.PetitionDocument;
import ru.gooamoko.roiservice.model.PetitionDocumentDataModel;
import ru.gooamoko.roiservice.model.PetitionModel;
import ru.gooamoko.roiservice.service.PetitionDocumentService;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.Callable;

public class AddOrUpdatePetitionProcessor implements Callable<Integer> {
    private static final Logger log = LoggerFactory.getLogger(AddOrUpdatePetitionProcessor.class);
    private final RoiClient roiClient;
    private final PetitionModel model;
    private final PetitionDocumentService petitionDocumentService;
    private final ObjectMapper mapper;

    public AddOrUpdatePetitionProcessor(RoiClient roiClient, PetitionDocumentService petitionDocumentService,
                                        PetitionModel model) {
        this.roiClient = roiClient;
        this.petitionDocumentService = petitionDocumentService;
        this.model = model;
        this.mapper = new ObjectMapper();
    }

    @Override
    public Integer call() {
        Long id = model.getId();
        Optional<PetitionDocument> optionalDocument = petitionDocumentService.findById(id);
        if (optionalDocument.isEmpty()) {
            // Получаем текст инициативы
            try {
                HttpEntity<String> petition = roiClient.getPetition(id);
                if (petition.hasBody()) {
                    String petitionText = petition.getBody();
                    log.info(petitionText);
                    PetitionDocumentDataModel documentDataModel = mapper.readValue(petitionText, PetitionDocumentDataModel.class);
                    petitionDocumentService.savePetition(documentDataModel.getData());
                    return 1;
                } else {
                    log.warn("Отсутствует тело инициативы.");
                }
            } catch (Exception e) {
                log.error("Ошибка при сохранении инициативы.", e);
            }

        } else {
            // Инициатива уже есть. Просто обновим дату
            PetitionDocument document = optionalDocument.get();
            document.setUpdateDate(LocalDate.now());
            petitionDocumentService.save(document);
        }

        return 0;
    }
}
