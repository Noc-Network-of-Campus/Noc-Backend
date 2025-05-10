package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.enums.Category;
import com.mycompany.myapp.web.dto.MapResponseDto;
import com.mycompany.myapp.web.dto.PostResponseDto;

import java.util.List;

/**
 * 지도(Map) 기능 관련 서비스를 정의하는 인터페이스
 * - 지도 핀 조회, 거리 기반 필터링, ID 기반 게시글 조회 등을 지원
 */
public interface MapService {

    /**
     * 특정 카테고리와 현재 위치(위도/경도)를 기반으로 주변에 표시할 지도 핀 정보 조회
     *
     * @param category 카테고리 (null이면 전체)
     * @param latitude 현재 위도
     * @param longitude 현재 경도
     * @return MapPinDto 리스트 (지도에 표시할 핀 정보)
     */
    List<MapResponseDto.MapPinDto> getPinsByCategoryAndLocation(Category category, Double latitude, Double longitude);

    /**
     * 선택된 핀 ID 목록을 기반으로 게시글 목록을 조회
     * (예: 클러스터 클릭 시 상세 목록 요청)
     *
     * @param ids 게시글 ID 목록
     * @return SimplePostDto 리스트 (핀에 해당하는 게시글 목록)
     */
    List<PostResponseDto.SimplePostDto> getPinsByIds(List<Long> ids);
}
