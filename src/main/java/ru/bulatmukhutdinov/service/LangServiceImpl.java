package ru.bulatmukhutdinov.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bulatmukhutdinov.persistance.dao.LangRepository;
import ru.bulatmukhutdinov.persistance.model.Lang;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class LangServiceImpl implements LangService {

    @Autowired
    private LangRepository langRepository;

    @Override
    public List<Lang> getLangs() {
        return langRepository.findAll();

    }
}