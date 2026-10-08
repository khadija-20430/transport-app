package transport.core;

import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class ServiceReclamation {
    private final Map<TypeReclamation, TreeSet<Reclamation>> reclamationsParType = new EnumMap<>(TypeReclamation.class);
    private final Map<Personne, TreeSet<Reclamation>> reclamationsParPersonne = new HashMap<>();
    private final Map<Suspendable, TreeSet<Reclamation>> reclamationsParSuspendable = new HashMap<>();
    private final Map<Suspendable, TreeSet<ReclamationTechnique>> reclamationsTechniquesParCible = new HashMap<>();

    public ServiceReclamation() {
        Arrays.stream(TypeReclamation.values())
                .forEach(type -> reclamationsParType.put(type, new TreeSet<>(Comparator.comparingInt(Reclamation::getNumero))));
    }

    public void soumettre(Reclamation reclamation) {
        reclamationsParType.get(reclamation.getType()).add(reclamation);
        reclamationsParPersonne.computeIfAbsent(reclamation.getPersonne(),
                k -> new TreeSet<>(Comparator.comparingInt(Reclamation::getNumero))).add(reclamation);


        if (reclamation instanceof ReclamationTechnique rt && rt.getCible() != null) {
            reclamationsTechniquesParCible.computeIfAbsent(rt.getCible(),
                    k -> new TreeSet<>(Comparator.comparingInt(Reclamation::getNumero))).add(rt);
            verifierSuspensionTechnique(rt.getCible());
        }
    }

    private final Set<Suspendable> ciblesSuspendues = new HashSet<>();

    public void verifierSuspensionTechnique(Suspendable cible) {
        long nbReclamationsGraves = reclamationsTechniquesParCible.getOrDefault(cible, new TreeSet<>())
                .stream()
                .filter(rt -> rt.getGravite() == NiveauGravite.FORT)
                .filter(rt -> ChronoUnit.HOURS.between(rt.getDate().atStartOfDay(), LocalDateTime.now()) <= 24)
                .filter(rt -> !rt.estResolue())
                .count();

        if (nbReclamationsGraves >= 3) {
            if (!cible.estSuspendu()) {
                cible.suspendre();
                ciblesSuspendues.add(cible);
            }
        } else {
            if (cible.estSuspendu()) {
                cible.reactiver();
                ciblesSuspendues.remove(cible);
            }
        }
    }

    public void resoudre(Reclamation reclamation) {
        reclamation.resoudre();
        if (reclamation instanceof ReclamationTechnique rt) {
            verifierSuspensionTechnique(rt.getCible());
        }
    }

    public List<Reclamation> getReclamationsParType(TypeReclamation type) {
        return new ArrayList<>(reclamationsParType.getOrDefault(type, new TreeSet<>()));
    }

    public List<Reclamation> getAllReclamations() {
        return reclamationsParType.values()
                .stream()
                .flatMap(Set::stream)
                .collect(Collectors.toList());
    }

}
