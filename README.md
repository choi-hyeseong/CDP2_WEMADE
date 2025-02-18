#  혈압 어때?
**고혈압 예방 및 관리 애플리케이션**

![Image](https://github.com/user-attachments/assets/7c227a97-32d6-47f4-ac54-59c7ae50cf6f)

## ❤️ Overview
고혈압은 전 세계적으로 수백만 명의 사람들이 겪고 있는 만성 질환 중 하나로, 조기 발견과 지속적인 관리 없이는 심장 질환, 뇌졸중, 신장 문제와 같은 심각한 건강 문제를 초래할 수 있습니다. 

그러나 많은 고혈압 환자들이 자신의 상태를 적절히 모니터링하고 관리하는 데 어려움을 겪고 있습니다.
고혈압 관리 어플리케이션은 환자들이 자신의 혈압 수치를 정기적으로 모니터링하고, 생활 습관을 개선하여 고혈압을 효과적으로 관리할 수 있도록 돕는 것을 목적으로 합니다.

본 애플리케이션은 사용자의 혈압 수치, 스트레스 수준, 수면 패턴 등 다양한 건강 정보를 추적하고 분석하여, 고혈압의 현재 위험도를 평가하고 이에 대한 주기적인 관찰을 가능하게 합니다. 또한, 스트레스와 수면 장애가 고혈압 위험도에 미치는 영향을 분석함으로써, 사용자에게 맞춤형 건강 관리 조언을 제공합니다.

## 🖥️ 구성도
![Image](https://github.com/user-attachments/assets/3909e977-60db-4bbd-bb0e-d47594685c42)
### 🖌️ Figma
https://www.figma.com/design/iqkKv573XbE8gkRUBGKXKV/%EA%B3%A0%ED%98%88%EC%95%95-%EC%98%88%EB%B0%A9-%EB%B0%8F-%EA%B4%80%EB%A6%AC-%EC%96%B4%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98?m=auto&t=V3xvPBXho4jmMhge-6

### 📱 Android
* 스마트폰에 내장된 만보기 센서, 웨어러블 디바이스에서 가져올 수 있는 혈압, 심박수등의 정보를 HealthConnect API를 이용해서 수집
* API를 통해 수집된 데이터를 조회하고, 백엔드에 고혈압의 위험성을 측정할 수 있는 AI를 이용해 사용자에게 고혈압의 위험도 결과를 반환.
* 이전에 수집된 건강정보를 API로 부터 가져와 대시보드에 나타내어 사용자가 한눈에 보기 쉽게 편성
* Coroutine을 활용한 비동기 처리로 메인 쓰레드 블로킹 최소화. 차트 파싱 과정에서 병렬적으로 처리하도록 하여 성능 향상
* Sandwich 라이브러리 사용으로써 REST API가 실패하더라도 대응 로직 구현
* Hilt와 MVVM 아키텍처를 이용하여 결합도를 낮춘 구조 및 View를 제외한 나머지 구문 커버리지 90% 이상 달성

### 💽 Backend
* Android에서 요청된 건강 정보를 토대로 AI를 통한 고혈압 위험도 체크 수행
* Next js를 이용하여 백엔드 서버 구축 및, TensorFlow js 환경에서 AI 구동
* JWT를 이용하여 Stateless한 요청 구성

### 🤖 AI
* CNN을 이용한 고혈압 여부 예측, 기존 DNN 모델에서는 정확도가 0.78로 낮았으나, CNN으로 변경 후 정확도가 0.85정도로 높아진것을 확인할 수 있음.
* 국민건강영양조사(제7기, 2018) 데이터셋을 이용해 고혈압 여부에 대한 학습을 수행하였음.

## ☠️ 트러블 슈팅
### AI Warm-up 문제
프론트엔드(안드로이드)에서 백엔드로 고혈압 여부 예측 요청시 AI 모델을 불러오는 과정에서 상당한 시간이 소요되었습니다.<br/>
이로 인해, 프론트엔드에서 최초 요청시 응답을 받기까지 걸리는 소요시간인 TAT(Turn Around Time)가 상당히 길어졌습니다.<br/>
2번째 요청부터는 AI모델이 메모리에 적재되어 즉각적인 응답이 가능했으나, 최초로 요청한 시점에서 소요되는 시간이 크게 느껴질 수 밖에 없었습니다.

➡️ 백엔드 start시 클래스 초기화 시점에 AI 모델 로드를 수행해, cold-start 문제를 해결하였습니다.
### 차트 구성시 중복된 코드 다수 존재
혈압(수축기, 이완기), 키, 몸무게와 같은 모델 데이터는 (TimeStamp - 측정시간, Double - 수치)형식으로 저장되어 있습니다.<br/>
이 모델을 이용해, 차트 (TimeStamp, Double)형식으로 변경해 차트로 나타내는 작업을 수행하였습니다.<br/>
이때, 각 모델(혈압, 키..)은 사실상 클래스 이름만 다를뿐 내부 데이터는 같은 상황에서, 모델을 차트로 파싱하는 클래스를 각각 만드니 중복된 코드가 다수 발견되었습니다.<br/>

➡️ 차트로 파싱하는 핵심 메소드를 abstract 화 시켜 통합시킨 뒤, 모델에서 데이터를 추출 하는 과정만 abstract method로 구현하도록 해 중복된 코드를 최소화 하였음 + 확장 가능성을 흭득



## 📷 시연 영상
https://www.youtube.com/watch?v=VWvPCwvHslI
