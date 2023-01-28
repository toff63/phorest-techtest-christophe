package com.marchal.christophe.phoresttechtest.migration;

import com.marchal.christophe.phoresttechtest.salon.AppointmentRepository;
import com.marchal.christophe.phoresttechtest.salon.ClientRepository;
import com.marchal.christophe.phoresttechtest.salon.ProductRepository;
import com.marchal.christophe.phoresttechtest.salon.ServiceRepository;
import org.springframework.stereotype.Service;

@Service
public class ImportService {
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final ServiceRepository serviceRepository;
    private final AppointmentRepository appointmentRepository;

    public ImportService(ClientRepository clientRepository,
                         ProductRepository productRepository,
                         ServiceRepository serviceRepository,
                         AppointmentRepository appointmentRepository) {
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.serviceRepository = serviceRepository;
        this.appointmentRepository = appointmentRepository;
    }

    
}
