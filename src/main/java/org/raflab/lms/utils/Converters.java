package org.raflab.lms.utils;

import org.raflab.lms.controllers.request.*;
import org.raflab.lms.controllers.response.*;
import org.raflab.lms.model.*;
import org.raflab.lms.service.AuthService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Converters {
    public static <E, R> List<R> toList(Iterable<E> entities, Function<E, R> converter) {
        List<R> rez = new ArrayList<>();
        entities.forEach(e -> rez.add(converter.apply(e)));
        return rez;
    }

    // Prijava
    public static LoginResponse toLoginResponse(AuthService.Prijava prijava) {
        return new LoginResponse(prijava.token(), prijava.istice(), prijava.uloga());
    }

    // Korisnik
    public static Korisnik toKorisnik(KorisnikRequest request) {
        Korisnik korisnik = new Korisnik();
        korisnik.setIme(request.getIme());
        korisnik.setPrezime(request.getPrezime());
        korisnik.setBrojIndeksa(request.getBrojIndeksa());
        korisnik.setUloga(request.getUloga());
        korisnik.setEmail(request.getEmail());
        return korisnik;
    }

    public static KorisnikResponse toKorisnikResponse(Korisnik korisnik) {
        if (korisnik == null) return null;
        KorisnikResponse response = new KorisnikResponse();
        response.setKorisnikId(korisnik.getKorisnikId());
        response.setIme(korisnik.getIme());
        response.setPrezime(korisnik.getPrezime());
        response.setBrojIndeksa(korisnik.getBrojIndeksa());
        response.setUloga(korisnik.getUloga());
        response.setEmail(korisnik.getEmail());
        return response;
    }

    // Predmet
    public static Predmet toPredmet(PredmetRequest request) {
        Predmet predmet = new Predmet();
        predmet.setNazivPredmeta(request.getNazivPredmeta());
        predmet.setKodPredmeta(request.getKodPredmeta());
        return predmet;
    }

    public static PredmetResponse toPredmetResponse(Predmet predmet) {
        if (predmet == null) return null;
        PredmetResponse response = new PredmetResponse();
        response.setPredmetId(predmet.getPredmetId());
        response.setNazivPredmeta(predmet.getNazivPredmeta());
        response.setKodPredmeta(predmet.getKodPredmeta());
        return response;
    }

    // Rok
    public static Rok toRok(RokRequest request) {
        Rok rok = new Rok();
        rok.setNazivRoka(request.getNazivRoka());
        rok.setGodina(request.getGodina());
        return rok;
    }

    public static RokResponse toRokResponse(Rok rok) {
        if (rok == null) return null;
        RokResponse response = new RokResponse();
        response.setRokId(rok.getRokId());
        response.setNazivRoka(rok.getNazivRoka());
        response.setGodina(rok.getGodina());
        return response;
    }

    // Provera
    public static Provera toProvera(ProveraRequest request) {
        Provera provera = new Provera();
        provera.setTipProvere(request.getTipProvere());
        return provera;
    }

    public static ProveraResponse toProveraResponse(Provera provera) {
        if (provera == null) return null;
        ProveraResponse response = new ProveraResponse();
        response.setProveraId(provera.getProveraId());
        response.setTipProvere(provera.getTipProvere());
        response.setPredmet(toPredmetResponse(provera.getPredmet()));
        response.setRok(toRokResponse(provera.getRok()));
        return response;
    }

    // Termin
    public static Termin toTermin(TerminRequest request) {
        Termin termin = new Termin();
        termin.setUcionica(request.getUcionica());
        termin.setVremePocetka(request.getVremePocetka());
        termin.setVremeZavrsetka(request.getVremeZavrsetka());
        return termin;
    }

    public static TerminResponse toTerminResponse(Termin termin) {
        if (termin == null) return null;
        TerminResponse response = new TerminResponse();
        response.setTerminId(termin.getTerminId());
        response.setUcionica(termin.getUcionica());
        response.setVremePocetka(termin.getVremePocetka());
        response.setVremeZavrsetka(termin.getVremeZavrsetka());
        response.setProvera(toProveraResponse(termin.getProvera()));
        return response;
    }

    // Grupa
    public static Grupa toGrupa(GrupaRequest request) {
        Grupa grupa = new Grupa();
        grupa.setNazivGrupe(request.getNazivGrupe());
        return grupa;
    }

    public static GrupaResponse toGrupaResponse(Grupa grupa) {
        if (grupa == null) return null;
        GrupaResponse response = new GrupaResponse();
        response.setGrupaId(grupa.getGrupaId());
        response.setNazivGrupe(grupa.getNazivGrupe());
        response.setTermin(toTerminResponse(grupa.getTermin()));
        return response;
    }

    // StudentskiRad
    public static StudentskiRad toStudentskiRad(StudentskiRadRequest request) {
        StudentskiRad studentskiRad = new StudentskiRad();
        studentskiRad.setNazivZadatka(request.getNazivZadatka());
        return studentskiRad;
    }

    public static StudentskiRadResponse toStudentskiRadResponse(StudentskiRad rad) {
        if (rad == null) return null;
        StudentskiRadResponse response = new StudentskiRadResponse();
        response.setStudentskiRadId(rad.getStudentskiRadId());
        response.setNazivZadatka(rad.getNazivZadatka());
        response.setGrupa(toGrupaResponse(rad.getGrupa()));
        return response;
    }

    // PrijavaZaProveru
    public static PrijavaZaProveruResponse toPrijavaZaProveruResponse(PrijavaZaProveru prijava) {
        if (prijava == null) return null;
        PrijavaZaProveruResponse response = new PrijavaZaProveruResponse();
        response.setPrijavaId(prijava.getPrijavaId());
        response.setStudent(toKorisnikResponse(prijava.getStudent()));
        response.setProvera(toProveraResponse(prijava.getProvera()));
        response.setGrupa(toGrupaResponse(prijava.getGrupa()));
        return response;
    }

    // Predaja
    public static PredajaResponse toPredajaResponse(Predaja predaja) {
        if (predaja == null) return null;
        PredajaResponse response = new PredajaResponse();
        response.setPredajaId(predaja.getPredajaId());
        response.setStudent(toKorisnikResponse(predaja.getStudent()));
        response.setRad(toStudentskiRadResponse(predaja.getRad()));
        response.setPoeni(predaja.getPoeni());
        response.setVremePreuzimanja(predaja.getVremePreuzimanja());
        response.setVremePredaje(predaja.getVremePredaje());
        return response;
    }

    // Komentar
    public static KomentarResponse toKomentarResponse(Komentar komentar) {
        if (komentar == null) return null;
        KomentarResponse response = new KomentarResponse();
        response.setKomentarId(komentar.getKomentarId());
        response.setPredaja(toPredajaResponse(komentar.getPredaja()));
        response.setNazivFajla(komentar.getNazivFajla());
        response.setLinijaOd(komentar.getLinijaOd());
        response.setLinijaDo(komentar.getLinijaDo());
        response.setTekst(komentar.getTekst());
        response.setNastavnik(toKorisnikResponse(komentar.getNastavnik()));
        return response;
    }

    // DrziPredmet
    public static DrziPredmetResponse toDrziPredmetResponse(DrziPredmet drziPredmet) {
        if (drziPredmet == null) return null;
        DrziPredmetResponse response = new DrziPredmetResponse();
        response.setDrziPredmetId(drziPredmet.getDrziPredmetId());
        response.setPredmet(toPredmetResponse(drziPredmet.getPredmet()));
        response.setNastavnik(toKorisnikResponse(drziPredmet.getNastavnik()));
        return response;
    }
}