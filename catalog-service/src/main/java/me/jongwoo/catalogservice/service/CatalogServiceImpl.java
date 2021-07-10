package me.jongwoo.catalogservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jongwoo.catalogservice.domain.Catalog;
import me.jongwoo.catalogservice.repository.CatalogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CatalogServiceImpl implements CatalogService{

    private final CatalogRepository catalogRepository;

    @Override
    public List<Catalog> getAllCatalogs() {

        final List<Catalog> catalogList = catalogRepository.findAll();
        return catalogList;
    }
}
