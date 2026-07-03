package Rest;

import Domain.Cuvant;
import Repository.CuvantRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cuvinte")
@CrossOrigin
public class CuvantController {

    private static final Logger logger = LogManager.getLogger();

    @Autowired
    private CuvantRepository cuvantRepository;

    @PostMapping
    public ResponseEntity<?> addConfiguratie(@RequestBody Cuvant cuvant) {
        logger.traceEntry("addCuvant {}", cuvant);
        try {
            String valoare = cuvant.getValoare();
            String indiciu =  cuvant.getIndiciu();
            String complexitate = cuvant.getComplexitate();
            if (valoare == null || valoare.trim().isEmpty()) {
                return new ResponseEntity<>("Cuvantul nu poate fi null!", HttpStatus.BAD_REQUEST);
            }

            if(indiciu == null || indiciu.trim().isEmpty()) {
                return new ResponseEntity<>("Indiciul nu poate fi null!", HttpStatus.BAD_REQUEST);
            }

            if(complexitate == null || complexitate.trim().isEmpty()
                    || (!complexitate.equals("usor") && !complexitate.equals("mediu") && !complexitate.equals("ridicat")
                            && !complexitate.equals("foarte ridicat"))) {
                return new ResponseEntity<>("Complexitatea poate fi usor, mediu, ridicat sau foarte ridicat!", HttpStatus.BAD_REQUEST);
            }

            Cuvant saved = cuvantRepository.save(cuvant);
            logger.traceExit("Cuvant salvat: {}", saved);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);

        } catch (Exception e) {
            logger.error("Eroare la salvarea configuratiei: {}", e.getMessage(), e);
            return new ResponseEntity<>("Eroare: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        logger.error("Excepție în ConfiguratieController: {}", e.getMessage(), e);
        return new ResponseEntity<>(
                "Eroare server: " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
