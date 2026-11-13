package org.raflab.lms.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.raflab.lms.controllers.request.PredajaFilter;
import org.raflab.lms.controllers.request.PreuzmiRequest;
import org.raflab.lms.controllers.response.PredajaResponse;
import org.raflab.lms.service.PredajaService;
import org.raflab.lms.service.PristupService;
import org.raflab.lms.utils.Converters;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/predaja")
@AllArgsConstructor
public class PredajaController {
    final PredajaService predajaService;
    final PristupService pristupService;

    // 1. korak: student unosi termin i grupu, dobija predaju (id + vreme preuzimanja)
    @PostMapping("/preuzmi")
    public PredajaResponse preuzmi(@RequestBody @Valid PreuzmiRequest request) {
        Long studentId = pristupService.ulogovaniKorisnikId();
        return Converters.toPredajaResponse(
                predajaService.preuzmi(studentId, request.getTerminId(), request.getGrupaId()));
    }

    // 2. korak: preuzimanje zip-a sa zadatkom
    @GetMapping("/{predajaId}/zadatak")
    public ResponseEntity<Resource> zadatak(@PathVariable Long predajaId) {
        Long studentId = pristupService.ulogovaniKorisnikId();
        Resource zip = predajaService.zadatakZaPreuzimanje(predajaId, studentId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/zip"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename("zadatak.zip").build().toString())
                .body(zip);
    }

    // predaja resenja: multipart, polje "fajl" je zip projekta; odgovor je potvrda sa vremenom predaje
    @PostMapping(value = "/{predajaId}/predaj", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PredajaResponse predaj(@PathVariable Long predajaId, @RequestParam("fajl") MultipartFile fajl) {
        Long studentId = pristupService.ulogovaniKorisnikId();
        return Converters.toPredajaResponse(predajaService.predaj(predajaId, studentId, fajl));
    }

    @GetMapping("/moje-predaje")
    public List<PredajaResponse> getMojePredaje() {
        Long korisnikId = pristupService.ulogovaniKorisnikId();
        return Converters.toList(predajaService.findByStudent(korisnikId), Converters::toPredajaResponse);
    }

    // nastavnik

    @GetMapping("/{id}")
    public PredajaResponse getById(@PathVariable Long id) {
        return Converters.toPredajaResponse(predajaService.findZaNastavnika(id));
    }

    @GetMapping("/rad/{radId}")
    public List<PredajaResponse> getByRad(@PathVariable Long radId) {
        return Converters.toList(predajaService.findByRad(radId), Converters::toPredajaResponse);
    }

    // npr. /api/predaja/pretraga?terminId=3&ucionica=A1  (svi parametri su opcioni)
    @GetMapping("/pretraga")
    public List<PredajaResponse> pretraga(PredajaFilter filter) {
        return Converters.toList(predajaService.pretrazi(filter), Converters::toPredajaResponse);
    }

    // isti filteri, ali vraca zip sa resenjima
    @GetMapping("/resenja/zip")
    public ResponseEntity<byte[]> resenjaZip(PredajaFilter filter) {
        byte[] zip = predajaService.spakujResenja(filter);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/zip"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename("resenja.zip").build().toString())
                .body(zip);
    }

    @PutMapping("/{predajaId}/poeni")
    public PredajaResponse dodeliPoene(@PathVariable Long predajaId, @RequestParam Integer poeni) {
        return Converters.toPredajaResponse(predajaService.dodeliPoene(predajaId, poeni));
    }

    @GetMapping("/{predajaId}/resenje")
    public ResponseEntity<Resource> resenje(@PathVariable Long predajaId) {
        Resource zip = predajaService.resenjeZaNastavnika(predajaId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/zip"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename("resenje-" + predajaId + ".zip").build().toString())
                .body(zip);
    }
}