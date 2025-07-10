# 💬 ChattingRoom - Private WebSocket Chat + JWT + OAuth2 Login

> 실시간 채팅, 초대 기반 비공개 채팅방,  
> JWT 인증 + OAuth2 로그인, 전략 패턴 설계까지 적용한 실전형 채팅 시스템

---

## 🚀 프로젝트 개요

- **프로젝트 명**: ChattingRoom  
- **기술 스택**:  
  `Java 17`, `Spring Boot`, `Spring Security`, `JWT`, `OAuth2`, `WebSocket`, `STOMP`, `SockJS`, `Lombok`, `Gradle`

- **구현 목적**:  
  인증된 사용자 간 **프라이빗 실시간 채팅**이 가능한 시스템을 만들되,  
  **보안**, **유연한 인증 구조**, **확장성**, **전략 패턴 설계**까지 적용하여  
  **실제 서비스 수준의 품질**을 목표로 설계

---

## 🧱 핵심 기능 요약

| 카테고리 | 기능 |
|----------|------|
| 🔐 인증     | JWT 로그인 / OAuth2(Naver, Kakao, Google) 로그인 지원 |
| 🧠 인증 설계 | 전략 패턴 기반 Provider 모듈화 (GoogleLoginStrategy, KakaoLoginStrategy 등) |
| 💬 채팅     | WebSocket + STOMP 기반 실시간 채팅 |
| 🔑 채팅방   | 고유 roomKey 기반 채팅방 생성 / 입장 제어 |
| 👥 초대     | 사용자 초대 기능 (roomKey + targetUserId) |
| 🧭 사용자 토픽 | 사용자 ID 기반 동적 topic 구독 (/topic/messages/${roomKey}) |
| ✅ 접근 제어 | 인증된 사용자만 채팅방 입장 가능 |
| 🔄 세션 관리 | WebSocket 연결/종료 감지 및 상태 관리 |

---

## 🔐 OAuth2 + JWT 인증 흐름

```mermaid
sequenceDiagram
Client ->> Server: /oauth2/authorization
Server ->> OAuthProvider: Redirect (Kakao/Naver/Google)
OAuthProvider ->> Server: Access Token + 사용자 정보
Server ->> Server: 전략 패턴으로 Provider 처리
Server ->> Client: JWT 발급 (access + refresh)
Client ->> Server: WebSocket 연결 (with JWT)
