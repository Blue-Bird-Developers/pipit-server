# 버전 관리 정책

## Commit Log
- `[#(이슈번호)] (설명)` 형식으로 한다.
  - 예: [#123] 회원 가입 API 추가
  - 코드에 반영이 필요한 건은 가능한 한 이슈 업 한 뒤 PR 한다.
  - 이슈로 정의하기 애매하다고 판단되는 Minor 변경사항은 다음과 같이 정리한다. (예: 오타 수정, minor refactoring ...)
    - 예시
        - (Push 시점) `Commit Log` : `CONVENTION 문서 오타 수정`
        - (PR Merge 시점) 최종 `Commit Log` : `[#PR번호] CONVENTION 문서 오타 수정`
    - 하지만 조그마한 것이라도 이슈로 작성한다면 팀원 간 공유 및 합의가 쉬우므로, Issue Up 을 지향한다.
- 한글로 간결하게 적는다.
- '한다', '합니다' 등의 postfix 는 생략한다.
- 마침표는 생략한다.
- commit 로그는 가급적 짧게 작성한다.
  - 자세한 설명이 필요하다면 본문을 이용한다.
- [Github 의 'Squash and Merge' 기능](https://meetup.toast.com/posts/122) 을 사용하여 Merge 시점에 Commit 제목에 PR 번호를 포함한다.
  - `[#이슈번호] 설명 (#PR번호)`

## Branch
- base branch 는 develop 으로 설정한다.
- develop 으로 직접 commit 하지 않는다.
- PR merge 작업은 PR 을 최초 작성한 사람이 수행하는 것을 원칙으로 한다.
  - 공유를 목적으로 필수 인원 approve 후 merge 한다.
  - approve 최소 인원을 정한뒤 충족하면 merge 한다.
