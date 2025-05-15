package com.mycompany.myapp.web.controller;

import com.mycompany.myapp.domain.Member;
import com.mycompany.myapp.domain.enums.Category;
import com.mycompany.myapp.exception.CustomExceptions;
import com.mycompany.myapp.exception.ResponseMessage;
import com.mycompany.myapp.exception.StatusCode;
import com.mycompany.myapp.service.MapService;
import com.mycompany.myapp.service.MemberService;
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

/**
 * 지도 기반 게시글 조회 관련 REST API 컨트롤러
 * - 지도 화면에 표시할 핀 데이터 조회
 * - 선택된 핀에 해당하는 게시글 상세 목록 조회
 */
@Api(tags = "지도 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/map")
public class MapController extends BaseController {

    private final MapService mapService;
    private final MemberService memberService;

    /**
     * 사용자의 현재 위치와 카테고리를 기반으로 지도에 표시할 핀 정보 조회
     * - 위치 기반 반경 필터링 적용 가능
     *
     * @param category 카테고리 (null이면 전체)
     * @param latitude 사용자의 현재 위도
     * @param longitude 사용자의 현재 경도
     * @return 지도에 표시할 MapPinDto 리스트
     */
    @ApiOperation(value = "카테고리별 지도 조회 API(사용자 위치 기반)")
    @ApiResponse(code = 200, message = "카테고리별 지도 조회 성공")
    @GetMapping("/location")
    public ResponseEntity getMapByCategoryAndLocation(@ApiParam(value = "카테고리. 없으면 전체", example = "FOOD")
                                               @RequestParam(required = false) Category category,

                                           @ApiParam(value = "사용자 위도", example = "37.123456")
                                               @RequestParam(required = false) Double latitude,

                                           @ApiParam(value = "사용자 경도", example = "127.123456")
                                               @RequestParam(required = false) Double longitude){
        try {
            logger.info("Received request: method={}, path={}, description={}", "GET", "/api/map/location", "카테고리별 지도 조회 API");

            List<MapResponseDto.MapPinDto> res = mapService.getPinsByCategoryAndLocation(category, latitude, longitude);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.READ_MAP_SUCCESS, res), HttpStatus.OK);
        } catch (CustomExceptions.testException e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 카테고리를 기반으로 지도에 표시할 핀 정보 조회
     *
     * @param category 카테고리 (null이면 전체)
     * @return 지도에 표시할 MapPinDto 리스트
     */
    @ApiOperation(value = "카테고리별 지도 조회 API")
    @ApiResponse(code = 200, message = "카테고리별 지도 조회 성공")
    @GetMapping
    public ResponseEntity getMapByCategory(@ApiParam(value = "카테고리. 없으면 전체", example = "FOOD")
                                           @RequestParam(required = false) Category category){
        try {
            logger.info("Received request: method={}, path={}, description={}", "GET", "/api/map", "카테고리별 지도 조회 API");

            List<MapResponseDto.MapPinDto> res = mapService.getPinsByCategory(category);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.READ_MAP_SUCCESS, res), HttpStatus.OK);
        } catch (CustomExceptions.testException e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 선택된 핀들의 ID 목록을 기반으로 게시글 정보 조회
     * - 클러스터 클릭 또는 핀 다중 선택 시 사용
     *
     * @param ids 조회할 게시글 ID 리스트
     * @return 각 핀에 해당하는 게시글 목록 (SimplePostDto)
     */
    @ApiOperation(value = "핀 리스트 조회 API")
    @ApiResponse(code = 200, message = "핀 리스트 조회 성공")
    @GetMapping("/pins")
    public ResponseEntity getPins(@RequestParam List<Long> ids){
        try {
            logger.info("Received request: method={}, path={}, description={}", "GET", "/api/map/pins", "핀 리스트 조회 API");

            List<PostResponseDto.SimplePostDto> res = mapService.getPinsByIds(ids);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.READ_PINS_SUCCESS, res), HttpStatus.OK);
        } catch (CustomExceptions.testException e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }
}
