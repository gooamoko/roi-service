package ru.gooamoko.roiservice.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gooamoko.roiservice.model.PetitionDocumentDataModel;
import ru.gooamoko.roiservice.service.PetitionDocumentService;

@RestController
@RequestMapping("petitions")
public class PetitionController {
    private final PetitionDocumentService petitionDocumentService;

    public PetitionController(PetitionDocumentService petitionDocumentService) {
        this.petitionDocumentService = petitionDocumentService;
    }


    @PostMapping(value = "save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> save(@RequestBody PetitionDocumentDataModel dataModel) {
        petitionDocumentService.savePetition(dataModel.getData());
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "find", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> find(@RequestParam("q") String searchText) {
        return ResponseEntity.ok(petitionDocumentService.findPetitions(searchText));
    }

    @GetMapping(value = "clear")
    public ResponseEntity<String> clear() {
        petitionDocumentService.clear();
        return ResponseEntity.ok().build();
    }
}
