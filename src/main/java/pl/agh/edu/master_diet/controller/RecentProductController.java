package pl.agh.edu.master_diet.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.agh.edu.master_diet.core.model.rest.diary.AddRecentProductRequest;
import pl.agh.edu.master_diet.core.model.rest.diary.AddRecentProductResponse;
import pl.agh.edu.master_diet.core.model.shared.RecentProductParameters;
import pl.agh.edu.master_diet.service.RecentProductService;
import pl.agh.edu.master_diet.service.converter.ConversionService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/diary")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RecentProductController {

    private final ConversionService conversionService;
    private final RecentProductService recentProductService;

    @PostMapping("/add")
    public ResponseEntity<AddRecentProductResponse> addRecentProduct(
            @Valid @RequestBody final AddRecentProductRequest request
    ) {
        log.info("Attempt to add recent product");
        final RecentProductParameters recentProductParameters = conversionService.convert(request);
        return ResponseEntity.ok()
                .body(recentProductService.addRecentProduct(recentProductParameters));
    }
}
