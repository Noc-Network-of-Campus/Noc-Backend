package com.mycompany.myapp.web.controller;

import com.mycompany.myapp.domain.Member;
import com.mycompany.myapp.domain.enums.Category;
import com.mycompany.myapp.domain.enums.SortType;
import com.mycompany.myapp.exception.CustomExceptions;
import com.mycompany.myapp.exception.ResponseMessage;
import com.mycompany.myapp.exception.StatusCode;
import com.mycompany.myapp.repository.MemberRepository;
import com.mycompany.myapp.service.MapService;
import com.mycompany.myapp.service.MemberService;
import com.mycompany.myapp.service.PostService;
import com.mycompany.myapp.web.controller.base.BaseController;
import com.mycompany.myapp.web.dto.MapResponseDto;
import com.mycompany.myapp.web.dto.PostResponseDto;
import com.mycompany.myapp.web.dto.base.DefaultRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "지도 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/map")
public class MapController extends BaseController {

    private final MapService mapService;
    private final MemberService memberService;

    @ApiOperation(value = "카테고리별 지도 조회 API")
    @ApiResponse(code = 200, message = "카테고리별 지도 조회 성공")
    @GetMapping
    public ResponseEntity getMapByCategory(@ApiParam(value = "카테고리. 없으면 전체", example = "FOOD")
                                               @RequestParam(required = false) Category category,

                                           @ApiParam(value = "사용자 위도", example = "37.123456")
                                               @RequestParam(required = false) Double latitude,

                                           @ApiParam(value = "사용자 경도", example = "127.123456")
                                               @RequestParam(required = false) Double longitude){
        try {
            logger.info("Received request: method={}, path={}, description={}", "GET", "/api/map", "카테고리별 지도 조회 API");

            Member member = memberService.getCurrentMember();

            List<MapResponseDto.MapPinDto> res = mapService.getPinsByCategoryAndLocation(category, latitude, longitude);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.READ_MAP_SUCCESS, res), HttpStatus.OK);
        } catch (CustomExceptions.testException e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "핀 리스트 조회 API")
    @ApiResponse(code = 200, message = "핀 리스트 조회 성공")
    @GetMapping("/pins")
    public ResponseEntity getPins(@RequestParam List<Long> ids){
        try {
            logger.info("Received request: method={}, path={}, description={}", "GET", "/api/map/pins", "핀 리스트 조회 API");

            Member member = memberService.getCurrentMember();

            List<PostResponseDto.SimplePostDto> res = mapService.getPinsByIds(ids);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.READ_PINS_SUCCESS, res), HttpStatus.OK);
        } catch (CustomExceptions.testException e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }
}
