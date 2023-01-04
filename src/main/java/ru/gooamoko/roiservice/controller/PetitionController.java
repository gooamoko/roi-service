package ru.gooamoko.roiservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gooamoko.roiservice.model.PetitionDocumentDataModel;
import ru.gooamoko.roiservice.service.PetitionDocumentService;

@RestController
@RequestMapping("petitions")
public class PetitionController {
    private static final Logger log = LoggerFactory.getLogger(PetitionController.class);
    private final PetitionDocumentService petitionDocumentService;

    public PetitionController(PetitionDocumentService petitionDocumentService) {
        this.petitionDocumentService = petitionDocumentService;
    }


    @PostMapping(value = "save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> save(@RequestBody PetitionDocumentDataModel dataModel) {
        try {
            petitionDocumentService.savePetition(dataModel.getData());
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            log.error("Ошибка сохранения инициативы.", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "find", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> find(@RequestParam("q") String searchText) {
        return ResponseEntity.ok(petitionDocumentService.findPetitions(searchText));
    }

    @GetMapping(value = "delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteById(@RequestParam("id") String id) {
        try {
            petitionDocumentService.deleteById(id);
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            log.error("Ошибка удаления инициативы.", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "clear")
    public ResponseEntity<String> clear() {
        petitionDocumentService.clear();
        return ResponseEntity.ok().build();
    }
}
