package com.mycompany.myapp.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.myapp.domain.Member;
import com.mycompany.myapp.exception.StatusCode;
import com.mycompany.myapp.service.AuthService;
import com.mycompany.myapp.service.MemberService;
import com.mycompany.myapp.web.controller.base.BaseController;
import com.mycompany.myapp.web.dto.AuthRequestDto;
import com.mycompany.myapp.web.dto.AuthResponseDto;
import com.mycompany.myapp.web.dto.base.DefaultRes;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@Api(tags = "로그인 관련 API")
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController extends BaseController {
	private final AuthService authService;
	private final MemberService memberService;

	@ApiOperation(
		value = "구글 OAuth2 로그인 URL 안내",
		notes = "구글 소셜 로그인을 위해 아래 URL로 리디렉트 해주세요.\n" +
			"프론트엔드는 http://localhost:8080/oauth2/authorization/google 로 이동시켜 로그인.\n" +
			"로그인 성공 시, 백엔드가 토큰 발급 후 지정된 리디렉트 URI로 리디렉션.\n" +
			"임의로 http://localhost:3000/oauth2/redirect 로 설정함\n"
	)
	@GetMapping("/login/google")
	public ResponseEntity<DefaultRes<String>> getGoogleLoginUrl() {
		String redirectUrl = "/oauth2/authorization/google"; // Spring Security 기본 로그인 시작 URL
		return ResponseEntity.ok(DefaultRes.res(StatusCode.OK, "구글 로그인 URL", redirectUrl));
	}

	@ApiOperation(value = "토큰 재발급 API")
	@ApiResponse(code = 200, message = "토큰 재발급 성공")
	@PostMapping("/reissue")
	public ResponseEntity reissueToken(@RequestBody AuthRequestDto.ReissueDto request) {
		try {
			logger.info("Received request: method={}, path={}, description={}", "POST", "/api/auth/reissue", "토큰 재발급 API");
			AuthResponseDto.ReissueDto res = authService.reissue(request);

			return new ResponseEntity(DefaultRes.res(StatusCode.OK, "토큰 재발급 성공", res), HttpStatus.OK);
		} catch (Exception e) {
			return handleApiException(e, HttpStatus.BAD_REQUEST);
		}
	}
}
