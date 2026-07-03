package Rest;

import Domain.Joc;
import Domain.Jucator;
import Domain.Runda;
import Repository.JocRepository;
import Repository.JucatorRepository;
import Repository.RundaRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/jocuri")
@CrossOrigin
public class JocController {

    private static final Logger logger = LogManager.getLogger();

    @Autowired
    private JocRepository jocRepository;

    @Autowired
    private JucatorRepository jucatorRepository;

    @Autowired
    private RundaRepository rundaRepository;

    @GetMapping("/jucator/{alias}")
    public ResponseEntity<?> getJocuriByJucator(@PathVariable String alias) {
        logger.traceEntry("getJocuriByJucator alias={}", alias);

        Jucator jucator = jucatorRepository.findByAlias(alias);
        if (jucator == null) {
            logger.warn("Jucător negăsit cu alias={}", alias);
            return new ResponseEntity<>(
                    "Jucătorul cu alias-ul '" + alias + "' nu a fost găsit.",
                    HttpStatus.NOT_FOUND
            );
        }

        List<Map<String, Object>> responseList = StreamSupport
                .stream(jocRepository.findAllGamesWithDetails().spliterator(), false)
                .filter(joc -> joc.getJucator() != null
                        && joc.getJucator().getAlias().equals(alias)
                        && joc.getPunctaj() >= 5)
                .map(joc -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("idJoc", joc.getId());
                    map.put("numarRunde", rundaRepository.findRundeByJocId(joc.getId()).size());
                    map.put("pozitiiRunde", rundaRepository.findRundeByJocId(joc.getId()).stream()
                            .map(Runda::getPozitie)
                            .collect(Collectors.toList()));
                    map.put("punctajeRunde", rundaRepository.findRundeByJocId(joc.getId()).stream()
                            .map(Runda::getPunctaj)
                            .collect(Collectors.toList()));
                    return map;
                })
                .collect(Collectors.toList());

        logger.trace("Returning {} jocuri for alias={}", responseList.size(), alias);
        logger.traceExit();
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        logger.error("Excepție în JocController: {}", e.getMessage(), e);
        return new ResponseEntity<>(
                "Eroare server: " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
