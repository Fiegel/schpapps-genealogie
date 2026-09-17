package schpapps.genealogie.infrastructure.entrypoints.valueobject;

import java.time.LocalDateTime;
import java.util.List;

public record GedcomParsed(String filename,
        LocalDateTime dateUpload,
        List<IndividuParsed> individuList,
        List<FamilleParsed> familleList,
        List<RapportDiagnostic> rapportDiagnosticList) {
}
