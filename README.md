<img src = "https://user-images.githubusercontent.com/80062632/178316819-9873c137-bcbc-4162-afae-095e1a8e99ce.png">  <br>

# happic: 해픽

**Be happy, take a happic:**

> **매일의 추억이 나의 행복이 되어, 해픽** <br>
> 저희는 해픽입니다
>
> SOPT 30th APP JAM <br>
> 프로젝트 기간 : 2021.07.02 ~ 2021.07.23


<br>

<br>

##  Team happic Android Developers
 <img src="https://user-images.githubusercontent.com/33388801/178488528-949c5fde-436f-428b-8f87-3d4e91ec11c9.png" width="500"> | <img src="https://user-images.githubusercontent.com/33388801/178488555-eddf3963-a2df-41d3-95cf-e50083c97336.png" width="500"> | <img src="https://user-images.githubusercontent.com/33388801/178488565-4c50de37-43d2-4ab6-aa0f-d0b8b050c711.png" width="500"> |
 :---------:|:----------:|:---------:
 문명주 | 김다희 | 정설희 |
[mym0404](https://github.com/mym0404) | [kimdahee7](https://github.com/kimdahee7) | [xxeol2](https://github.com/xxeol2) |

<br>
<br>

## Development Environment and Using Library

- Kotlin `1.6.21`
- Android Gradle Plugin `7.1.2`

<br>
<br>

## 사용한 기술 스택

[노션](https://www.notion.so/0cb29f05b7e449f4bfee21e8d9d4d2b0)

- Coroutine
- MVVM
- Data/ViewBinding

## 폴더링 구조

[노션](https://www.notion.so/cf2a338835f042ba9de50c0e462c8b44)
- `extension` 코틀린 확장 함수
    - `XXXExtension.kt`
    - 위치에 제한을 두지 않고, 필요할 때 마다 적절한 패키지에 정의하여 적절한 파일 이름을 붙인 뒤 함수들을 정의한다.

- `ui`: User Interface와 관련된 요소들
    - `acitivty`: 액티비티 파일
        - `XXXActivity.kt`
    - `fragment` : 프라그먼트 파일
        - `XXXFragment.kt`
    - `widget` : 커스텀 뷰같은 위젯들 파일
        - `MyButton.kt`
    - `dilaog` : 팝업
        - `XXXDialog.kt`
        - `XXXBottomSheetDialog.kt`
- `util` 유틸리티들
    - `NumberUtil.kt`
    - `LocationFollower.kt`
- `data` : 모델 클래스나 api, repository 등 데이터와 관련된 요소들
    - `api` : Api와 관련된 요소들
        - `XXXApi.kt`
        - `XXXDTO.kt`
    - `model` : 대개 데이터 클래스가 될 앱에서 사용하는 데이터를 의미하는 모델들
        - `XXXModel.kt`


## 서비스 이름과 간단한 프로젝트 설명

- 서비스 이름: 해픽 - Happic:)
- 설명: 하루에 기억에 남는 일을 행복을 위해 사진을 찍어 태그와 함께 기록하는 어플리케이션

## 각자 맡은 역할
 
### 문명주

- 깃 컨벤션 정의(rule)
- 프로젝트 구조 정의(rule)
- 설정 페이지(feature)
- 추가 예정

### 김다희

- 캐릭터 선택 페이지(feature)
- 추가 예정

### 정설희

- 하루해픽 사진 페이지(feature)
- 회의 서기
- 추가 예정


---

<img src = "https://user-images.githubusercontent.com/80062632/178400592-f38ba33e-d58e-4ecf-9c1a-96fec9f305a6.png" width="100"> 
