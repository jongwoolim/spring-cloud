package me.jongwoo.catalogservice.service;

import me.jongwoo.catalogservice.domain.Catalog;

import java.util.List;

public interface CatalogService {

    List<Catalog> getAllCatalogs();
}
