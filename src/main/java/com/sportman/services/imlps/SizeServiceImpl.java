package com.sportman.services.imlps;

import com.sportman.dto.request.SizeCreateRequest;
import com.sportman.dto.response.SizeCreateResponse;
import com.sportman.dto.response.page.SizePageResponse;
import com.sportman.entities.Size;
import com.sportman.exceptions.AppException;
import com.sportman.exceptions.ErrorCode;
import com.sportman.mappers.SizeMapper;
import com.sportman.repositories.SizeRepository;
import com.sportman.services.interfaces.SizeService;
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
public class SizeServiceImpl implements SizeService {

    SizeRepository sizeRepository;
    SizeMapper sizeMapper;

    /**
     * @Author Lien Chi khang
     */
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public SizeCreateResponse create(SizeCreateRequest request) {

        //check size exist
        if (sizeRepository.existsById(request.getSizeTag())) throw new AppException(ErrorCode.SIZE_EXISTED);

        //create new size
        Size size = sizeMapper.toSize(request);
        size.setIsDeleted(false);
        size.setSizeTag(size.getSizeTag().toUpperCase());

        return sizeMapper.toSizeResponse(sizeRepository.save(size));
    }

    @Override
    public SizePageResponse get(Pageable pageable) {

        List<SizeCreateResponse> sizes = sizeRepository
                .findAllByIsDeletedFalse(pageable).stream().map(sizeMapper::toSizeResponse).toList();

        return SizePageResponse
                .builder()
                .sizes(sizes)
                .currentPage(pageable.getPageNumber() + 1)
                .totalElements(sizeRepository.count())
                .totalPage(Math.ceilDiv(sizeRepository.count(), pageable.getPageSize()))
                .build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(String sizeId) {
        //check size exist
        Size size = sizeRepository.findById(sizeId.toUpperCase()).orElseThrow(() -> new AppException(ErrorCode.SIZE_NOT_FOUND));
        size.setIsDeleted(true);
        sizeRepository.save(size);
    }
}
