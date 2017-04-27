package ru.bulatmukhutdinov.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.bulatmukhutdinov.persistance.dao.ServiceRepository;
import ru.bulatmukhutdinov.persistance.model.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by neewy on 06/04/17.
 */
@org.springframework.stereotype.Service
@Transactional
public class ServiceServiceImpl implements ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Override
    public List<Service> findAll() {
        return serviceRepository.findAll();
    }

    @Override
    public void save(Service service) {
        serviceRepository.save(service);
    }

    @Override
    public void delete(Service service) {
        serviceRepository.delete(service);
    }
}
