package org.raflab.lms.service;

import org.raflab.lms.exceptions.BadRequestException;
import org.raflab.lms.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

// Fajlovi: storage/zadaci/{radId}/projekat.zip i storage/predaje/{predajaId}/projekat.zip.
// Ime fajla sa klijenta se nikad ne koristi u putanji (zastita od path traversal-a).

@Service
public class FileStorageService {

    private static final String NAZIV_FAJLA = "projekat.zip";

    private final Path root;

    public FileStorageService(@Value("${app.storage.root:./storage}") String rootPath) {
        this.root = Paths.get(rootPath).toAbsolutePath().normalize();
    }

    // vraca relativnu putanju koja se upisuje u bazu
    public String sacuvajZadatak(Long radId, MultipartFile fajl) {
        return sacuvaj("zadaci", radId, fajl);
    }

    public String sacuvajPredaju(Long predajaId, MultipartFile fajl) {
        return sacuvaj("predaje", predajaId, fajl);
    }

    public Resource ucitaj(String relativnaPutanja) {
        return new FileSystemResource(nadji(relativnaPutanja));
    }

    // imeUArhivi -> relativna putanja fajla; vraca jedan zip sa svim fajlovima
    public byte[] spakuj(Map<String, String> fajlovi) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (ZipOutputStream zip = new ZipOutputStream(out)) {
            for (Map.Entry<String, String> e : fajlovi.entrySet()) {
                zip.putNextEntry(new ZipEntry(e.getKey()));
                Files.copy(nadji(e.getValue()), zip);
                zip.closeEntry();
            }
        } catch (IOException ex) {
            throw new UncheckedIOException("Pakovanje fajlova nije uspelo.", ex);
        }
        return out.toByteArray();
    }

    // pise se u privremeni fajl pa premesta, da prekinut upload ne unisti prethodno validno resenje
    private String sacuvaj(String podfolder, Long id, MultipartFile fajl) {
        proveriZip(fajl);
        Path dir = root.resolve(podfolder).resolve(String.valueOf(id));
        Path cilj = dir.resolve(NAZIV_FAJLA);
        Path privremeni = null;
        try {
            Files.createDirectories(dir);
            privremeni = Files.createTempFile(dir, "upload-", ".tmp");
            try (InputStream in = fajl.getInputStream()) {
                Files.copy(in, privremeni, StandardCopyOption.REPLACE_EXISTING);
            }
            premesti(privremeni, cilj);
        } catch (IOException e) {
            throw new UncheckedIOException("Cuvanje fajla nije uspelo", e);
        } finally {
            if (privremeni != null) {
                try {
                    Files.deleteIfExists(privremeni); // ostaje samo ako nesto pukne pre premestanja
                } catch (IOException ignored) {
                }
            }
        }
        return root.relativize(cilj).toString().replace('\\', '/');
    }

    private void premesti(Path izvor, Path cilj) throws IOException {
        try {
            Files.move(izvor, cilj, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(izvor, cilj, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private Path nadji(String relativnaPutanja) {
        if (relativnaPutanja == null) {
            throw new ResourceNotFoundException("Fajl ne postoji.");
        }
        Path putanja = root.resolve(relativnaPutanja).normalize();
        if (!putanja.startsWith(root) || !Files.isRegularFile(putanja)) {
            throw new ResourceNotFoundException("Fajl ne postoji.");
        }
        return putanja;
    }

    // Provera da li je fajl stvarno ZIP (provera 'P' i 'K' bajtova)
    private void proveriZip(MultipartFile fajl) {
        if (fajl == null || fajl.isEmpty()) {
            throw new BadRequestException("Fajl je obavezan.");
        }
        String naziv = fajl.getOriginalFilename();
        if (naziv == null || !naziv.toLowerCase().endsWith(".zip")) {
            throw new BadRequestException("Dozvoljen je samo .zip fajl.");
        }

        try (InputStream in = fajl.getInputStream()) {
            byte[] zaglavlje = in.readNBytes(2);
            if (zaglavlje.length < 2 || zaglavlje[0] != 'P' || zaglavlje[1] != 'K') {
                throw new BadRequestException("Fajl nije ispravan zip.");
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Citanje fajla nije uspelo.", e);
        }
    }
}