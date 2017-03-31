package ru.bulatmukhutdinov.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bulatmukhutdinov.persistance.dao.PhotoRepository;
import ru.bulatmukhutdinov.persistance.model.Photo;

import javax.transaction.Transactional;

/**
 * Created by Reverendo on 31.03.2017.
 */
@Service
@Transactional
public class PhotoServiceImpl implements PhotoService {
    @Autowired
    PhotoRepository photoRepository;

    @Override
    public void save(Photo photo) {
        photoRepository.save(photo);
    }
}
