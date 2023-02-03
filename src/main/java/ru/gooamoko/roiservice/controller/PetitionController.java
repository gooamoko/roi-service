package ru.gooamoko.roiservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.gooamoko.roiservice.service.PetitionDocumentService;
import ru.gooamoko.roiservice.service.PetitionsUpdateService;

@RestController
@RequestMapping("petitions")
public class PetitionController {
    private static final Logger log = LoggerFactory.getLogger(PetitionController.class);
    private final PetitionDocumentService petitionDocumentService;
    private final PetitionsUpdateService petitionsUpdateService;

    public PetitionController(PetitionDocumentService petitionDocumentService,
                              PetitionsUpdateService petitionsUpdateService) {
        this.petitionDocumentService = petitionDocumentService;
        this.petitionsUpdateService = petitionsUpdateService;
    }


    @GetMapping(value = "find", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> find(@RequestParam("q") String searchText) {
        log.info("Ищем инициативы, содержащие '{}'.", searchText);
        return ResponseEntity.ok(petitionDocumentService.findPetitions(searchText));
    }

    @GetMapping(value = "clear-all")
    public ResponseEntity<String> clearAll() {
        log.info("Запущен процесс удаления всех инициатив.");
        petitionDocumentService.clear();
        return ResponseEntity.ok("Все инициативы удалены.");
    }

    @GetMapping(value = "clear-old")
    public ResponseEntity<String> clearOld() {
        log.info("Запущен процесс удаления устаревших инициатив.");
        petitionsUpdateService.clearOld();
        return ResponseEntity.ok("Устаревшие инициативы удалены.");
    }

    @GetMapping(value = "update")
    public ResponseEntity<String> update() {
        log.info("Запущен процесс обновления списка инициатив.");
        petitionsUpdateService.update();
        return ResponseEntity.ok("Список инициатив обновлен.");
    }
}
