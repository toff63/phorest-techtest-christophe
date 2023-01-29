package com.marchal.christophe.phoresttechtest.migration;

import com.marchal.christophe.phoresttechtest.exception.ImportFileException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/import")
public class ImportController {

    private final ImportService service;

    public ImportController(ImportService service) {
        this.service = service;
    }

    @PostMapping("/client")
    public String importClients(@RequestParam("file") MultipartFile file,
                                RedirectAttributes redirectAttributes) {

        try {
            service.importClientCsv(file.getInputStream());
        } catch (IOException e) {
            throw new ImportFileException("Failed to import Client CSV file", e);
        }
        return "redirect:/";
    }

    @PostMapping("/appointment")
    public String importAppointments(@RequestParam("file") MultipartFile file,
                                     RedirectAttributes redirectAttributes) {

        try {
            service.importAppointmentCsv(file.getInputStream());
        } catch (IOException e) {
            throw new ImportFileException("Failed to import Client CSV file", e);
        }
        return "redirect:/";
    }

    @PostMapping("/purchase")
    public String importPurchases(@RequestParam("file") MultipartFile file,
                                  RedirectAttributes redirectAttributes) {
        try {
            service.importPurchaseCsv(file.getInputStream());
        } catch (IOException e) {
            throw new ImportFileException("Failed to import Client CSV file", e);
        }

        return "redirect:/";
    }

    @PostMapping("/service")
    public String importServices(@RequestParam("file") MultipartFile file,
                                 RedirectAttributes redirectAttributes) {
        try {
            service.importServiceCsv(file.getInputStream());
        } catch (IOException e) {
            throw new ImportFileException("Failed to import Client CSV file", e);
        }

        return "redirect:/";
    }
}
