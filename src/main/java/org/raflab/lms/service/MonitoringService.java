package org.raflab.lms.service;

import lombok.AllArgsConstructor;
import org.raflab.lms.controllers.response.StudentStatusResponse;
import org.raflab.lms.controllers.response.TerminMonitoringResponse;
import org.raflab.lms.exceptions.ResourceNotFoundException;
import org.raflab.lms.model.Predaja;
import org.raflab.lms.model.PrijavaZaProveru;
import org.raflab.lms.model.StatusStudenta;
import org.raflab.lms.model.Termin;
import org.raflab.lms.repository.PredajaRepository;
import org.raflab.lms.repository.PrijavaZaProveruRepository;
import org.raflab.lms.repository.TerminRepository;
import org.raflab.lms.utils.Converters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class MonitoringService {
    private final TerminRepository terminRepository;
    private final PrijavaZaProveruRepository prijavaRepository;
    private final PredajaRepository predajaRepository;
    private final DrziPredmetService drziPredmetService;

    @Transactional(readOnly = true)
    public TerminMonitoringResponse zaTermin(Long terminId) {
        Termin termin = terminRepository.findById(terminId)
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji Termin sa id: " + terminId));

        drziPredmetService.proveriPristup(termin);

        List<PrijavaZaProveru> prijave = prijavaRepository.findByGrupaTerminTerminId(terminId);
        Map<Long, Predaja> predajePoStudentu = predajaRepository.findByRadGrupaTerminTerminId(terminId).stream()
                .collect(Collectors.toMap(p -> p.getStudent().getKorisnikId(), p -> p, (a, b) -> a));

        List<StudentStatusResponse> studenti = new ArrayList<>();
        int preuzeto = 0;
        int predato = 0;
        long ukupnoMinuta = 0;

        for (PrijavaZaProveru prijava : prijave) {
            StudentStatusResponse s = new StudentStatusResponse();
            s.setStudent(Converters.toKorisnikResponse(prijava.getStudent()));
            s.setGrupa(prijava.getGrupa().getNazivGrupe());

            Predaja predaja = predajePoStudentu.get(prijava.getStudent().getKorisnikId());
            if (predaja == null) {
                s.setStatus(StatusStudenta.NIJE_PREUZEO);
            } else {
                preuzeto++;
                s.setVremePreuzimanja(predaja.getVremePreuzimanja());
                if (predaja.getVremePredaje() == null) {
                    s.setStatus(StatusStudenta.U_IZRADI);
                } else {
                    predato++;
                    long minuta = Duration.between(predaja.getVremePreuzimanja(), predaja.getVremePredaje()).toMinutes();
                    ukupnoMinuta += minuta;
                    s.setStatus(StatusStudenta.PREDAT);
                    s.setVremePredaje(predaja.getVremePredaje());
                    s.setTrajanjeMinuta(minuta);
                }
            }
            studenti.add(s);
        }

        TerminMonitoringResponse response = new TerminMonitoringResponse();
        response.setTerminId(termin.getTerminId());
        response.setUcionica(termin.getUcionica());
        response.setVremePocetka(termin.getVremePocetka());
        response.setVremeZavrsetka(termin.getVremeZavrsetka());
        response.setUkupnoPrijavljenih(prijave.size());
        response.setUkupnoPreuzeto(preuzeto);
        response.setUkupnoPredato(predato);
        response.setProsecnoVremeIzradeMinuta(predato == 0 ? null : (double) ukupnoMinuta / predato);
        response.setStudenti(studenti);
        return response;
    }
}