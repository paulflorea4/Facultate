package Rest;

import Domain.Joc;
import Domain.Jucator;
import Domain.Runda;
import Repository.JocRepository;
import Repository.JucatorRepository;
import Repository.RundaRepository;
import Rest.dto.JucatorRundeDTO;
import Rest.dto.RundaDetaliuDTO;
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

    @GetMapping("/{idJoc}/castigatori/{Y}")
    public ResponseEntity<?>getJucatoriCuYCuvinteGhicite(@PathVariable Long idJoc, @PathVariable int Y) {
        logger.traceEntry("getJucatoriCuYCuvinteGhicite Y={}", Y);

        Joc joc = jocRepository.findOne(idJoc);

        if(joc == null){
            return new  ResponseEntity<>("Jocul cu id "+ idJoc + " nu exista", HttpStatus.NOT_FOUND);
        }

        List<Runda> rundeJoc = StreamSupport.stream(rundaRepository.findAll().spliterator(), false)
                .filter(runda -> runda.getJoc() != null && runda.getJoc().getId().equals(idJoc))
                .toList();

        Map<Jucator, List<Runda>> rundePerJucator = rundeJoc.stream().collect(Collectors.groupingBy(Runda::getJucator));

        List<JucatorRundeDTO> rezultat = rundePerJucator.entrySet().stream()
                .filter(entry -> {
                    long cuvinteGhicite = entry.getValue().stream()
                            .filter(runda -> runda.getPunctaj()==15).count();

                    return cuvinteGhicite >= Y;
                })
                .map(entry -> {
                    Jucator jucator = (Jucator) entry.getKey();
                    List<RundaDetaliuDTO> detalii = entry.getValue().stream()
                            .map(r -> new RundaDetaliuDTO(r.getCuvantJucator(), r.getCuvantServer() ,r.getPunctaj())).toList();
                    return new JucatorRundeDTO(jucator.getAlias(), detalii);
                }).toList();

        logger.traceExit(rezultat);
        return new ResponseEntity<>(rezultat, HttpStatus.OK);
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
