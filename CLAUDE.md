# utils — hisondev Java 유틸리티 라이브러리

hisondev 생태계의 Java 유틸 artifact. 문자열/날짜/숫자/프로퍼티 처리용 정적 메서드 모음.
hisonjv 통합 artifact에 포함되며, 단독 사용도 가능 (`io.github.hisondev:utils`).

## 구조 (중첩 디렉토리 주의)

```
jv/utils/               ← git 저장소 루트 (README, LICENSE)
└─ utils/               ← 실제 Maven 프로젝트 루트 (pom.xml)
   └─ src/main/java/io/github/hison/utils/
      ├─ Utils.java           ← 유일한 기능 클래스 (약 3,000줄, 전부 public static)
      └─ UtilsException.java  ← RuntimeException 상속 커스텀 예외
```

## 핵심 사실

- **Maven**: groupId `io.github.hisondev`, artifactId `utils`, v2.0.0 / **Java 21** / jakarta.servlet-api 6.0.0(provided, getClientIpAddress에서만 사용) / MIT
- **패키지는 `io.github.hison.utils`** (groupId와 다름 — 혼동 주의)
- `Utils`는 final 클래스 + private 생성자. 모든 메서드가 `Utils.xxx()` 정적 호출
- **설정은 classpath의 `application.properties`에서 로드** (static 블록). 키: `hison.utils.format.date`, `hison.utils.format.datetime`, `hison.utils.type.date-add/date-diff/dayofweek`, `hison.utils.charbyte.less2047/less65535/greater65535`, `hison.utils.format.number`, `hison.utils.propertie.file.path`
- 기능 분류: Boolean 판별(isAlpha 등 19종) / 날짜(addDate, getDateDiff 등) / 숫자(getCeil/getFloor/getRound/getTrunc — String·int·long·float·double 오버로드) / 문자열(getByteLength, getLpad 등) / 기타(nvl, getClientIpAddress, getFileExtension/getFileName) / 프로퍼티 조회(getPropertyValue, getProperties 등)
- 클라이언트(hisonjs)의 `hison.utils`가 동일 개념의 JS 대응 기능 제공

## 상세 문서

- 가이드: `../../../md/hisondev-utils.md` (소스 검증 완료)
- 전체 메서드 표: `../../../md/hisondev-hisonjv.md`의 Utils 섹션 (소스와 일치 확인됨)
- 생태계 전체: `../../../md/hisondev-ecosystem.md`

## 알려진 이슈 (수정 금지 — 추후 소유자와 재정리 예정)

1. README.md가 구버전: groupId `io.github.hison`(오기), version 1.0.2, "JDK 8+" — 현재는 io.github.hisondev / 2.0.0 / Java 21
2. README/javadoc의 `hison-utils-config.properties` 안내는 낡음 — **실제(의도된) 동작은 application.properties 로드**
3. javadoc `@version 1.0.2` 방치, UtilsException javadoc이 "DataException"을 언급(복붙 흔적)
4. additional-spring-configuration-metadata.json의 description이 placeholder 상태

## 작업 규칙

- 이 저장소의 소스/README 수정은 사용자의 명시적 지시가 있을 때만 진행 (프로젝트 루트 CLAUDE.md 규칙 준수)
