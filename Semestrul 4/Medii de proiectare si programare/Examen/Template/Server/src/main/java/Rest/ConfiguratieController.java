package Rest;

import Domain.Configuratie;
import Repository.ConfiguratieRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/configuratii")
@CrossOrigin
public class ConfiguratieController {

    private static final Logger logger = LogManager.getLogger();

    @Autowired
    private ConfiguratieRepository configuratieRepository;

    @PostMapping
    public ResponseEntity<?> addConfiguratie(@RequestBody Configuratie configuratie) {
        logger.traceEntry("addConfiguratie {}", configuratie);
        try {
            String valori = configuratie.getValori();
            if (valori == null || valori.trim().isEmpty()) {
                return new ResponseEntity<>("Configurația nu poate fi goală!", HttpStatus.BAD_REQUEST);
            }

            String[] elemente = valori.split(",");
            if(elemente.length != 2*configuratie.getN()) {
                return new ResponseEntity<>("Numar elemente invalid!", HttpStatus.BAD_REQUEST);
            }

            for (String el : elemente) {
                try { int v = Integer.parseInt(el.trim()); if (v <= 0) throw new NumberFormatException(); }
                catch (NumberFormatException ex) {
                    return new ResponseEntity<>("Toate valorile trebuie să fie întregi pozitivi!", HttpStatus.BAD_REQUEST);
                }
            }

            Configuratie saved = configuratieRepository.save(configuratie);
            logger.traceExit("Configuratie salvată: {}", saved);
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
