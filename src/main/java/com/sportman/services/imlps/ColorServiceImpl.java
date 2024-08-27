package com.sportman.services.imlps;

import com.sportman.dto.request.ColorCreateRequest;
import com.sportman.dto.response.ColorCreateResponse;
import com.sportman.dto.response.page.ColorPageResponse;
import com.sportman.entities.Color;
import com.sportman.exceptions.AppException;
import com.sportman.exceptions.ErrorCode;
import com.sportman.mappers.ColorMapper;
import com.sportman.repositories.ColorRepository;
import com.sportman.services.interfaces.ColorService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ColorServiceImpl implements ColorService {

    ColorRepository colorRepository;
    ColorMapper colorMapper;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ColorCreateResponse create(ColorCreateRequest request) {

        //check color exist
        if (colorRepository.existsById(request.getColorHex())) throw new AppException(ErrorCode.COLOR_EXISTED);

        //create new color
        Color newColor = colorMapper.toColor(request);
//        newColor.setColorHex(newColor.getColorHex());
        newColor.setIsDeleted(false);

        return colorMapper.toColorResponse(colorRepository.save(newColor));
    }

    @Override
    public ColorPageResponse get(Pageable pageable) {

        List<ColorCreateResponse> colors = colorRepository.findAllByIsDeletedFalse(pageable)
                .stream()
                .map(color -> colorMapper.toColorResponse(color))
                .toList();

        return ColorPageResponse
                .builder()
                .totalPage(Math.ceilDiv(colorRepository.count(), pageable.getPageSize()))
                .totalElements(colorRepository.count())
                .currentPage(pageable.getPageNumber() + 1)
                .colors(colors)
                .build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(String colorHex) {

        //check color exist
        Color color = colorRepository.findById(colorHex).orElseThrow(() -> new AppException(ErrorCode.COLOR_NOT_FOUND));

        color.setIsDeleted(true);

        colorRepository.save(color);

    }
}
