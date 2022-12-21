(ns dev-dive.data)

(def example-products
  [{:name "배추" :id 1001 :prev-price 10000 :price 2000}
   {:name "딸기" :id 1002 :prev-price 5000 :price 4500}
   {:name "바나나" :id 1003 :prev-price 2000 :price 3000}
   {:name "배" :id 1004 :prev-price 10000 :price 5000}
   {:name "사과" :id 1005 :prev-price 1000 :price 1500}
   {:name "무" :id 1006 :prev-price 10000 :price 2000}
   {:name "부추" :id 1007 :prev-price 1000 :price 900}
   {:name "양배추" :id 1008 :prev-price 500 :price 600}
   {:name "상추" :id 1009 :prev-price 2300 :price 24000}
   {:name "마늘" :id 1010 :prev-price 5000 :price 5500}])

(def product-names ["경북문경 청년농부의 표고버섯 1/2kg"
                     "러시아산 킹크랩 블루/레드  500g, 1kg"
                     "동해에서 어획한 자숙홍게 2/3kg"
                     "완도에서 자란 전복 400g/500g/1kg"
                     "세척된 해남에서 자란 고구마 2/3/5kg"
                     "미국산 우대갈비 2kg 통컷팅/반컷팅"
                     "미경산 한우 안심1++ 200/400g"
                     "미경산 한우 꽃갈비살 1++ 200/400g"
                     "숙성 에이징 초고마블 3종 선물세트"
                     "라이브커머스 & 상품페이지 제작 신청_장수유통"
                     "영암에서 자란 무화과 500g/1kg/2kg"
                     "제주 황금향 3kg"
                     "김천에서 자란 샤인머스켓/거봉 1.7kg"
                     "[예약판매] [행사] 샤인머스켓 2kg"
                     "샹그릴라 자연송이버섯 500g/1kg"
                     "신선캐시 100만원 충전"
                     "안동에서 자란 홍로사과 2-4kg"
                     "신선캐시 500만원 충전"
                     "신선캐시 1,000만원 충전"
                     "프리미엄 애플망고 선물세트 1.6kg"
                     "[감사랑농원] 명품 영동 반건시 곶감 (900g~2.2kg)"
                     "미국산 생블루베리 620g"
                     "루비에스 미니 사과 2kg"
                     "금산 인삼(수삼) 세트 600g/750g/1kg/1.5kg"
                     "21년 가을 햇전어 500g/1kg"
                     "영광 모시마루 모시송편"
                     "신선캐시 3333원 충전"
                     "의성에서 자란 꼭지있는 홍로사과 5/10kg"
                     "국내산 꾸지뽕열매 1/5kg"
                     "산지직송 통영 자연산 멍게 1kg"
                     "남해안 당일조업 활 바지락 1kg"
                     "당일조업 통영 자연산 바다장어 1kg"
                     "대왕 참가리비 1kg"
                     "초이스등급 소갈비세트 1.8kg / 3.6kg"
                     "[시대유감]"
                     "바담특가"
                     "[시대유감]"
                     "[WAU 엘에스비그룹]"
                     "사과선물세트 2.6/3kg"
                     "[예약판매] 배 선물세트 5kg"
                     "[러브잇]"
                     "모시촌 서천 모시떡,송편 1/1.2kg"
                     "담이농장 꿀밤고구마(베니하루카) 3 / 5 / 10kg"
                     "문경 유황 백송화버섯"
                     "[예약판매] 배 선물세트 5kg_요리보고"
                     "[주식회사 감동]"
                     "[솔직한농부]"
                     "여수에서 자란 생물 새꼬막 1kg~5kg"
                     "청정해역 서해안 멸치 1.5kg"
                     "충남 서천에서 온 항만박대 700g"
                     "[정가]"
                     "[더네이처파머스]"
                     "나주배 선물/특품/가정용 3-15kg"
                     "[예약판매]해남 푸른농업 HACCP 절임배추 20kg"
                     "[예약판매]해남 땅끝햇살농장 GAP 절임배추"
                     "[배민라이브]_햇터농장"
                     "정보석 농부의 지리산 하동 햇알밤/옥광밤/깐밤 2kg"
                     "영주에서 자란 믿음사과 못난이 3/6kg"
                     "영주에서 자란 믿음사과 정품 2/4kg"
                     "[예약판매]진당산참새골농장의 천황사과대추"
                     "남해 생물 피조개 1kg/3kg"
                     "당일조업 살아있는 홍가리비 1kg/5kg"
                     "해녀가 직접 채취한 자연산 뿔소라 1kg/3kg"
                     "부여에서 자란 스테비아 애플마토 500g~2kg"
                     "국내산 당근고구마 풍원미 2~5kg"
                     "(도매) 고당도 블랙 사파이어 포도 4/2kg"
                     "아삭 달달 한달만 맛 볼수있는 국내산 사과대추(특) 1kg/2kg"
                     "[예약판매]땅끝 해남 박기범 농부의 절임배추 20kg"
                     "이현수 농부의 부유단감 5kg / 10kg"
                     "김영호 농부의 부여 햇알밤 2kg / 4kg"
                     "씨없는 감 홍시용 청도반시 5kg"
                     "(도매) 안동 햇 부사 사과 10kg"
                     "황태익 농부님의 전남 고흥 석류 5kg"
                     "초신선 무항생제 친환경 근본돼지"
                     "삼초마을 로열비프"
                     "무항생제 방사 유정란 삼초계란"
                     "이성일 농부의 전남 신안 태양초 고춧가루 600g"
                     "고흥에서 자란 고흥 비파 1kg"
                     "고흥 무농약 유자 2kg 3kg 5kg 10kg"
                     "고흥 무농약 유자 못난이 3kg 5kg 10kg"
                     "[예약판매]정보석 농부님의 지리산 하동 대봉감(대봉시) 5kg / 10kg"
                     "청도 건시곶감 감말랭이 800g"
                     "[예약판매] 해남 철희네 HACCAP 절임배추"
                     "포항 구룡포 자숙홍게 2~3kg"
                     "운동하며 자란 한돈 냉장 삼겹살&목살"
                     "하늘빛 고운농원 건고추&고춧가루"
                     "윤득종 농부의 전남 함평 고춧가루 500g / 1kg"
                     "[산지경매] 사과 부사"
                     "[산지경매] 꿀밤고구마(베니하루카) 10kg"
                     "[산지경매] 피양파"
                     "[산지경매] 꿀밤고구마(베니하루카) 1kg당"
                     "전북 남원에서 온 국내산 블루베리 1kg/2kg"
                     "부여에서 자란 스테비아 애플마토 1kg/2kg/4kg"
                     "성주 햇 참외 2kg/3kg/4kg"
                     "법적 머시깽이"
                     "사과 (과세상품)"
                     "[예약판매] 고창 저탄소 GAP인증 멜론 4/8kg"
                     "자동관리 감자"
                     "하우스 감귤"
                     "백도 복숭아"
                     "천도 복숭아"
                     "칠레산 생 블루베리 3팩/6팩/12팩"
                     "충청/경상 수확 시나노골드 2/3kg "
                     "황도 복숭아"
                     "미시마 사과"
                     "시나노레드 사과"
                     "아오리 사과"
                     "대석 자두"
                     "대왕 자두"
                     "도담 자두"
                     "자두(기타)"
                     "추희 자두"
                     "홍자두"
                     "후무사 자두"
                     "거봉"
                     "델라웨어"
                     "샤인머스켓"
                     "캠벨얼리"
                     "가야백자 메론"
                     "얼스계"
                     "대추방울토마토"
                     "송이토마토"
                     "복수박"
                     "속노란 수박"
                     "수박"
                     "애플 수박"
                     "흑수박"
                     "찰 토마토"
                     "쿠마토(흑토마토)"
                     "가지"
                     "노각 오이"
                     "다다기오이"
                     "청오이"
                     "취청오이"
                     "애호박"
                     "쥬키니 호박"
                     "풋호박(둥근호박)"
                     "새송이버섯"
                     "표고버섯"
                     "자주 감자"
                     "홍감자"
                     "붉은 고구마"
                     "호박 고구마"
                     "미니 파프리카"
                     "빨간 파프리카"
                     "깻단"
                     "깻잎순"
                     "깻잎"
                     "돌미나리(밭미나리)"
                     "미나리(일반)"
                     "영양 부추"
                     "적상추"
                     "적포기 상추"
                     "쫑상추"
                     "청상추"
                     "청포기"
                     "흑적상추"
                     "시금치"
                     "양배추"
                     "얼갈이 배추"
                     "열무"
                     "꽈리 고추"
                     "깐대파"
                     "대파"
                     "롱그린 풋고추"
                     "아삭이 고추"
                     "오이맛 고추"
                     "청양 고추"
                     "청홍 풋고추"
                     "풋고추(기타)"
                     "홍고추"
                     "홍청양고추"
                     "만생양파"
                     "중생양파"
                     "저장양파"
                     "조생양파"
                     "머스크 메론"
                     "가을 무"])

(def portal-example
  {:examples.data/list '(0 1 2),
   :examples.data/vector [1 2 4],
   :examples.data/url-string "https://github.com/djblue/portal",
   :examples.data/keyword :hello-world,
   :examples.data/nil nil,
   :examples.data/set #{1 3 2},
   :examples.data/character "A",
   :examples.data/ns-keyword :examples.data/hello-world,
   :examples.data/booleans #{true false},
   :examples.data/nested-vector [1 2 3 [4 5 6]],
   "string-key" "string-value"})

(def portal-string-data
  {:examples.data/json "{\"hello\": 123}"
   :examples.data/edn
   "^{:portal.viewer/default :portal.viewer/tree} {:hello 123}",
   :examples.data/csv "a,b,c\n1,2,3\n4,5,6",
   :examples.data/markdown
   "# portal\n\nA clojure tool to navigate through your data.\n\n[![Clojars Project][clojars-badge]][clojars]\n[![VS Code Extension][vscode-badge]][vscode]\n[![Version][intellij-badge]][intellij]\n[![Get help on Slack][clojurians-badge]][clojurians]\n\n<a href=\"https://djblue.github.io/portal/\">\n<picture>\n  <source media=\"(prefers-color-scheme: dark)\" srcset=\"https://user-images.githubusercontent.com/1986211/196015562-238cf450-6467-451c-a985-04c7a9b49dba.png\">\n  <source media=\"(prefers-color-scheme: light)\" srcset=\"https://user-images.githubusercontent.com/1986211/196015567-74ba9153-341a-4fd7-be47-2c26f0c88c2e.png\">\n  <img src=\"https://user-images.githubusercontent.com/1986211/196015562-238cf450-6467-451c-a985-04c7a9b49dba.png\">\n</picture>\n</a>\n\nThe portal UI can be used to inspect values of various shapes and sizes. The UX\nwill probably evolve over time and user feedback is welcome!\n\nFor an in-depth explanation of the UI, you can jump to the [UI][ui-concepts]\ndocs.\n\n## Demo\n\nTo get an overview of the Portal UI and workflow, checkout the following\nrecording of a [live demo][live-demo] I gave for [London\nClojurians][london-clojurians].\n\n<a href=\"https://www.youtube.com/watch?v=Tj-iyDo3bq0\">\n<img src=\"https://img.youtube.com/vi/Tj-iyDo3bq0/hqdefault.jpg\" alt=\"London Clojurians Demo\" />\n</a>\n\n## Usage\n\nTo start a repl with portal, run the **clojure >= 1.10.0** cli with:\n\n```bash\nclj -Sdeps '{:deps {djblue/portal {:mvn/version \"0.33.0\"}}}'\n```\n\nor for a **web** **clojurescript >= 1.10.773** repl, do:\n\n```bash\nclj -Sdeps '{:deps {djblue/portal {:mvn/version \"0.33.0\"}\n                    org.clojure/clojurescript {:mvn/version \"1.10.844\"}}}' \\\n    -m cljs.main\n```\n\nor for a **node** **clojurescript >= 1.10.773** repl, do:\n\n```bash\nclj -Sdeps '{:deps {djblue/portal {:mvn/version \"0.33.0\"}\n                    org.clojure/clojurescript {:mvn/version \"1.10.844\"}}}' \\\n    -m cljs.main -re node\n```\n\nor for a **babashka >=0.2.4** repl, do:\n\n```bash\nbb -cp `clj -Spath -Sdeps '{:deps {djblue/portal {:mvn/version \"0.33.0\"}}}'`\n```\n\nor for a Leiningen project:\n\n- Add Portal as a dependency, either to `:dev` profile or its own profile:\n\n```clojure\n{:profiles {:dev {:dependencies [[djblue/portal \"0.33.0\"]]}}}\n```\n\nor\n\n```clojure\n{:profiles {:portal {:dependencies [[djblue/portal \"0.33.0\"]]}}}\n```\n\nOr as a global profile, add to `~/.lein/profiles.clj`:\n\n```clojure\n{:portal {:dependencies [[djblue/portal \"0.33.0\"]]}}\n```\n\nIf you add Portal to a profile other than `:dev`, when starting a REPL\nstart it with `with-profiles +portal`. The `+` is important.\n\nor for examples on how to integrate portal into an existing project, look\nthrough the [examples](./examples) directory.\n\n> **Note**\n> Portal can also be used without a runtime via the [standalone version](./doc/guides/standalone.md).\n\n### API\n\nTry the [portal api](./src/portal/api.cljc) with the following commands:\n\n```clojure\n;; for node and jvm\n(require '[portal.api :as p])\n\n;; for web\n;; NOTE: you might need to enable popups for the portal ui to work in the\n;; browser.\n(require '[portal.web :as p])\n\n\n(def p (p/open)) ; Open a new inspector\n\n;; or with an extension installed, do:\n(def p (p/open {:launcher :vs-code}))  ; jvm / node only\n(def p (p/open {:launcher :intellij})) ; jvm / node only\n\n(add-tap #'p/submit) ; Add portal as a tap> target\n\n(tap> :hello) ; Start tapping out values\n\n(p/clear) ; Clear all values\n\n(tap> :world) ; Tap out more values\n\n(prn @p) ; bring selected value back into repl\n\n(remove-tap #'p/submit) ; Remove portal from tap> targetset\n\n(p/close) ; Close the inspector when done\n```\n\n> **Warning**\n> Portal will keep objects from being garbage collected until they are cleared\n> from the UI.\n\n### Options\n\nOptions for `portal.api/open`:\n\n| Option          | Description                                 | Default             | Spec                                                                        |\n|-----------------|---------------------------------------------|---------------------|-----------------------------------------------------------------------------|\n| `:window-title` | Custom window title for UI                  | \"portal\"            | string?                                                                     |\n| `:theme`        | Default theme for UI                        | :portal.colors/nord |                                                                             |\n| `:value`        | Root value of UI                            | (atom (list))       |                                                                             |\n| `:app`          | Launch UI in Chrome app window              | true                | boolean?                                                                    |\n| `:launcher`     | Launch UI using this editor                 |                     | #{[:vs-code][vs-code-docs] [:intellij][intellij-docs] [:emacs][emacs-docs]} |\n| `:editor`       | Enable editor commands, but use separate UI |                     | #{[:vs-code][vs-code-docs] [:intellij][intellij-docs] [:emacs][emacs-docs]} |\n| `:port`         | Http server port for UI                     | 0                   | int?                                                                        |\n| `:host`         | Http server host for UI                     | \"localhost\"         | string?                                                                     |\n\n\nFor more documentation, take a look through the [docs][docs].\n\n[clojars]: https://clojars.org/djblue/portal\n[clojars-badge]: https://img.shields.io/clojars/v/djblue/portal?color=380036&style=flat-square\n[vscode]: https://marketplace.visualstudio.com/items?itemName=djblue.portal\n[vscode-badge]: https://vsmarketplacebadge.apphb.com/version-short/djblue.portal.svg?color=007ACC&label=vs-code&logo=vs&style=flat-square\n[intellij]: https://plugins.jetbrains.com/plugin/18467-portal\n[intellij-badge]: https://img.shields.io/jetbrains/plugin/v/18467?style=flat-square&label=intellij\n\n[clojurians]: https://clojurians.slack.com/channels/portal\n[clojurians-badge]: https://img.shields.io/badge/slack-clojurians%20%23portal-4A154B?color=63B132&style=flat-square\n\n[live-demo]: https://www.youtube.com/watch?v=Tj-iyDo3bq0\n[london-clojurians]: https://www.youtube.com/channel/UC-pYfofTyvVDMwM4ttfFGqw\n[docs]: https://cljdoc.org/d/djblue/portal/0.33.0/doc/ui-concepts\n[ui-concepts]: https://cljdoc.org/d/djblue/portal/0.33.0/doc/ui-concepts\n\n[vs-code-docs]: ./doc/editors/vs-code.md\n[intellij-docs]: ./doc/editors/intellij.md\n[emacs-docs]: ./doc/editors/emacs.md#xwidget-webkit-embed\n"})

(defn products-from-db []
  (->> (range (count product-names))
       (map (fn [id] {:id id
                      :name (nth product-names id)}))
       (map (fn [product] (assoc product :prev-price (+ 100 (* (rand-int 100) 100)))))
       (map (fn [product] (assoc product :price (+ 100 (* (rand-int 100) 100)))))))

(def test-results [{:subject :math, :score 100, :grade "A+", :name "철수"}
                   {:subject :english, :score 90, :grade "A", :name "철수"}
                   {:subject :science, :score 80, :grade "B", :name "철수"}
                   {:subject :math, :score 75, :grade "B", :name "영희"}
                   {:subject :english, :score 100, :grade "A+", :name "영희"}
                   {:subject :science, :score 80, :grade "B", :name "영희"}
                   {:subject :math, :score 50, :grade "C+", :name "짱구"}
                   {:subject :english, :score 40, :grade "C", :name "짱구"}
                   {:subject :science, :score 0, :grade "F", :name "짱구"}])

(def names ["짱구" "철수" "유리" "맹구" "훈이" "흰둥이"])

(defn score->grade [score]
  (cond
    (>= score 95) "A+"
    (>= score 90) "A"
    (>= score 85) "B+"
    (>= score 80) "B"
    (>= score 75) "C+"
    (>= score 70) "C"
    (>= score 65) "D+"
    (>= score 60) "D"
    :else "F"))

(defn generate-test-results []
  (-> (for [name names
            subject [:math :english :science :history :korean :music]]
        (let [score (+ 50 (rand-int 51))]
          {:name    name
           :subject subject
           :score   score
           :grade   (score->grade score)}))
      shuffle))
