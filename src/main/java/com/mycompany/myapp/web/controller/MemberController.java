package com.mycompany.myapp.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.myapp.domain.Member;
import com.mycompany.myapp.exception.ResponseMessage;
import com.mycompany.myapp.exception.StatusCode;
import com.mycompany.myapp.service.MemberService;
import com.mycompany.myapp.web.controller.base.BaseController;
import com.mycompany.myapp.web.dto.MemberRequestDto;
import com.mycompany.myapp.web.dto.MemberResponseDto;
import com.mycompany.myapp.web.dto.base.DefaultRes;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Api(tags = "사용자 관련 API")
@RequestMapping("/api/members")
public class MemberController extends BaseController {
	private final MemberService memberService;

	//사용자 닉네임 생성
	@ApiOperation(value = "사용자 닉네임 생성 API")
	@ApiResponse(code = 200, message = "사용자 닉네임 생성 성공")
	@PostMapping("/nickname")
	public ResponseEntity createNickname() {
		try {
			logger.info("Received request: method={}, path={}, description={}", "POST", "/api/members/nickname",
				"사용자 닉네임 생성 API");
			String nickname = memberService.createNickname();
			return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.CREATE_NICKNAME_SUCCESS, nickname), HttpStatus.OK);
		} catch (Exception e) {
			return handleApiException(e, HttpStatus.BAD_REQUEST);
		}
	}

	//사용자 프로필 설정
	@ApiOperation(value = "사용자 프로필 설정 API")
	@ApiResponse(code = 200, message = "사용자 프로필 설정 성공")
	@PostMapping("/profile")
	public ResponseEntity setProfile(@RequestBody MemberRequestDto.CreateProfileDto request) {
		try {
			logger.info("Received request: method={}, path={}, description={}", "POST", "/api/members/profile",
				"사용자 프로필 설정 API");
			Member currentMember = memberService.getCurrentMember();
			memberService.createProfile(currentMember, request);
			return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.SET_PROFILE_SUCCESS), HttpStatus.OK);
		} catch (Exception e) {
			return handleApiException(e, HttpStatus.BAD_REQUEST);
		}
	}

	//사용자 프로필 조회
	@ApiOperation(value = "사용자 프로필 조회 API")
	@ApiResponse(code = 200, message = "사용자 프로필 조회 성공")
	@GetMapping("/profile/view")
	public ResponseEntity getProfile() {
		try {
			logger.info("Received request: method={}, path={}, description={}", "POST", "/api/members/profile/view",
				"사용자 프로필 조회 API");
			Member currentMember = memberService.getCurrentMember();
			MemberResponseDto.ProfileDto res = memberService.getProfile(currentMember);
			return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.GET_PROFILE_SUCCESS, res), HttpStatus.OK);
		} catch (Exception e) {
			return handleApiException(e, HttpStatus.BAD_REQUEST);
		}
	}

	//사용자 회원 탈퇴
	@ApiOperation(value = "사용자 회원 탈퇴 API")
	@ApiResponse(code = 200, message = "사용자 회원 탈퇴 성공")
	@DeleteMapping("/withdraw")
	public ResponseEntity withdraw() {
		try {
			logger.info("Received request: method={}, path={}, description={}", "POST", "/api/members/withdraw",
				"사용자 회원 탈퇴 API");
			Member currentMember = memberService.getCurrentMember();
			memberService.withdraw(currentMember);
			return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.WITHDRAW_SUCCESS), HttpStatus.OK);
		} catch (Exception e) {
			return handleApiException(e, HttpStatus.BAD_REQUEST);
		}
	}
}
