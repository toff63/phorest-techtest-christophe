package com.marchal.christophe.phoresttechtest.migration.api;

import com.marchal.christophe.phoresttechtest.exception.ImportFileException;
import com.marchal.christophe.phoresttechtest.migration.ImportService;
import com.marchal.christophe.phoresttechtest.migration.ImportStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/import")
public class ImportController {

    private final ImportService service;
    private final ServiceResponseConverter converter;

    public ImportController(ImportService service, ServiceResponseConverter converter) {
        this.service = service;
        this.converter = converter;
    }


    @PostMapping("/salon")
    public ImportResponse importSalon(
            @RequestParam("clients") MultipartFile clientsFile,
            @RequestParam("appointments") MultipartFile appointmentsFile,
            @RequestParam("purchases") MultipartFile purchasesFile,
            @RequestParam("services") MultipartFile servicesFile) {

        ImportStatus serviceResponse = service.importSalon(openFile(clientsFile), openFile(appointmentsFile), openFile(purchasesFile), openFile(servicesFile));
        return converter.toImportResponse(serviceResponse);
    }

    private InputStream openFile(MultipartFile file) {
        try {
            return file.getInputStream();
        } catch (IOException e) {
            throw new ImportFileException("Failed to open " + file.getName(), e);
        }
    }
}
