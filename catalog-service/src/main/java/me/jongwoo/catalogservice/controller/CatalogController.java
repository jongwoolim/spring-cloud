package me.jongwoo.catalogservice.controller;

import lombok.RequiredArgsConstructor;
import me.jongwoo.catalogservice.domain.Catalog;
import me.jongwoo.catalogservice.service.CatalogService;
import me.jongwoo.catalogservice.vo.ResponseCatalog;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/catalog-service")
@RequiredArgsConstructor
public class CatalogController {

    private final Environment env;
    private final CatalogService catalogService;
    private final ModelMapper modelMapper;

    @GetMapping("/catalogs")
    public List<ResponseCatalog> getUsers(){
        final List<Catalog> catalogList = catalogService.getAllCatalogs();
        List<ResponseCatalog> result = new ArrayList<>();
        catalogList.forEach(e -> {
            final ResponseCatalog resCatalog = modelMapper.map(e, ResponseCatalog.class);
            result.add(resCatalog);
        });
        catalogList.forEach(System.out::println);
        return result;
    }

    @GetMapping("/health_check")
    public String status(){
        return String.format("It`s Working in Catalog Service POrT %s", env.getProperty("local.server.port"));
    }


}
