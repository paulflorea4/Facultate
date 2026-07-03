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

            // TODO: VALIDARE EXAMEN
            // Aici despart 'valori' (ex: valori.split(",")) si verific regulile din enunt.
            // Daca datele nu sunt corecte: return new ResponseEntity<>("Mesaj eroare", HttpStatus.BAD_REQUEST);
            //
            // Exemple frecvente la examen:
            // String[] elemente = valori.split(",");
            //
            // 1. Număr fix de elemente:
            //    if (elemente.length != N)
            //        return new ResponseEntity<>("Trebuie exact N valori!", HttpStatus.BAD_REQUEST);
            //
            // 2. Fără duplicate:
            //    long distincte = Arrays.stream(elemente).map(String::trim).distinct().count();
            //    if (distincte != elemente.length)
            //        return new ResponseEntity<>("Nu sunt permise duplicate!", HttpStatus.BAD_REQUEST);
            //
            // 3. Valori numerice întregi pozitive:
            //    for (String el : elemente) {
            //        try { int v = Integer.parseInt(el.trim()); if (v <= 0) throw new NumberFormatException(); }
            //        catch (NumberFormatException ex) {
            //            return new ResponseEntity<>("Toate valorile trebuie să fie întregi pozitivi!", HttpStatus.BAD_REQUEST);
            //        }
            //    }
            //
            // 4. Matrice pătratică (√N întreg):
            //    double sqrt = Math.sqrt(elemente.length);
            //    if (sqrt != Math.floor(sqrt))
            //        return new ResponseEntity<>("Numărul de valori trebuie să fie un pătrat perfect!", HttpStatus.BAD_REQUEST);

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
