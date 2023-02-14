package ru.gooamoko.roiservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.gooamoko.roiservice.client.RoiClient;
import ru.gooamoko.roiservice.config.properties.ProcessingConfig;
import ru.gooamoko.roiservice.model.PetitionListDataModel;
import ru.gooamoko.roiservice.model.PetitionModel;
import ru.gooamoko.roiservice.service.process.AddOrUpdatePetitionProcessor;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class PetitionsUpdateService {
    private final static Logger log = LoggerFactory.getLogger(PetitionsUpdateService.class);
    private final static int DEFAULT_THREADS_COUNT = 6;
    private final static int DEFAULT_DAYS_COUNT = 7;
    private final RoiClient roiClient;
    private final PetitionDocumentService petitionDocumentService;
    private final int threadsCount;
    private final int daysCount;

    public PetitionsUpdateService(RoiClient roiClient,
                                  PetitionDocumentService petitionDocumentService,
                                  ProcessingConfig config) {
        this.roiClient = roiClient;
        this.petitionDocumentService = petitionDocumentService;
        this.threadsCount = config.getThreadsCount() == null ? DEFAULT_THREADS_COUNT : config.getThreadsCount();
        this.daysCount = config.getOldDaysCount() == null ? DEFAULT_DAYS_COUNT : config.getOldDaysCount();
    }

    @Scheduled(cron = "0 0 */8 * * *")
    public void update() {
        PetitionListDataModel poll = roiClient.getPoll();
        List<PetitionModel> petitionsList = poll.getData();
        long time = System.currentTimeMillis();
        log.info("Получено инициатив: {}.", petitionsList.size());
        int recordsAdded = 0;
        ExecutorService threadPool = Executors.newFixedThreadPool(threadsCount);
        try {
            ExecutorCompletionService<Integer> executorService = new ExecutorCompletionService<>(threadPool);
            for (PetitionModel model : petitionsList) {
                executorService.submit(new AddOrUpdatePetitionProcessor(roiClient, petitionDocumentService, model));
            }

            // Получаем результаты
            for (int i = 0; i < petitionsList.size(); i++) {
                Future<Integer> future = executorService.take();
                recordsAdded += future.get();
            }
        } catch (Exception e) {
            log.error("Ошибка обработки списка инициатив.", e);
        } finally {
            threadPool.shutdown();
        }

        log.info("Добавлено инициатив в БД: {}. Потоков для обработки: {}. Добавление заняло {} мс.",
                recordsAdded, threadsCount, System.currentTimeMillis() - time);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void clearOld() {
        LocalDate date = LocalDate.now().minusDays(daysCount);
        petitionDocumentService.deleteOld(date);
    }
}
