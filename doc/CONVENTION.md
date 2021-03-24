# CODING-CONVENTION
- [naver coding convention](http://docs.navercorp.com/coding-convention/java.html)
- ./rule 아래에 `naver-checkstyle-rules.xml`, `naver-intellij-formattter.xml` 파일이 위치한다.

### IntelliJ Formatter 적용
1. `File` -> `Settings` -> `Editor` -> `Code Style` -> `Java` 항목으로 이동
2. `Scheme` 메뉴에서 `Import Scheme` -> `IntelliJ IDEA code style XML` 을 선택
3. `./rule` 아래에 위치한 `naver-intellij-formattter.xml` 파일을 선택한 후 적용

![스크린샷 2021-03-22 오후 6 38 27](https://user-images.githubusercontent.com/45280737/111969670-d5226880-8b3d-11eb-968b-0b60af2fe36e.png)

### Null
- Null Validation 이 필요한 경우 `@NotNull` 을 사용해서 validation 시점을 한가지로 통일한다.
  - `@javax.validation.NotNull` 은 Validation 이 필요한 not-null 속성에 붙인다.
- `@Nullable` 을 사용하여 null 이 될 수 있는 경우를 명시적으로 표현해준다.
  - 메서드 파라미터에도 `null` 이 될 수 있는 필드에 한해 `@Nullable` 을 적용할 수 있다.
- 기본적으로 메서드에서 `null` 을 리턴하지 않도록 하며, 가능성이 있을 경우 `Optional` 을 사용한다.

#### List, Set, Map 사용 시 Null-Safe 처리
- 기본적으로 로직 내에서는 `null` 대신 `empty()` 를 활용한다.
- Nullable 속성일 경우 해당 필드에 `@Nullable` 을 명시하고 `getter` 를 구현하여 공통적으로 Null 을 방지한다.
- 외부에 (public) List, Set, Map 필드를 제공한다면 `unmodifiableList` 를 활용한다.

```java
@Getter
public static class Sample {
     @Nullable
     private List<String> list;

     public List<String> getList() {
         return isNull(list) ? emptyList() : unmodifiableList(list);
     }

     public void add(String element) {
          if (this.list == null) {
               this.list = new ArrayList();
          }

          this.list.add(element);
     }
}
```

### Enum 타입 변수 이름
Enum 변수 이름과 맞추어 `Type` 으로 suffix 를 붙인다.
```java
private MemberType memberType;
```

### Utility 클래스 정의
- static 메소드로 구성된 Utility 클래스에는 lombok 의 `@UtilityClass` 를 붙인다.
- Util 특성을 나타내는 이름의 복수형을 사용한다. (ex: `PhoneNumbers`, `Times`)
- 의존하는 라이브러리에 동일한 이름이 있다면, `Supports` 를 postfix 로 붙인다. (Collections -> CollectionSupports)
- `com.bluebird.pipit.xxx.support` 패키지 안에서 작성한다.

### 객체 생성
- Constructor : 3개 이하의 parameter 로 객체를 생성하는 경우
- Builder Pattern : 4개 이상의 parameter 로 객체를 생성하는 경우
- Static Factory Method : 객체 생성에 의미 부여가 필요한 경우
