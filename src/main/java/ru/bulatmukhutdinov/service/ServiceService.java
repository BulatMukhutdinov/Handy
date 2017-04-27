package ru.bulatmukhutdinov.service;

import ru.bulatmukhutdinov.persistance.model.Service;

import java.util.List;

/**
 * Created by neewy on 06/04/17.
 */
public interface ServiceService {

    List<Service> findAll();

    void save(Service service);

    void delete(Service service);

}
