package ru.gooamoko.roiservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class PetitionsUpdateService {
	private final static Logger log = LoggerFactory.getLogger(PetitionsUpdateService.class);
	private final RoiService roiService;


	public PetitionsUpdateService(RoiService roiService) {
		this.roiService = roiService;
	}

	@Scheduled(cron = "0 */5 * ? * *")
	public void updatePetitions() {
		log.info("Получаем список инициатив на голосовании.");
		roiService.processPoll();

		log.info("обрабатываем список устаревших инициатив.");
		roiService.processOld();
	}
}
